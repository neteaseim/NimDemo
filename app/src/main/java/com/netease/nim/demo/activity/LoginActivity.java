package com.netease.nim.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.nim.demo.R;
import com.netease.nim.demo.RegisterHttpClient;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.string.MD5;
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


    private void register() {
        final String account = edtRegisterUserAccount.getText().toString();
        String nick = edtRegisterUserName.getText().toString();
        final String password = edtRegisterPassword.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(nick) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "相关信息不能为空", Toast.LENGTH_SHORT).show();
        }
        RegisterHttpClient.getInstance().register(account, nick, password, new RegisterHttpClient.HttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this, "注册成功 ", Toast.LENGTH_SHORT).show();
                login(account, password);

            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String name, String password) {

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "相关信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        password = MD5.getStringMD5(password);

        NIMClient.getService(AuthService.class).login(new LoginInfo(name, password)).setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo o) {
                NimUIKit.loginSuccess(o.getAccount());
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                Toast.makeText(LoginActivity.this, "登录异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
