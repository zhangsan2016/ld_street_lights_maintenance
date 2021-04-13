package com.example.ld_street_lights_maintenance.entity;

import java.util.List;

public class ProjectInfo {


    //   {"errno":0,"errmsg":"","data":{"count":2,"totalPages":1,"pageSize":1000,"currentPage":1,"data":[{"_id":40,"title":"中科洛丁展示项目/重庆展厅","lng":"106.541885","lat":"29.803683","smsphone":"","subgroups":"[\"无线版控制器\"]","admin":"ld","code":"400020","alarmreceivers":"[]","members":"[\"ld\"]","referencepower":0,"modules":"[\"light\",\"video\",\"dangervehicle\",\"alarm\",\"environment\",\"security\",\"home\"]"},{"_id":41,"title":"中科洛丁展示项目/深圳展厅","lng":"114.003727","lat":"22.635131","smsphone":"18033440703","subgroups":"[\"1\",\"2\"]","admin":"ld","code":"518100","alarmreceivers":"[\"chenyong\", \"ld\"]","members":"[\"ld\",\"lz\"]","referencepower":0,"modules":"[\"home\",\"light\",\"video\",\"dangervehicle\",\"alarm\",\"environment\",\"security\"]"}]}}
    /**
     * errno : 0
     * errmsg :
     * data : {"count":2,"totalPages":1,"pageSize":1000,"currentPage":1,"data":[{"_id":40,"title":"中科洛丁展示项目/重庆展厅","lng":"106.541885","lat":"29.803683","smsphone":"","subgroups":"[\"无线版控制器\"]","admin":"ld","code":"400020","alarmreceivers":"[]","members":"[\"ld\"]","referencepower":0,"modules":"[\"light\",\"video\",\"dangervehicle\",\"alarm\",\"environment\",\"security\",\"home\"]"},{"_id":41,"title":"中科洛丁展示项目/深圳展厅","lng":"114.003727","lat":"22.635131","smsphone":"18033440703","subgroups":"[\"1\",\"2\"]","admin":"ld","code":"518100","alarmreceivers":"[\"chenyong\", \"ld\"]","members":"[\"ld\",\"lz\"]","referencepower":0,"modules":"[\"home\",\"light\",\"video\",\"dangervehicle\",\"alarm\",\"environment\",\"security\"]"}]}
     */

    private int errno;
    private String errmsg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * count : 2
         * totalPages : 1
         * pageSize : 1000
         * currentPage : 1
         * data : [{"_id":40,"title":"中科洛丁展示项目/重庆展厅","lng":"106.541885","lat":"29.803683","smsphone":"","subgroups":"[\"无线版控制器\"]","admin":"ld","code":"400020","alarmreceivers":"[]","members":"[\"ld\"]","referencepower":0,"modules":"[\"light\",\"video\",\"dangervehicle\",\"alarm\",\"environment\",\"security\",\"home\"]"},{"_id":41,"title":"中科洛丁展示项目/深圳展厅","lng":"114.003727","lat":"22.635131","smsphone":"18033440703","subgroups":"[\"1\",\"2\"]","admin":"ld","code":"518100","alarmreceivers":"[\"chenyong\", \"ld\"]","members":"[\"ld\",\"lz\"]","referencepower":0,"modules":"[\"home\",\"light\",\"video\",\"dangervehicle\",\"alarm\",\"environment\",\"security\"]"}]
         */

        private int count;
        private int totalPages;
        private int pageSize;
        private int currentPage;
        private List<DataBean> data;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * _id : 40
             * title : 中科洛丁展示项目/重庆展厅
             * lng : 106.541885
             * lat : 29.803683
             * smsphone :
             * subgroups : ["无线版控制器"]
             * admin : ld
             * code : 400020
             * alarmreceivers : []
             * members : ["ld"]
             * referencepower : 0
             * modules : ["light","video","dangervehicle","alarm","environment","security","home"]
             */

            private int _id;
            private String title;
            private String lng;
            private String lat;
            private String smsphone;
            private String subgroups;
            private String admin;
            private String code;
            private String alarmreceivers;
            private String members;
            private int referencepower;
            private String modules;

            public int get_id() {
                return _id;
            }

            public void set_id(int _id) {
                this._id = _id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getSmsphone() {
                return smsphone;
            }

            public void setSmsphone(String smsphone) {
                this.smsphone = smsphone;
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

            public String getAlarmreceivers() {
                return alarmreceivers;
            }

            public void setAlarmreceivers(String alarmreceivers) {
                this.alarmreceivers = alarmreceivers;
            }

            public String getMembers() {
                return members;
            }

            public void setMembers(String members) {
                this.members = members;
            }

            public int getReferencepower() {
                return referencepower;
            }

            public void setReferencepower(int referencepower) {
                this.referencepower = referencepower;
            }

            public String getModules() {
                return modules;
            }

            public void setModules(String modules) {
                this.modules = modules;
            }
        }
    }
}
