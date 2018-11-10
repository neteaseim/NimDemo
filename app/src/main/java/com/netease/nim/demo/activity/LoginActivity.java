package com.netease.nim.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.nim.demo.R;
import com.netease.nim.demo.RegisterHttpClient;
import com.netease.nim.demo.utils.BaseUtil;
import com.netease.nim.demo.utils.BaseInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private View loginContainer;
    private EditText edtLoginUserAccount;
    private EditText edtLoginPassword;

    private View registerContainer;
    private EditText edtRegisterUserAccount;
    private EditText edtRegisterUserName;
    private EditText edtRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
    }

    private void setupView() {
        loginContainer = findViewById(R.id.login_container);
        edtLoginUserAccount = findViewById(R.id.edt_login_user_account);
        edtLoginPassword = findViewById(R.id.edt_login_user_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_go_register).setOnClickListener(this);

        registerContainer = findViewById(R.id.register_container);
        edtRegisterUserAccount = findViewById(R.id.edt_register_user_account);
        edtRegisterUserName = findViewById(R.id.edt_register_user_name);
        edtRegisterPassword = findViewById(R.id.edt_register_user_password);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.tv_go_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_login) {
            String account = edtLoginUserAccount.getText().toString();
            String password = edtLoginPassword.getText().toString();
            login(account, password);
        } else if (id == R.id.btn_register) {
            register();
        } else if (id == R.id.tv_go_register) {
            loginContainer.setVisibility(View.GONE);
            registerContainer.setVisibility(View.VISIBLE);
        } else if (id == R.id.tv_go_login) {
            loginContainer.setVisibility(View.VISIBLE);
            registerContainer.setVisibility(View.GONE);
        }

    }

    private boolean isRegistering;

    private void register() {
        if (isRegistering) {
            Toast.makeText(this, "正在注册中，请勿重复提交", Toast.LENGTH_SHORT).show();
            return;
        }
        final String account = edtRegisterUserAccount.getText().toString();
        String nick = edtRegisterUserName.getText().toString();
        final String password = edtRegisterPassword.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(nick) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "相关信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        isRegistering = true;
        RegisterHttpClient.getInstance().register(account, nick, password, new RegisterHttpClient.HttpCallback() {
            @Override
            public void onSuccess() {
                isRegistering = false;
                Toast.makeText(LoginActivity.this, "注册成功 ", Toast.LENGTH_SHORT).show();
                login(account, password);

            }

            @Override
            public void onFailed(int code, String errorMsg) {
                isRegistering = false;
                Toast.makeText(LoginActivity.this, "注册失败：code = " + code + " , msg = " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isInLogin;

    private void login(final String account, String password) {

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "相关信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isInLogin) {
            Toast.makeText(this, "正在登录中，请勿重复提交", Toast.LENGTH_SHORT).show();
        }
        password = BaseUtil.getStringMD5(password);
        isInLogin = true;
        NIMClient.getService(AuthService.class).login(new LoginInfo(account, password)).setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo o) {
                isInLogin = false;
                BaseInfo.setAccount(account);
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int code) {
                isInLogin = false;
                Toast.makeText(LoginActivity.this, "登录失败，code = " + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                isInLogin = false;
                Toast.makeText(LoginActivity.this, "登录异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
