package com.example.ld_street_lights_maintenance.entity;

import java.util.List;

public class FirmwareJson {


    /**
     * errno : 0
     * errmsg :
     * data : {"endpoint":"http://asset.sz-luoding.com/","files":["123.jpg","1_1_0_4_7.bin","1_2_0_0_3.bin","APP_4007000001.bin","APP_4007000005.bin","APP_8314000000.bin","APP_8314000003.bin","Kinglog.txt","LD-TYH_GPS_LORAHOST_APP.bin","LD-TYH_GPS_LORA_APP.bin","LD_DCCL_A41_SIGLE.bin","Makefile","rk1808_mqtt_update.zip"]}
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
         * endpoint : http://asset.sz-luoding.com/
         * files : ["123.jpg","1_1_0_4_7.bin","1_2_0_0_3.bin","APP_4007000001.bin","APP_4007000005.bin","APP_8314000000.bin","APP_8314000003.bin","Kinglog.txt","LD-TYH_GPS_LORAHOST_APP.bin","LD-TYH_GPS_LORA_APP.bin","LD_DCCL_A41_SIGLE.bin","Makefile","rk1808_mqtt_update.zip"]
         */

        private String endpoint;
        private List<String> files;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public List<String> getFiles() {
            return files;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }
    }
}
