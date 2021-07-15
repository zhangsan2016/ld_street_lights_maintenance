package com.example.ld_street_lights_maintenance.act;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.ld_street_lights_maintenance.util.HttpConfiguration;
import com.example.ld_street_lights_maintenance.util.HttpUtil;
import com.example.ld_street_lights_maintenance.util.LogUtil;
import com.example.ld_street_lights_maintenance.util.SpUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONStringer;

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

    String username, password, serviceAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.login);


        username = (String) SpUtils.getValue("username", "");
        password = (String) SpUtils.getValue("password", "");

        if (!TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(password)) {
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
                serviceAddress = ((EditText) findViewById(R.id.txt_service_address))
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

                String url = serviceAddress + HttpConfiguration.PROFILE + HttpConfiguration.CONTENT_TYPE_USER_LOGIN;

                RequestBody requestBody = new FormBody.Builder()
                        .add("strTemplate", "{\"ischeck\":$data.rows}")
                        .add("username", username)
                        .add("password", password)
                        .add("strVerify", "[admin]")
                        .build();

                LogUtil.e("requestBody = " + requestBody.toString());
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

						/*Log.e("xxx", "成功 json = " + json);
						stopProgress();*/

                        // {"errno":0,"errmsg":"","data":{"grantedActions":null}}
                        Gson gson = new Gson();
                        LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
                        if (loginInfo.getErrno() == 0) {

                            Log.e("xxx", "成功 json = " + json);
                            // cookie持久化
                            // 保存用户名和密码
                            SpUtils.putValue("username", username);
                            SpUtils.putValue("password", password);
                            SpUtils.putValue(SpUtils.LOGIN_INFO, json);


            /*    Intent intent = new Intent(LoginAct.this, ParameterAct.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ParameterAct.FRAGMENT_FLAG, ParameterAct.MAIN);
                bundle.putSerializable("loginInfo", loginInfo);
                intent.putExtras(bundle);
                startActivity(intent);*/


                            Intent intent = new Intent(LoginAct.this, MainActivity.class);
                            startActivity(intent);


                            stopProgress();
                            LoginAct.this.finish();

                        } else {
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



    private void Check() throws JSONException {

     /*   String url = serviceAddress + HttpConfiguration.PROFILE + HttpConfiguration.CONTENT_TYPE_DEVICE_LAMP_LIST;
        String postBody = "{\"where\":{\"UUID\":\"" + "83140000862285035977697" + "\"},\"size\":2000}";
        RequestBody requestBody = FormBody.create( postBody,MediaType.parse("application/json"));*/

/*
        String url = serviceAddress + HttpConfiguration.PROFILE + HttpConfiguration.CONTENT_DEVICE_EDIT + "?upsert=1&id=77497";
            RequestBody requestBody = new FormBody.Builder()
                .add("LNG", "666.58")
                .build();*/


        String url = serviceAddress + HttpConfiguration.PROFILE + HttpConfiguration.CONTENT_DEVICE_EDIT;

        // String postBody = "{\"data\":{ \"LNG\":"+"106.541652"+",\"\"LAT:" +"29.803828" +"},\"where\":{ \"UUID\":"+"000000000000000000000022" +"} }";
        JSONStringer jsonstr = new JSONStringer()
                .object().key("data")
                .object().key("LNG").value("106.541654")
                .key("LAT").value("29.803827")
                .endObject()
                .key("where").object().key("UUID").value("000000000000000000000022")
                .endObject().endObject();
        //  String postBody = "{\"data\":{ \"LNG\":\"106.541654\",\"LAT\":\"29.803828\"},\"where\":{ \"UUID\":\"000000000000000000000022\"} }";
        RequestBody requestBody = FormBody.create(jsonstr.toString(), MediaType.parse("application/json"));


        HttpUtil.sendHttpRequest(url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LoginAct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("连接服务器异常！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                LoginAct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + response.body().string());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }, "7650a440-8e01-11eb-bb68-2f7d714262b3", requestBody);
    }


    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress = ProgressDialog.show(LoginAct.this, "", "Loading...");
            }
        });

    }

    private void stopProgress() {
        mProgress.cancel();
    }

    private void showToast(String msg) {
        Toast.makeText(LoginAct.this, msg, Toast.LENGTH_LONG).show();
    }

    private String uuidToString(byte[] a) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            b.append(a[i]);
            if (i != a.length - 1) {
                b.append(",");
            }
        }
        return b.toString();
    }


    /**
     * 根据用户名转换为ascii码，拼接成uuid
     *
     * @param username 用户名
     * @return
     */
    public static boolean getAppUuid(String username) {
        byte[] result = new byte[username.length()];
        int max = username.length();
        for (int i = 0; i < max; i++) {
            char c = username.charAt(i);
            int b = (int) c;
            result[i] = (byte) b;
        }
        // 拼接appuuid
        byte[] uuidScript = getByteUuid(HttpConfiguration.UUID_SCRIPT);
        System.arraycopy(result, 0, uuidScript, uuidScript.length - result.length, result.length);

        // 判断uuid是否正确生成
        int sum = 0;
        for (int i = 0; i < uuidScript.length; i++) {
            sum += uuidScript[i];
        }
        if (sum > 1) {
            String appUuid = Arrays.toString(uuidScript);
            appUuid = appUuid.substring(1, appUuid.length() - 1);
            HttpConfiguration._Clientuuid = appUuid;
            LogUtil.e("uuidScript = " + HttpConfiguration._Clientuuid);
            return true;
        }

        return false;
    }


    /**
     * String uuid 转 byte uuid
     *
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

