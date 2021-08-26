package com.example.ld_street_lights_maintenance.entity;

public class LampData {


    /**
     * errno : 0
     * errmsg :
     * data : {"UUID":"83140000351818119616977","LAT":"","LNG":"","NAME":"83140000351818119616977","TYPE":2,"PROJECT":"乌鲁木齐工程","SUBGROUP":"灯头一体控制器","_id":83611,"FUUID":"","info":"{}","config":null,"report_config":null,"ts_created":"2021-08-07T03:10:30.000Z","ts_updated":"2021-08-07T03:10:30.000Z","_countBad":0,"_countBadCurrent":0,"_countBadLeak":0,"_countBadOff":0,"_countBadOn":0,"_countBadTemp":0,"_countBadVoltage":0,"_countMessage":0,"_countOffline":0,"_countOn":0,"_lastStatsDate":"2021-08-24T16:00:00.000Z","energy":0,"STATE":1,"Current":0.02,"Energy":0,"Gprs_csq":16,"Illu":37,"Leak_curt":0,"Power":1.3,"Power_Factor":0,"Temp":25.87,"Voltage":232.62,"ts_mqtt":1629974773789,"Dimming":70,"FirDimming":70,"SecDimming":70,"Fif_tp_Fir":80,"Fif_tt_Fir":"4:0","Fir_tp_Fir":100,"Fir_tt_Fir":"17:0","Four_tp_Fir":40,"Four_tt_Fir":"1:0","Sec_tp_Fir":80,"Sec_tt_Fir":"22:30","Six_tp_Fir":0,"Six_tt_Fir":"9:0","Thir_tp_Fir":30,"Thir_tt_Fir":"23:0","Fif_tp_Sec":80,"Fif_tt_Sec":"4:0","Fir_tp_Sec":100,"Fir_tt_Sec":"17:0","Four_tp_Sec":40,"Four_tt_Sec":"1:0","Sec_tp_Sec":80,"Sec_tt_Sec":"22:30","Six_tp_Sec":0,"Six_tt_Sec":"9:0","Thir_tp_Sec":30,"Thir_tt_Sec":"23:0","ELE_Warning_type":0,"Warning_state":0,"RESET_COUNT":5,"Version":"LD_SCT_NB24G_1-6-3|0-0-0","GPS_CLOSETIME":"7:13","GPS_OPENTIME":"21:8","SIM_CCID":"898604863021D0171338","Time":"2021-8-26-17:0:5"}
     */

    private int errno;
    private String errmsg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * UUID : 83140000351818119616977
         * LAT :
         * LNG :
         * NAME : 83140000351818119616977
         * TYPE : 2
         * PROJECT : 乌鲁木齐工程
         * SUBGROUP : 灯头一体控制器
         * _id : 83611
         * FUUID :
         * info : {}
         * config : null
         * report_config : null
         * ts_created : 2021-08-07T03:10:30.000Z
         * ts_updated : 2021-08-07T03:10:30.000Z
         * _countBad : 0
         * _countBadCurrent : 0
         * _countBadLeak : 0
         * _countBadOff : 0
         * _countBadOn : 0
         * _countBadTemp : 0
         * _countBadVoltage : 0
         * _countMessage : 0
         * _countOffline : 0
         * _countOn : 0
         * _lastStatsDate : 2021-08-24T16:00:00.000Z
         * energy : 0
         * STATE : 1
         * Current : 0.02
         * Energy : 0
         * Gprs_csq : 16
         * Illu : 37
         * Leak_curt : 0
         * Power : 1.3
         * Power_Factor : 0
         * Temp : 25.87
         * Voltage : 232.62
         * ts_mqtt : 1629974773789
         * Dimming : 70
         * FirDimming : 70
         * SecDimming : 70
         * Fif_tp_Fir : 80
         * Fif_tt_Fir : 4:0
         * Fir_tp_Fir : 100
         * Fir_tt_Fir : 17:0
         * Four_tp_Fir : 40
         * Four_tt_Fir : 1:0
         * Sec_tp_Fir : 80
         * Sec_tt_Fir : 22:30
         * Six_tp_Fir : 0
         * Six_tt_Fir : 9:0
         * Thir_tp_Fir : 30
         * Thir_tt_Fir : 23:0
         * Fif_tp_Sec : 80
         * Fif_tt_Sec : 4:0
         * Fir_tp_Sec : 100
         * Fir_tt_Sec : 17:0
         * Four_tp_Sec : 40
         * Four_tt_Sec : 1:0
         * Sec_tp_Sec : 80
         * Sec_tt_Sec : 22:30
         * Six_tp_Sec : 0
         * Six_tt_Sec : 9:0
         * Thir_tp_Sec : 30
         * Thir_tt_Sec : 23:0
         * ELE_Warning_type : 0
         * Warning_state : 0
         * RESET_COUNT : 5
         * Version : LD_SCT_NB24G_1-6-3|0-0-0
         * GPS_CLOSETIME : 7:13
         * GPS_OPENTIME : 21:8
         * SIM_CCID : 898604863021D0171338
         * Time : 2021-8-26-17:0:5
         */

        private String UUID;
        private String LAT;
        private String LNG;
        private String NAME;
        private int TYPE;
        private String PROJECT;
        private String SUBGROUP;
        private int _id;
        private String FUUID;
        private String info;
        private Object config;
        private Object report_config;
        private String ts_created;
        private String ts_updated;
        private int _countBad;
        private int _countBadCurrent;
        private int _countBadLeak;
        private int _countBadOff;
        private int _countBadOn;
        private int _countBadTemp;
        private int _countBadVoltage;
        private int _countMessage;
        private int _countOffline;
        private int _countOn;
        private String _lastStatsDate;
        private int energy;
        private int STATE;
        private double Current;
        private int Energy;
        private int Gprs_csq;
        private int Illu;
        private int Leak_curt;
        private double Power;
        private int Power_Factor;
        private double Temp;
        private double Voltage;
        private long ts_mqtt;
        private int Dimming;
        private int FirDimming;
        private int SecDimming;
        private int Fif_tp_Fir;
        private String Fif_tt_Fir;
        private int Fir_tp_Fir;
        private String Fir_tt_Fir;
        private int Four_tp_Fir;
        private String Four_tt_Fir;
        private int Sec_tp_Fir;
        private String Sec_tt_Fir;
        private int Six_tp_Fir;
        private String Six_tt_Fir;
        private int Thir_tp_Fir;
        private String Thir_tt_Fir;
        private int Fif_tp_Sec;
        private String Fif_tt_Sec;
        private int Fir_tp_Sec;
        private String Fir_tt_Sec;
        private int Four_tp_Sec;
        private String Four_tt_Sec;
        private int Sec_tp_Sec;
        private String Sec_tt_Sec;
        private int Six_tp_Sec;
        private String Six_tt_Sec;
        private int Thir_tp_Sec;
        private String Thir_tt_Sec;
        private int ELE_Warning_type;
        private int Warning_state;
        private int RESET_COUNT;
        private String Version;
        private String GPS_CLOSETIME;
        private String GPS_OPENTIME;
        private String SIM_CCID;
        private String Time;

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

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
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

        public String getTs_created() {
            return ts_created;
        }

        public void setTs_created(String ts_created) {
            this.ts_created = ts_created;
        }

        public String getTs_updated() {
            return ts_updated;
        }

        public void setTs_updated(String ts_updated) {
            this.ts_updated = ts_updated;
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

        public int get_countBadOff() {
            return _countBadOff;
        }

        public void set_countBadOff(int _countBadOff) {
            this._countBadOff = _countBadOff;
        }

        public int get_countBadOn() {
            return _countBadOn;
        }

        public void set_countBadOn(int _countBadOn) {
            this._countBadOn = _countBadOn;
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

        public int get_countOffline() {
            return _countOffline;
        }

        public void set_countOffline(int _countOffline) {
            this._countOffline = _countOffline;
        }

        public int get_countOn() {
            return _countOn;
        }

        public void set_countOn(int _countOn) {
            this._countOn = _countOn;
        }

        public String get_lastStatsDate() {
            return _lastStatsDate;
        }

        public void set_lastStatsDate(String _lastStatsDate) {
            this._lastStatsDate = _lastStatsDate;
        }


        public int getSTATE() {
            return STATE;
        }

        public void setSTATE(int STATE) {
            this.STATE = STATE;
        }

        public double getCurrent() {
            return Current;
        }

        public void setCurrent(double Current) {
            this.Current = Current;
        }

        public int getEnergy() {
            return Energy;
        }

        public void setEnergy(int Energy) {
            this.Energy = Energy;
        }

        public int getGprs_csq() {
            return Gprs_csq;
        }

        public void setGprs_csq(int Gprs_csq) {
            this.Gprs_csq = Gprs_csq;
        }

        public int getIllu() {
            return Illu;
        }

        public void setIllu(int Illu) {
            this.Illu = Illu;
        }

        public int getLeak_curt() {
            return Leak_curt;
        }

        public void setLeak_curt(int Leak_curt) {
            this.Leak_curt = Leak_curt;
        }

        public double getPower() {
            return Power;
        }

        public void setPower(double Power) {
            this.Power = Power;
        }

        public int getPower_Factor() {
            return Power_Factor;
        }

        public void setPower_Factor(int Power_Factor) {
            this.Power_Factor = Power_Factor;
        }

        public double getTemp() {
            return Temp;
        }

        public void setTemp(double Temp) {
            this.Temp = Temp;
        }

        public double getVoltage() {
            return Voltage;
        }

        public void setVoltage(double Voltage) {
            this.Voltage = Voltage;
        }

        public long getTs_mqtt() {
            return ts_mqtt;
        }

        public void setTs_mqtt(long ts_mqtt) {
            this.ts_mqtt = ts_mqtt;
        }

        public int getDimming() {
            return Dimming;
        }

        public void setDimming(int Dimming) {
            this.Dimming = Dimming;
        }

        public int getFirDimming() {
            return FirDimming;
        }

        public void setFirDimming(int FirDimming) {
            this.FirDimming = FirDimming;
        }

        public int getSecDimming() {
            return SecDimming;
        }

        public void setSecDimming(int SecDimming) {
            this.SecDimming = SecDimming;
        }

        public int getFif_tp_Fir() {
            return Fif_tp_Fir;
        }

        public void setFif_tp_Fir(int Fif_tp_Fir) {
            this.Fif_tp_Fir = Fif_tp_Fir;
        }

        public String getFif_tt_Fir() {
            return Fif_tt_Fir;
        }

        public void setFif_tt_Fir(String Fif_tt_Fir) {
            this.Fif_tt_Fir = Fif_tt_Fir;
        }

        public int getFir_tp_Fir() {
            return Fir_tp_Fir;
        }

        public void setFir_tp_Fir(int Fir_tp_Fir) {
            this.Fir_tp_Fir = Fir_tp_Fir;
        }

        public String getFir_tt_Fir() {
            return Fir_tt_Fir;
        }

        public void setFir_tt_Fir(String Fir_tt_Fir) {
            this.Fir_tt_Fir = Fir_tt_Fir;
        }

        public int getFour_tp_Fir() {
            return Four_tp_Fir;
        }

        public void setFour_tp_Fir(int Four_tp_Fir) {
            this.Four_tp_Fir = Four_tp_Fir;
        }

        public String getFour_tt_Fir() {
            return Four_tt_Fir;
        }

        public void setFour_tt_Fir(String Four_tt_Fir) {
            this.Four_tt_Fir = Four_tt_Fir;
        }

        public int getSec_tp_Fir() {
            return Sec_tp_Fir;
        }

        public void setSec_tp_Fir(int Sec_tp_Fir) {
            this.Sec_tp_Fir = Sec_tp_Fir;
        }

        public String getSec_tt_Fir() {
            return Sec_tt_Fir;
        }

        public void setSec_tt_Fir(String Sec_tt_Fir) {
            this.Sec_tt_Fir = Sec_tt_Fir;
        }

        public int getSix_tp_Fir() {
            return Six_tp_Fir;
        }

        public void setSix_tp_Fir(int Six_tp_Fir) {
            this.Six_tp_Fir = Six_tp_Fir;
        }

        public String getSix_tt_Fir() {
            return Six_tt_Fir;
        }

        public void setSix_tt_Fir(String Six_tt_Fir) {
            this.Six_tt_Fir = Six_tt_Fir;
        }

        public int getThir_tp_Fir() {
            return Thir_tp_Fir;
        }

        public void setThir_tp_Fir(int Thir_tp_Fir) {
            this.Thir_tp_Fir = Thir_tp_Fir;
        }

        public String getThir_tt_Fir() {
            return Thir_tt_Fir;
        }

        public void setThir_tt_Fir(String Thir_tt_Fir) {
            this.Thir_tt_Fir = Thir_tt_Fir;
        }

        public int getFif_tp_Sec() {
            return Fif_tp_Sec;
        }

        public void setFif_tp_Sec(int Fif_tp_Sec) {
            this.Fif_tp_Sec = Fif_tp_Sec;
        }

        public String getFif_tt_Sec() {
            return Fif_tt_Sec;
        }

        public void setFif_tt_Sec(String Fif_tt_Sec) {
            this.Fif_tt_Sec = Fif_tt_Sec;
        }

        public int getFir_tp_Sec() {
            return Fir_tp_Sec;
        }

        public void setFir_tp_Sec(int Fir_tp_Sec) {
            this.Fir_tp_Sec = Fir_tp_Sec;
        }

        public String getFir_tt_Sec() {
            return Fir_tt_Sec;
        }

        public void setFir_tt_Sec(String Fir_tt_Sec) {
            this.Fir_tt_Sec = Fir_tt_Sec;
        }

        public int getFour_tp_Sec() {
            return Four_tp_Sec;
        }

        public void setFour_tp_Sec(int Four_tp_Sec) {
            this.Four_tp_Sec = Four_tp_Sec;
        }

        public String getFour_tt_Sec() {
            return Four_tt_Sec;
        }

        public void setFour_tt_Sec(String Four_tt_Sec) {
            this.Four_tt_Sec = Four_tt_Sec;
        }

        public int getSec_tp_Sec() {
            return Sec_tp_Sec;
        }

        public void setSec_tp_Sec(int Sec_tp_Sec) {
            this.Sec_tp_Sec = Sec_tp_Sec;
        }

        public String getSec_tt_Sec() {
            return Sec_tt_Sec;
        }

        public void setSec_tt_Sec(String Sec_tt_Sec) {
            this.Sec_tt_Sec = Sec_tt_Sec;
        }

        public int getSix_tp_Sec() {
            return Six_tp_Sec;
        }

        public void setSix_tp_Sec(int Six_tp_Sec) {
            this.Six_tp_Sec = Six_tp_Sec;
        }

        public String getSix_tt_Sec() {
            return Six_tt_Sec;
        }

        public void setSix_tt_Sec(String Six_tt_Sec) {
            this.Six_tt_Sec = Six_tt_Sec;
        }

        public int getThir_tp_Sec() {
            return Thir_tp_Sec;
        }

        public void setThir_tp_Sec(int Thir_tp_Sec) {
            this.Thir_tp_Sec = Thir_tp_Sec;
        }

        public String getThir_tt_Sec() {
            return Thir_tt_Sec;
        }

        public void setThir_tt_Sec(String Thir_tt_Sec) {
            this.Thir_tt_Sec = Thir_tt_Sec;
        }

        public int getELE_Warning_type() {
            return ELE_Warning_type;
        }

        public void setELE_Warning_type(int ELE_Warning_type) {
            this.ELE_Warning_type = ELE_Warning_type;
        }

        public int getWarning_state() {
            return Warning_state;
        }

        public void setWarning_state(int Warning_state) {
            this.Warning_state = Warning_state;
        }

        public int getRESET_COUNT() {
            return RESET_COUNT;
        }

        public void setRESET_COUNT(int RESET_COUNT) {
            this.RESET_COUNT = RESET_COUNT;
        }

        public String getVersion() {
            return Version;
        }

        public void setVersion(String Version) {
            this.Version = Version;
        }

        public String getGPS_CLOSETIME() {
            return GPS_CLOSETIME;
        }

        public void setGPS_CLOSETIME(String GPS_CLOSETIME) {
            this.GPS_CLOSETIME = GPS_CLOSETIME;
        }

        public String getGPS_OPENTIME() {
            return GPS_OPENTIME;
        }

        public void setGPS_OPENTIME(String GPS_OPENTIME) {
            this.GPS_OPENTIME = GPS_OPENTIME;
        }

        public String getSIM_CCID() {
            return SIM_CCID;
        }

        public void setSIM_CCID(String SIM_CCID) {
            this.SIM_CCID = SIM_CCID;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "UUID='" + UUID + '\'' +
                    ", LAT='" + LAT + '\'' +
                    ", LNG='" + LNG + '\'' +
                    ", NAME='" + NAME + '\'' +
                    ", TYPE=" + TYPE +
                    ", PROJECT='" + PROJECT + '\'' +
                    ", SUBGROUP='" + SUBGROUP + '\'' +
                    ", _id=" + _id +
                    ", FUUID='" + FUUID + '\'' +
                    ", info='" + info + '\'' +
                    ", config=" + config +
                    ", report_config=" + report_config +
                    ", ts_created='" + ts_created + '\'' +
                    ", ts_updated='" + ts_updated + '\'' +
                    ", _countBad=" + _countBad +
                    ", _countBadCurrent=" + _countBadCurrent +
                    ", _countBadLeak=" + _countBadLeak +
                    ", _countBadOff=" + _countBadOff +
                    ", _countBadOn=" + _countBadOn +
                    ", _countBadTemp=" + _countBadTemp +
                    ", _countBadVoltage=" + _countBadVoltage +
                    ", _countMessage=" + _countMessage +
                    ", _countOffline=" + _countOffline +
                    ", _countOn=" + _countOn +
                    ", _lastStatsDate='" + _lastStatsDate + '\'' +
                    ", energy=" + energy +
                    ", STATE=" + STATE +
                    ", Current=" + Current +
                    ", Energy=" + Energy +
                    ", Gprs_csq=" + Gprs_csq +
                    ", Illu=" + Illu +
                    ", Leak_curt=" + Leak_curt +
                    ", Power=" + Power +
                    ", Power_Factor=" + Power_Factor +
                    ", Temp=" + Temp +
                    ", Voltage=" + Voltage +
                    ", ts_mqtt=" + ts_mqtt +
                    ", Dimming=" + Dimming +
                    ", FirDimming=" + FirDimming +
                    ", SecDimming=" + SecDimming +
                    ", Fif_tp_Fir=" + Fif_tp_Fir +
                    ", Fif_tt_Fir='" + Fif_tt_Fir + '\'' +
                    ", Fir_tp_Fir=" + Fir_tp_Fir +
                    ", Fir_tt_Fir='" + Fir_tt_Fir + '\'' +
                    ", Four_tp_Fir=" + Four_tp_Fir +
                    ", Four_tt_Fir='" + Four_tt_Fir + '\'' +
                    ", Sec_tp_Fir=" + Sec_tp_Fir +
                    ", Sec_tt_Fir='" + Sec_tt_Fir + '\'' +
                    ", Six_tp_Fir=" + Six_tp_Fir +
                    ", Six_tt_Fir='" + Six_tt_Fir + '\'' +
                    ", Thir_tp_Fir=" + Thir_tp_Fir +
                    ", Thir_tt_Fir='" + Thir_tt_Fir + '\'' +
                    ", Fif_tp_Sec=" + Fif_tp_Sec +
                    ", Fif_tt_Sec='" + Fif_tt_Sec + '\'' +
                    ", Fir_tp_Sec=" + Fir_tp_Sec +
                    ", Fir_tt_Sec='" + Fir_tt_Sec + '\'' +
                    ", Four_tp_Sec=" + Four_tp_Sec +
                    ", Four_tt_Sec='" + Four_tt_Sec + '\'' +
                    ", Sec_tp_Sec=" + Sec_tp_Sec +
                    ", Sec_tt_Sec='" + Sec_tt_Sec + '\'' +
                    ", Six_tp_Sec=" + Six_tp_Sec +
                    ", Six_tt_Sec='" + Six_tt_Sec + '\'' +
                    ", Thir_tp_Sec=" + Thir_tp_Sec +
                    ", Thir_tt_Sec='" + Thir_tt_Sec + '\'' +
                    ", ELE_Warning_type=" + ELE_Warning_type +
                    ", Warning_state=" + Warning_state +
                    ", RESET_COUNT=" + RESET_COUNT +
                    ", Version='" + Version + '\'' +
                    ", GPS_CLOSETIME='" + GPS_CLOSETIME + '\'' +
                    ", GPS_OPENTIME='" + GPS_OPENTIME + '\'' +
                    ", SIM_CCID='" + SIM_CCID + '\'' +
                    ", Time='" + Time + '\'' +
                    '}';
        }
    }


}
