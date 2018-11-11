package com.netease.nim.demo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.netease.nim.demo.utils.BaseUtil;
import com.netease.nim.demo.utils.BaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class RegisterHttpClient {
    private static final String TAG = "RegisterHttpClient";

    private static final int RES_CODE_SUCCESS = 200;
    private static final String CHARSET = "UTF-8";
    private static final String RESULT_KEY_RES = "res";
    private static final String RESULT_KEY_ERROR_MSG = "errmsg";
    private static final String REGISTER_URL = "https://app.netease.im/api/createDemoUser";


    public interface HttpCallback {
        void onSuccess();

        void onFailed(int code, String errorMsg);
    }

    private Handler handler = new Handler(BaseInfo.getContext().getMainLooper());

    public static RegisterHttpClient getInstance() {
        return InstanceHolder.REGISTER_HTTP_CLIENT;
    }


    private RegisterHttpClient() {
    }


    /**
     * 向应用服务器创建账号（注册账号）
     * 由应用服务器调用WEB SDK接口将新注册的用户数据同步到云信服务器
     */
    public void register(String account, String nickName, String password, final HttpCallback callback) {
        password = BaseUtil.getStringMD5(password);
        try {
            nickName = URLEncoder.encode(nickName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final Map<String, String> headers = new HashMap<>(1);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        headers.put("User-Agent", "nim_demo_android");
        headers.put("appkey", readAppKey());

        final String body = "username=" + account.toLowerCase() +
                "&nickname=" + nickName +
                "&password=" + password;

        new AsyncTask<Void, Void, RegisterResult>() {
            @Override
            protected RegisterResult doInBackground(Void... voids) {
                RegisterResult result = post(REGISTER_URL, headers, body);
                return result;
            }

            @Override
            protected void onPostExecute(RegisterResult result) {
                if (result == null) {
                    callback.onFailed(-1, "异常失败");
                    return;
                }
                if (result.code != RES_CODE_SUCCESS) {
                    callback.onFailed(result.code, result.message);
                    return;
                }
                callback.onSuccess();
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null);
    }

    private String readAppKey() {
        try {
            ApplicationInfo appInfo = BaseInfo.getContext().
                    getPackageManager().
                    getApplicationInfo(BaseInfo.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private RegisterResult post(final String urlStr, final Map<String, String> headers, String body) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = buildPost(urlStr, headers, body);

            int resCode = urlConnection.getResponseCode();
            String response = null;
            if (resCode == RES_CODE_SUCCESS) {
                response = buildString(urlConnection.getInputStream());
            }

            if (TextUtils.isEmpty(response)) {
                return null;
            }
            JSONObject jsonObject = new JSONObject(response);
            return new RegisterResult(jsonObject.getInt(RESULT_KEY_RES), jsonObject.optString(RESULT_KEY_ERROR_MSG));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            BaseUtil.disconnect(urlConnection);
        }
        return null;
    }

    private static HttpURLConnection buildPost(String urlStr, final Map<String, String> headers, String body) {
        DataOutputStream dataOutput = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(30 * 1000);
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            urlConnection.setRequestProperty("charset", CHARSET);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    urlConnection.setRequestProperty(key, headers.get(key));
                }
            }

            dataOutput = new DataOutputStream(urlConnection.getOutputStream());
            dataOutput.write(body.getBytes(CHARSET));
            dataOutput.flush();

        } catch (IOException exce) {
            exce.printStackTrace();
        } finally {
            BaseUtil.close(dataOutput);
        }

        return urlConnection;
    }


    private String buildString(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            int len;
            byte buffer[] = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return new String(outputStream.toByteArray(), CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            BaseUtil.close(inputStream);
        }
        return null;
    }

    private class RegisterResult {
        int code;
        String message;

        public RegisterResult(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    private static class InstanceHolder {
        private static final RegisterHttpClient REGISTER_HTTP_CLIENT = new RegisterHttpClient();
    }
}
