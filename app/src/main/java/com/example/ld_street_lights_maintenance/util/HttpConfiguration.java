package com.example.ld_street_lights_maintenance.util;

/**
 * Created by ldgd on 2019/3/5.
 * 功能：
 * 说明：
 */

public class HttpConfiguration {

    //	String _Clientuuid = "1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1";
   // public static final String  _Clientuuid = "13,11,99,99,99,99,99,99,99,99,99,99,99,99,99,12";
    public static String  UUID_SCRIPT = "1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";  // uuid脚本
    public static String  _Clientuuid = "1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";  // 当前app使用的uuid
    public static final String urlSend = "http://47.99.177.66:9000/send ";  // 当前 send 数据地址
    public static final String urlPoll = "http://47.99.177.66:9000/poll ";  // 当前 poll 数据地址
    public static final String urlLoginToken = "http://47.99.168.98:9002/api/Common.asmx/LoginToken";

    public static final int NET = 6;

    public class PushType{
        public final static byte pushHeart = 0;  // 心跳
        public final static byte pushData = 1;  // 推送数据
        public final static byte pushResponse = 2;  // 应答
    }




    // 后文件夹
    public   static String  PROFILE = "/api/";
    // content-type 用户登录
    public  static final String CONTENT_TYPE_USER_LOGIN = "user/login";
    // content-type 项目列表
    public    static final String CONTENT_TYPE_PROJECT_LIST = "project/list";
    // content-type 电箱路灯列表
    public  static final String CONTENT_TYPE_DEVICE_LAMP_LIST = "v_device_lamp/list";
    // content-type 电箱列表
    public   static final String DEVICE_EBOX = "v_device_ebox/list";
    // content-type 汇报设备配置
    public  static final String REPORT_CONFIG = "device/reportConfig";
    // content-type 单个设备信息
    public  static final String VIEW_BY_UUID = "device/viewByUUID";
    // content-type 获取断缆报警器列表
    public  static final String DEVICE_WIRESAFE_LIST ="v_device_wiresafe/list";
    // content-type 设备控制
    public  static final String DEVICE_CONTROL ="device/control";
    // content-type 清除报警
    public   static final String CLEAN_ALARM ="device/cleanAlarm";
    // content-type 历史记录
    public static final String HISTORY_METRICS ="device/historyMetrics";
    // content-type 检测 Token 状态
    public    static final String CONTENT_TYPE_USER_TOKEN = "user/checkToken";
    // content-type 设备列表（包含当前项目下的所有设备）
    public   static final String CONTENT_TYPE_DEVICE_LIST = "device/list";
    // content-type 汇报设备配置
    public   static final String CONTENT_DEVICE_REPORTCONFIG = "device/reportConfig";
    // content-type 设备编辑
    public   static final String CONTENT_DEVICE_EDIT = "device_lamp/edit";
    // base 地址
    static String _urlBase = "https://iot2.sz-luoding.com:2888" + PROFILE;
    // content-type 设备列表（包含当前项目下的所有设备）


    // 获取项目列表地址
    public static String  PROJECT_LIST_URL = _urlBase + CONTENT_TYPE_PROJECT_LIST;
    // 获取项目下路灯地址
    public static String DEVICE_LAMP_LIST_URL = _urlBase + CONTENT_TYPE_DEVICE_LAMP_LIST;
    // 获取当前项目下的所有设备信息
    public static String  DEVICE_LIST_URL =_urlBase + CONTENT_TYPE_DEVICE_LIST;
    // 设备控制地址
    public static String  DEVICE_CONTROL_URL =_urlBase + DEVICE_CONTROL;
    // 清除报警地址
    public static String  CLEAN_ALARM_URL =_urlBase + CLEAN_ALARM;
    // 历史记录地址
    public static String  HISTORY_METRICS_URL =_urlBase + HISTORY_METRICS;


}
