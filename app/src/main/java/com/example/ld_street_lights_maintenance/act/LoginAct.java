package com.example.ld_street_lights_maintenance.act;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.entity.LoginInfo;
import com.example.ld_street_lights_maintenance.util.CustomUtils;
import com.example.ld_street_lights_maintenance.util.HttpConfiguration;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;



public class LoginAct extends Activity {
    private static final String LOGIN_RIGHT = "login_right"; // 登录成功
    private static final String ERORR_PASSWORD = "error_password"; // 密码错误
    private static final String CLIENT_NOT_EXIST = "client_not_exist"; // 该用户不存在
    private static final String CLIENT_STATE = "client_state"; // 登录成功保存用户名密码

    private ProgressDialog mProgress;
    private final String TAG_REQUEST = "MY_TAG";
    /*
     * 游客登录button
     */
    private Button touristLogin;
    /*
     * 版本信息
     */
    private int newVersionCode;
    private String newVersionName;

    private SharedPreferences preferences;
    String username,password,serviceAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.login);



        preferences = getSharedPreferences(CLIENT_STATE, 0);
        username = preferences.getString("username", "");
        password = preferences.getString("password", "");

        if(!TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(password)){
            ((EditText) findViewById(R.id.txt_user_name)).setText(username);
            ((EditText) findViewById(R.id.txt_pass_word)).setText(password);
        }




        // 测试
        //((EditText) findViewById(R.id.txt_user_name)).setText("ldshow");
        //((EditText) findViewById(R.id.txt_pass_word)).setText("123456");


        Button loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                username = ((EditText) findViewById(R.id.txt_user_name))
                        .getText().toString().trim();
                password = ((EditText) findViewById(R.id.txt_pass_word))
                        .getText().toString().trim();
                serviceAddress =((EditText) findViewById(R.id.txt_service_address))
                        .getText().toString().trim();


                if (username == null || password == null) {
                    showToast("string null!");
                    return;
                } else if (username.length() == 0 || password.length() == 0) {
                    showToast("请输入用户名和密码");
                    return;
                } else if (username.contains(" ") || password.contains(" ")) {
                    showToast("用户名和密码不能含有空格");
                    return;
                } else if (username.length() > 16 || password.length() > 16) {
                    showToast("用户名和密码长度不能超过16");
                    return;
                }
		/*		// 最高权限，直接进入系统
				else if (username.equals("hjw") && password.equals("hjw")) {
					Intent intent = new Intent(LoginAct.this, MainAct.class);
					startActivity(intent);
					LoginAct.this.finish();
					return;
				}*/
                showProgress();
                makeSampleHttpRequest();
            }
        });




    }

    private void makeSampleHttpRequest() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                //String url = "http://" + "121.40.194.91" + ":8080/ldsight/clientAction";
               // String url = "http://47.99.168.98:9001/API/CommonFn.asmx/Login";

                String url = serviceAddress + HttpConfiguration.PROFILE + HttpConfiguration.CONTENT_TYPE_USER_LOGIN;;

                RequestBody requestBody = new FormBody.Builder()
                        .add("strTemplate", "{\"ischeck\":$data.rows}")
                        .add("strName", username)
                        .add("strPwd", password + "1")
                        .add("strVerify", "[admin]")
                        .build();

                //   String url = "http://47.99.168.98:9001/api/CommonFn.asmx?op=Login";


                HttpUtil.sendHttpRequest(url, new Callback() {


                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtil.e("xxx" + "失败" + e.toString());
                        LoginAct.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("连接服务器异常！");
                            }
                        });
                        stopProgress();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();

                        Log.e("xxx", "成功 json = " + json);
						/*Log.e("xxx", "成功 json = " + json);
						stopProgress();*/

						// {"errno":0,"errmsg":"","data":{"grantedActions":null}}
                        Gson gson = new Gson();
                        LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
                        if(loginInfo.isB()){

                            Log.e("xxx", "成功" + loginInfo.getData().get(0).getResponse());
                            // cookie持久化
                            String url = loginInfo.getData().get(0).getResponse();
                            getSookie(url,loginInfo);

                        }else{
                            Log.e("xxx", "失败" + json);
                            LoginAct.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("账号或者用户名错误！");
                                }
                            });
                            stopProgress();
                        }

                    }
                }, requestBody);


            }
        }).start();


    }

    /**
     *  获取sookie做持久化操作
     * @param url Response 地址
     * @param loginInfo
     */
    private void getSookie(String url, final LoginInfo loginInfo) {

        HttpUtil.sendGetSookieHttpRequest(url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("xxx", "失败" + e.toString());
                stopProgress();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("xxx", "成功" + response.body().string());

                // 获取uuid
                boolean uuidState = getAppUuid(username);
                if(!uuidState){
                    showToast("本地生成应用UUID失败！");
                    return;
                }

                // 保存用户名和密码
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();


            /*    Intent intent = new Intent(LoginAct.this, ParameterAct.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ParameterAct.FRAGMENT_FLAG, ParameterAct.MAIN);
                bundle.putSerializable("loginInfo", loginInfo);
                intent.putExtras(bundle);
                startActivity(intent);*/


                stopProgress();
                LoginAct.this.finish();

            }
        });
    }





    private void showProgress() {
        mProgress = ProgressDialog.show(this, "", "Loading...");
    }

    private void stopProgress() {
        mProgress.cancel();
    }

    private void showToast(String msg) {
        Toast.makeText(LoginAct.this, msg, Toast.LENGTH_LONG).show();
    }

    private String uuidToString(byte[] a){
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            b.append(a[i]);
            if(i != a.length-1){
                b.append(",");
            }
        }
        return b.toString();
    }


    /**
     *  根据用户名转换为ascii码，拼接成uuid
     * @param username  用户名
     * @return
     */
    public static boolean getAppUuid(String username){
        byte[] result =  new byte[username.length()];
        int max = username.length();
        for (int i=0; i<max; i++){
            char c = username.charAt(i);
            int b = (int)c;
            result[i] =  (byte) b;
        }
        // 拼接appuuid
        byte[] uuidScript = getByteUuid(HttpConfiguration.UUID_SCRIPT);
        System.arraycopy(result, 0, uuidScript,uuidScript.length - result.length, result.length);

        // 判断uuid是否正确生成
        int sum = 0;
        for (int i = 0; i < uuidScript.length; i++) {
            sum += uuidScript[i];
        }
        if(sum > 1){
            String appUuid = Arrays.toString(uuidScript);
            appUuid =  appUuid.substring(1, appUuid.length()-1);
            HttpConfiguration._Clientuuid = appUuid;
            LogUtil.e("uuidScript = " +HttpConfiguration._Clientuuid);
            return true;
        }

        return false;
    }


    /**
     *  String uuid 转 byte uuid
     * @param uuid
     * @return
     */
    public static byte[] getByteUuid(String uuid) {
        byte[] byteUuid = new byte[16];
        String[] struuid = uuid.split(",");
        for (int i = 0; i < byteUuid.length; i++) {
            byteUuid[i] = Byte.parseByte(struuid[i]);
        }
        return byteUuid;
    }


}

