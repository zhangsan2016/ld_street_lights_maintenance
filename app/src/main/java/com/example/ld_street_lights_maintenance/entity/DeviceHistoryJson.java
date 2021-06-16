package com.example.ld_street_lights_maintenance.entity;

import java.util.List;

public class DeviceHistoryJson {


    /**
     * errno : 0
     * errmsg :
     * data : [{"_id":"60c878df8ea1ed202c62007f","data":{"FirDimming":0,"Energy":64889.97,"Voltage":202.35,"Power":0,"Current":0,"Temp":61.16,"Illu":3057.54,"Warning_state":0,"ts_mqtt":1623750879548,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:54:39.550Z"},{"_id":"60c8786d8ea1ed202c62000e","data":{"FirDimming":0,"Energy":64888.82,"Voltage":218.01,"Power":0,"Current":0,"Temp":60.25,"Illu":3605.25,"Warning_state":0,"ts_mqtt":1623750764454,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:52:45.453Z"},{"_id":"60c878398ea1ed202c61ffdb","data":{"FirDimming":0,"Energy":64888.3,"Voltage":239.02,"Power":0,"Current":0,"Temp":67.99,"Illu":3377.94,"Warning_state":0,"ts_mqtt":1623750712415,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:51:53.407Z"},{"_id":"60c878128ea1ed202c61ffb5","data":{"FirDimming":0,"Energy":64887.92,"Voltage":224.36,"Power":0,"Current":0,"Temp":67.14,"Illu":3329.37,"Warning_state":0,"ts_mqtt":1623750674383,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:51:14.383Z"},{"_id":"60c877ff8ea1ed202c61ffa2","data":{"FirDimming":0,"Energy":64887.73,"Voltage":232.84,"Power":0,"Current":0,"Temp":64.09,"Illu":3189.63,"Warning_state":0,"ts_mqtt":1623750655374,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:50:55.375Z"},{"_id":"60c877c78ea1ed202c61ff69","data":{"FirDimming":0,"Energy":64887.16,"Voltage":232.61,"Power":0,"Current":0,"Temp":62.08,"Illu":3825.23,"Warning_state":0,"ts_mqtt":1623750598335,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:49:59.335Z"},{"_id":"60c877888ea1ed202c61ff2a","data":{"FirDimming":0,"Energy":64886.53,"Voltage":234.45,"Power":0,"Current":0,"Temp":72.21,"Illu":3328.66,"Warning_state":0,"ts_mqtt":1623750535305,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:48:56.301Z"},{"_id":"60c8773d8ea1ed202c61fee0","data":{"FirDimming":0,"Energy":64885.78,"Voltage":199.21,"Power":0,"Current":0,"Temp":73.51,"Illu":3047.11,"Warning_state":0,"ts_mqtt":1623750460253,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:47:41.239Z"},{"_id":"60c877038ea1ed202c61fea7","data":{"FirDimming":0,"Energy":64885.2,"Voltage":223.01,"Power":0,"Current":0,"Temp":70.69,"Illu":3021.04,"Warning_state":0,"ts_mqtt":1623750402191,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:46:43.191Z"},{"_id":"60c876af8ea1ed202c61fe53","data":{"FirDimming":0,"Energy":64884.36,"Voltage":223.3,"Power":0,"Current":0,"Temp":72.74,"Illu":3725.2,"Warning_state":0,"ts_mqtt":1623750318122,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:45:19.116Z"},{"_id":"60c876588ea1ed202c61fdfe","data":{"FirDimming":0,"Energy":64883.49,"Voltage":205.27,"Power":0,"Current":0,"Temp":71.22,"Illu":3030.14,"Warning_state":0,"ts_mqtt":1623750231059,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:43:52.060Z"},{"_id":"60c8756e8ea1ed202c61fd1c","data":{"FirDimming":0,"Energy":64881.16,"Voltage":214.12,"Power":0,"Current":0,"Temp":66.64,"Illu":3656.38,"Warning_state":0,"ts_mqtt":1623749997906,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:39:58.879Z"},{"_id":"60c8756d8ea1ed202c61fd1b","data":{"FirDimming":0,"Energy":64881.15,"Voltage":234.87,"Power":0,"Current":0,"Temp":74.22,"Illu":3923.26,"Warning_state":0,"ts_mqtt":1623749996905,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:39:57.878Z"},{"_id":"60c8752f8ea1ed202c61fcdd","data":{"FirDimming":0,"Energy":64880.53,"Voltage":214.32,"Power":0,"Current":0,"Temp":71.45,"Illu":3161.24,"Warning_state":0,"ts_mqtt":1623749934862,"STATE":1},"code":"518100","type":2,"createdAt":"2021-06-15T09:38:55.808Z"}]
     */

    private int errno;
    private String errmsg;
    private List<DataBeanX> data;

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

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * _id : 60c878df8ea1ed202c62007f
         * data : {"FirDimming":0,"Energy":64889.97,"Voltage":202.35,"Power":0,"Current":0,"Temp":61.16,"Illu":3057.54,"Warning_state":0,"ts_mqtt":1623750879548,"STATE":1}
         * code : 518100
         * type : 2
         * createdAt : 2021-06-15T09:54:39.550Z
         */

        private String _id;
        private DataBean data;
        private String code;
        private int type;
        private String createdAt;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public static class DataBean {
            /**
             * FirDimming : 0
             * Energy : 64889.97
             * Voltage : 202.35
             * Power : 0
             * Current : 0
             * Temp : 61.16
             * Illu : 3057.54
             * Warning_state : 0
             * ts_mqtt : 1623750879548
             * STATE : 1
             */

            private int FirDimming;
            private double Energy;
            private double Voltage;
            private String Power;
            private double Current;
            private double Temp;
            private double Illu;
            private int Warning_state;
            private long ts_mqtt;
            private int STATE;

            public int getFirDimming() {
                return FirDimming;
            }

            public void setFirDimming(int FirDimming) {
                this.FirDimming = FirDimming;
            }

            public double getEnergy() {
                return Energy;
            }

            public void setEnergy(double Energy) {
                this.Energy = Energy;
            }

            public double getVoltage() {
                return Voltage;
            }

            public void setVoltage(double Voltage) {
                this.Voltage = Voltage;
            }

            public String getPower() {
                return Power;
            }

            public void setPower(String Power) {
                this.Power = Power;
            }

            public double getCurrent() {
                return Current;
            }

            public void setCurrent(double Current) {
                this.Current = Current;
            }

            public double getTemp() {
                return Temp;
            }

            public void setTemp(double Temp) {
                this.Temp = Temp;
            }

            public double getIllu() {
                return Illu;
            }

            public void setIllu(double Illu) {
                this.Illu = Illu;
            }

            public int getWarning_state() {
                return Warning_state;
            }

            public void setWarning_state(int Warning_state) {
                this.Warning_state = Warning_state;
            }

            public long getTs_mqtt() {
                return ts_mqtt;
            }

            public void setTs_mqtt(long ts_mqtt) {
                this.ts_mqtt = ts_mqtt;
            }

            public int getSTATE() {
                return STATE;
            }

            public void setSTATE(int STATE) {
                this.STATE = STATE;
            }
        }

        @Override
        public String toString() {
            return "{" +
                    "_id='" + _id + '\'' +
                    ", data=" + data +
                    ", code='" + code + '\'' +
                    ", type=" + type +
                    ", createdAt='" + createdAt + '\'' +
                    '}' + "\n";
        }
    }
}
