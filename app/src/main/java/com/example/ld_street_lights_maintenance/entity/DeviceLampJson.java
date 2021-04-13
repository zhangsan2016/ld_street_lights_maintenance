package com.example.ld_street_lights_maintenance.entity;

import java.util.List;

public class DeviceLampJson {



    private int errno;
    private String errmsg;
    private List<DataBean> data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * UUID : 000000000000000000000021
         * LAT : 29.803991
         * LNG : 666.58
         * NAME : 单头展示灯
         * TYPE : 2
         * PROJECT : 中科洛丁展示项目/重庆展厅
         * SUBGROUP :
         * ADDR :
         * IPADDR :
         * _id : 47
         * FUUID : 88,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99
         * subgroups : ["无线版控制器"]
         * admin : ld
         * code : 400020
         * members : ["ld"]
         * config : null
         * report_config : null
         * Energy : 10500.52
         * FirDimming : 40
         * Illu : 3678.71
         * STATE : 1
         * Temp : 61.08
         * ts_mqtt : 1618307686060
         * Power : 120.63
         * Warning_state : 0
         * _countBad : 116
         * _countBadCurrent : 0
         * _countBadLeak : 0
         * _countBadTemp : 116
         * _countBadVoltage : 0
         * _countMessage : 391
         * _lastStatsDate : 2021-04-11T16:00:00.000Z
         * __countMessage : 0
         * Fif_tp_Fir : 20
         * Fif_tp_Sec : 20
         * Fif_tt_Fir : 14:30
         * Fif_tt_Sec : 14:30
         * Fir_tp_Fir : 80
         * Fir_tp_Sec : 30
         * Fir_tt_Fir : 12:30
         * Fir_tt_Sec : 12:30
         * Four_tp_Fir : 20
         * Four_tp_Sec : 20
         * Four_tt_Fir : 14:00
         * Four_tt_Sec : 14:00
         * Sec_tp_Fir : 80
         * Sec_tp_Sec : 30
         * Sec_tt_Fir : 13:00
         * Sec_tt_Sec : 13:00
         * Six_tp_Fir : 0
         * Six_tp_Sec : 0
         * Six_tt_Fir : 15:00
         * Six_tt_Sec : 15:00
         * Thir_tp_Fir : 20
         * Thir_tp_Sec : 20
         * Thir_tt_Fir : 13:30
         * Thir_tt_Sec : 13:30
         * Current : 0.52
         * Voltage : 230.48
         * C_downthreshold : 0.01
         * C_upthreshold : 5
         * Leak_c_enable : 1
         * Leak_c_threshold : 20
         * V_downthreshold : 120
         * V_upthreshold : 260
         * _countOn : 1069
         * _countBadOn : 619
         * _countBadOff : 3
         * Leak_cur : 22
         * LampDiameter : 16cm
         * Power_Manufacturer : 英飞特
         * Lamp_RatedCurrent : 1.8A
         * Lamp_Ratedvoltage : 125V
         * lampType : LED
         * Lamp_Manufacturer : 洛丁光电
         * Lamp_Num : 2
         * PoleProductionDate : 2016/3/12
         * Pole_height : 3.6m
         * Rated_power : 120
         * Subcommunicate_mode : 有线载波48V
         * _countOffline : 2
         * info : {}
         * stream_url : rtsp://192.168.2.142:554/stream0?username=admin&password=E10ADC3949BA59ABBE56E057F20F883E
         * stream_user : admin
         * stream_password : 123456
         * road_direction : 高速收费站往云计算中心2#路方向
         * cam_id : C00021
         * onvif_port : 8000
         * manufacturer : luoding
         */

        private String UUID;
        private String LAT;
        private String LNG;
        private String NAME;
        private int TYPE;
        private String PROJECT;
        private String SUBGROUP;
        private String ADDR;
        private String IPADDR;
        private int _id;
        private String FUUID;
        private String subgroups;
        private String admin;
        private String code;
        private String members;
        private Object config;
        private Object report_config;
        private double Energy;
        private double FirDimming;
        private double Illu;
        private int STATE;
        private double Temp;
        private long ts_mqtt;
        private double Power;
        private int Warning_state;
        private int _countBad;
        private int _countBadCurrent;
        private int _countBadLeak;
        private int _countBadTemp;
        private int _countBadVoltage;
        private int _countMessage;
        private String _lastStatsDate;
        private int __countMessage;
        private int Fif_tp_Fir;
        private int Fif_tp_Sec;
        private String Fif_tt_Fir;
        private String Fif_tt_Sec;
        private int Fir_tp_Fir;
        private int Fir_tp_Sec;
        private String Fir_tt_Fir;
        private String Fir_tt_Sec;
        private int Four_tp_Fir;
        private int Four_tp_Sec;
        private String Four_tt_Fir;
        private String Four_tt_Sec;
        private int Sec_tp_Fir;
        private int Sec_tp_Sec;
        private String Sec_tt_Fir;
        private String Sec_tt_Sec;
        private int Six_tp_Fir;
        private int Six_tp_Sec;
        private String Six_tt_Fir;
        private String Six_tt_Sec;
        private int Thir_tp_Fir;
        private int Thir_tp_Sec;
        private String Thir_tt_Fir;
        private String Thir_tt_Sec;
        private double Current;
        private double Voltage;
        private double C_downthreshold;
        private int C_upthreshold;
        private int Leak_c_enable;
        private int Leak_c_threshold;
        private int V_downthreshold;
        private int V_upthreshold;
        private int _countOn;
        private int _countBadOn;
        private int _countBadOff;
        private double Leak_cur;
        private String LampDiameter;
        private String Power_Manufacturer;
        private String Lamp_RatedCurrent;
        private String Lamp_Ratedvoltage;
        private String lampType;
        private String Lamp_Manufacturer;
        private String Lamp_Num;
        private String PoleProductionDate;
        private String Pole_height;
        private String Rated_power;
        private String Subcommunicate_mode;
        private int _countOffline;
        private String info;
        private String stream_url;
        private String stream_user;
        private String stream_password;
        private String road_direction;
        private String cam_id;
        private String onvif_port;
        private String manufacturer;

        public String getUUID() {
            return UUID;
        }

        public void setUUID(String UUID) {
            this.UUID = UUID;
        }

        public String getLAT() {
            return LAT;
        }

        public void setLAT(String LAT) {
            this.LAT = LAT;
        }

        public String getLNG() {
            return LNG;
        }

        public void setLNG(String LNG) {
            this.LNG = LNG;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public int getTYPE() {
            return TYPE;
        }

        public void setTYPE(int TYPE) {
            this.TYPE = TYPE;
        }

        public String getPROJECT() {
            return PROJECT;
        }

        public void setPROJECT(String PROJECT) {
            this.PROJECT = PROJECT;
        }

        public String getSUBGROUP() {
            return SUBGROUP;
        }

        public void setSUBGROUP(String SUBGROUP) {
            this.SUBGROUP = SUBGROUP;
        }

        public String getADDR() {
            return ADDR;
        }

        public void setADDR(String ADDR) {
            this.ADDR = ADDR;
        }

        public String getIPADDR() {
            return IPADDR;
        }

        public void setIPADDR(String IPADDR) {
            this.IPADDR = IPADDR;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getFUUID() {
            return FUUID;
        }

        public void setFUUID(String FUUID) {
            this.FUUID = FUUID;
        }

        public String getSubgroups() {
            return subgroups;
        }

        public void setSubgroups(String subgroups) {
            this.subgroups = subgroups;
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMembers() {
            return members;
        }

        public void setMembers(String members) {
            this.members = members;
        }

        public Object getConfig() {
            return config;
        }

        public void setConfig(Object config) {
            this.config = config;
        }

        public Object getReport_config() {
            return report_config;
        }

        public void setReport_config(Object report_config) {
            this.report_config = report_config;
        }

        public double getEnergy() {
            return Energy;
        }

        public void setEnergy(double Energy) {
            this.Energy = Energy;
        }

        public double getFirDimming() {
            return FirDimming;
        }

        public void setFirDimming(int FirDimming) {
            this.FirDimming = FirDimming;
        }

        public double getIllu() {
            return Illu;
        }

        public void setIllu(double Illu) {
            this.Illu = Illu;
        }

        public int getSTATE() {
            return STATE;
        }

        public void setSTATE(int STATE) {
            this.STATE = STATE;
        }

        public double getTemp() {
            return Temp;
        }

        public void setTemp(double Temp) {
            this.Temp = Temp;
        }

        public long getTs_mqtt() {
            return ts_mqtt;
        }

        public void setTs_mqtt(long ts_mqtt) {
            this.ts_mqtt = ts_mqtt;
        }

        public double getPower() {
            return Power;
        }

        public void setPower(double Power) {
            this.Power = Power;
        }

        public int getWarning_state() {
            return Warning_state;
        }

        public void setWarning_state(int Warning_state) {
            this.Warning_state = Warning_state;
        }

        public int get_countBad() {
            return _countBad;
        }

        public void set_countBad(int _countBad) {
            this._countBad = _countBad;
        }

        public int get_countBadCurrent() {
            return _countBadCurrent;
        }

        public void set_countBadCurrent(int _countBadCurrent) {
            this._countBadCurrent = _countBadCurrent;
        }

        public int get_countBadLeak() {
            return _countBadLeak;
        }

        public void set_countBadLeak(int _countBadLeak) {
            this._countBadLeak = _countBadLeak;
        }

        public int get_countBadTemp() {
            return _countBadTemp;
        }

        public void set_countBadTemp(int _countBadTemp) {
            this._countBadTemp = _countBadTemp;
        }

        public int get_countBadVoltage() {
            return _countBadVoltage;
        }

        public void set_countBadVoltage(int _countBadVoltage) {
            this._countBadVoltage = _countBadVoltage;
        }

        public int get_countMessage() {
            return _countMessage;
        }

        public void set_countMessage(int _countMessage) {
            this._countMessage = _countMessage;
        }

        public String get_lastStatsDate() {
            return _lastStatsDate;
        }

        public void set_lastStatsDate(String _lastStatsDate) {
            this._lastStatsDate = _lastStatsDate;
        }

        public int get__countMessage() {
            return __countMessage;
        }

        public void set__countMessage(int __countMessage) {
            this.__countMessage = __countMessage;
        }

        public int getFif_tp_Fir() {
            return Fif_tp_Fir;
        }

        public void setFif_tp_Fir(int Fif_tp_Fir) {
            this.Fif_tp_Fir = Fif_tp_Fir;
        }

        public int getFif_tp_Sec() {
            return Fif_tp_Sec;
        }

        public void setFif_tp_Sec(int Fif_tp_Sec) {
            this.Fif_tp_Sec = Fif_tp_Sec;
        }

        public String getFif_tt_Fir() {
            return Fif_tt_Fir;
        }

        public void setFif_tt_Fir(String Fif_tt_Fir) {
            this.Fif_tt_Fir = Fif_tt_Fir;
        }

        public String getFif_tt_Sec() {
            return Fif_tt_Sec;
        }

        public void setFif_tt_Sec(String Fif_tt_Sec) {
            this.Fif_tt_Sec = Fif_tt_Sec;
        }

        public int getFir_tp_Fir() {
            return Fir_tp_Fir;
        }

        public void setFir_tp_Fir(int Fir_tp_Fir) {
            this.Fir_tp_Fir = Fir_tp_Fir;
        }

        public int getFir_tp_Sec() {
            return Fir_tp_Sec;
        }

        public void setFir_tp_Sec(int Fir_tp_Sec) {
            this.Fir_tp_Sec = Fir_tp_Sec;
        }

        public String getFir_tt_Fir() {
            return Fir_tt_Fir;
        }

        public void setFir_tt_Fir(String Fir_tt_Fir) {
            this.Fir_tt_Fir = Fir_tt_Fir;
        }

        public String getFir_tt_Sec() {
            return Fir_tt_Sec;
        }

        public void setFir_tt_Sec(String Fir_tt_Sec) {
            this.Fir_tt_Sec = Fir_tt_Sec;
        }

        public int getFour_tp_Fir() {
            return Four_tp_Fir;
        }

        public void setFour_tp_Fir(int Four_tp_Fir) {
            this.Four_tp_Fir = Four_tp_Fir;
        }

        public int getFour_tp_Sec() {
            return Four_tp_Sec;
        }

        public void setFour_tp_Sec(int Four_tp_Sec) {
            this.Four_tp_Sec = Four_tp_Sec;
        }

        public String getFour_tt_Fir() {
            return Four_tt_Fir;
        }

        public void setFour_tt_Fir(String Four_tt_Fir) {
            this.Four_tt_Fir = Four_tt_Fir;
        }

        public String getFour_tt_Sec() {
            return Four_tt_Sec;
        }

        public void setFour_tt_Sec(String Four_tt_Sec) {
            this.Four_tt_Sec = Four_tt_Sec;
        }

        public int getSec_tp_Fir() {
            return Sec_tp_Fir;
        }

        public void setSec_tp_Fir(int Sec_tp_Fir) {
            this.Sec_tp_Fir = Sec_tp_Fir;
        }

        public int getSec_tp_Sec() {
            return Sec_tp_Sec;
        }

        public void setSec_tp_Sec(int Sec_tp_Sec) {
            this.Sec_tp_Sec = Sec_tp_Sec;
        }

        public String getSec_tt_Fir() {
            return Sec_tt_Fir;
        }

        public void setSec_tt_Fir(String Sec_tt_Fir) {
            this.Sec_tt_Fir = Sec_tt_Fir;
        }

        public String getSec_tt_Sec() {
            return Sec_tt_Sec;
        }

        public void setSec_tt_Sec(String Sec_tt_Sec) {
            this.Sec_tt_Sec = Sec_tt_Sec;
        }

        public int getSix_tp_Fir() {
            return Six_tp_Fir;
        }

        public void setSix_tp_Fir(int Six_tp_Fir) {
            this.Six_tp_Fir = Six_tp_Fir;
        }

        public int getSix_tp_Sec() {
            return Six_tp_Sec;
        }

        public void setSix_tp_Sec(int Six_tp_Sec) {
            this.Six_tp_Sec = Six_tp_Sec;
        }

        public String getSix_tt_Fir() {
            return Six_tt_Fir;
        }

        public void setSix_tt_Fir(String Six_tt_Fir) {
            this.Six_tt_Fir = Six_tt_Fir;
        }

        public String getSix_tt_Sec() {
            return Six_tt_Sec;
        }

        public void setSix_tt_Sec(String Six_tt_Sec) {
            this.Six_tt_Sec = Six_tt_Sec;
        }

        public int getThir_tp_Fir() {
            return Thir_tp_Fir;
        }

        public void setThir_tp_Fir(int Thir_tp_Fir) {
            this.Thir_tp_Fir = Thir_tp_Fir;
        }

        public int getThir_tp_Sec() {
            return Thir_tp_Sec;
        }

        public void setThir_tp_Sec(int Thir_tp_Sec) {
            this.Thir_tp_Sec = Thir_tp_Sec;
        }

        public String getThir_tt_Fir() {
            return Thir_tt_Fir;
        }

        public void setThir_tt_Fir(String Thir_tt_Fir) {
            this.Thir_tt_Fir = Thir_tt_Fir;
        }

        public String getThir_tt_Sec() {
            return Thir_tt_Sec;
        }

        public void setThir_tt_Sec(String Thir_tt_Sec) {
            this.Thir_tt_Sec = Thir_tt_Sec;
        }

        public double getCurrent() {
            return Current;
        }

        public void setCurrent(double Current) {
            this.Current = Current;
        }

        public double getVoltage() {
            return Voltage;
        }

        public void setVoltage(double Voltage) {
            this.Voltage = Voltage;
        }

        public double getC_downthreshold() {
            return C_downthreshold;
        }

        public void setC_downthreshold(double C_downthreshold) {
            this.C_downthreshold = C_downthreshold;
        }

        public int getC_upthreshold() {
            return C_upthreshold;
        }

        public void setC_upthreshold(int C_upthreshold) {
            this.C_upthreshold = C_upthreshold;
        }

        public int getLeak_c_enable() {
            return Leak_c_enable;
        }

        public void setLeak_c_enable(int Leak_c_enable) {
            this.Leak_c_enable = Leak_c_enable;
        }

        public int getLeak_c_threshold() {
            return Leak_c_threshold;
        }

        public void setLeak_c_threshold(int Leak_c_threshold) {
            this.Leak_c_threshold = Leak_c_threshold;
        }

        public int getV_downthreshold() {
            return V_downthreshold;
        }

        public void setV_downthreshold(int V_downthreshold) {
            this.V_downthreshold = V_downthreshold;
        }

        public int getV_upthreshold() {
            return V_upthreshold;
        }

        public void setV_upthreshold(int V_upthreshold) {
            this.V_upthreshold = V_upthreshold;
        }

        public int get_countOn() {
            return _countOn;
        }

        public void set_countOn(int _countOn) {
            this._countOn = _countOn;
        }

        public int get_countBadOn() {
            return _countBadOn;
        }

        public void set_countBadOn(int _countBadOn) {
            this._countBadOn = _countBadOn;
        }

        public int get_countBadOff() {
            return _countBadOff;
        }

        public void set_countBadOff(int _countBadOff) {
            this._countBadOff = _countBadOff;
        }

        public double getLeak_cur() {
            return Leak_cur;
        }

        public void setLeak_cur(double Leak_cur) {
            this.Leak_cur = Leak_cur;
        }

        public String getLampDiameter() {
            return LampDiameter;
        }

        public void setLampDiameter(String LampDiameter) {
            this.LampDiameter = LampDiameter;
        }

        public String getPower_Manufacturer() {
            return Power_Manufacturer;
        }

        public void setPower_Manufacturer(String Power_Manufacturer) {
            this.Power_Manufacturer = Power_Manufacturer;
        }

        public String getLamp_RatedCurrent() {
            return Lamp_RatedCurrent;
        }

        public void setLamp_RatedCurrent(String Lamp_RatedCurrent) {
            this.Lamp_RatedCurrent = Lamp_RatedCurrent;
        }

        public String getLamp_Ratedvoltage() {
            return Lamp_Ratedvoltage;
        }

        public void setLamp_Ratedvoltage(String Lamp_Ratedvoltage) {
            this.Lamp_Ratedvoltage = Lamp_Ratedvoltage;
        }

        public String getLampType() {
            return lampType;
        }

        public void setLampType(String lampType) {
            this.lampType = lampType;
        }

        public String getLamp_Manufacturer() {
            return Lamp_Manufacturer;
        }

        public void setLamp_Manufacturer(String Lamp_Manufacturer) {
            this.Lamp_Manufacturer = Lamp_Manufacturer;
        }

        public String getLamp_Num() {
            return Lamp_Num;
        }

        public void setLamp_Num(String Lamp_Num) {
            this.Lamp_Num = Lamp_Num;
        }

        public String getPoleProductionDate() {
            return PoleProductionDate;
        }

        public void setPoleProductionDate(String PoleProductionDate) {
            this.PoleProductionDate = PoleProductionDate;
        }

        public String getPole_height() {
            return Pole_height;
        }

        public void setPole_height(String Pole_height) {
            this.Pole_height = Pole_height;
        }

        public String getRated_power() {
            return Rated_power;
        }

        public void setRated_power(String Rated_power) {
            this.Rated_power = Rated_power;
        }

        public String getSubcommunicate_mode() {
            return Subcommunicate_mode;
        }

        public void setSubcommunicate_mode(String Subcommunicate_mode) {
            this.Subcommunicate_mode = Subcommunicate_mode;
        }

        public int get_countOffline() {
            return _countOffline;
        }

        public void set_countOffline(int _countOffline) {
            this._countOffline = _countOffline;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getStream_url() {
            return stream_url;
        }

        public void setStream_url(String stream_url) {
            this.stream_url = stream_url;
        }

        public String getStream_user() {
            return stream_user;
        }

        public void setStream_user(String stream_user) {
            this.stream_user = stream_user;
        }

        public String getStream_password() {
            return stream_password;
        }

        public void setStream_password(String stream_password) {
            this.stream_password = stream_password;
        }

        public String getRoad_direction() {
            return road_direction;
        }

        public void setRoad_direction(String road_direction) {
            this.road_direction = road_direction;
        }

        public String getCam_id() {
            return cam_id;
        }

        public void setCam_id(String cam_id) {
            this.cam_id = cam_id;
        }

        public String getOnvif_port() {
            return onvif_port;
        }

        public void setOnvif_port(String onvif_port) {
            this.onvif_port = onvif_port;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }
    }
}
