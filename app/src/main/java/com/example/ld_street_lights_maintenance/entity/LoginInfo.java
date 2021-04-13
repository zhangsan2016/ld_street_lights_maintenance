package com.example.ld_street_lights_maintenance.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ldgd on 2019/2/28.
 * 功能：
 * 说明：
 */

public class LoginInfo implements Serializable {


    /**
     * errno : 0
     * errmsg :
     * data : {"token":{"username":"ld","token":"1f706c00-8145-11eb-8d74-8b5e97bc2b27","expired":1615428344512},"userProfile":{"_id":27,"username":"ld","phone":"18011223344","fullname":"洛丁","roles":"[\"维护员\"]","openid":"oa89fv9pPkNdTKw5XKqcR6KOwcMg"},"grantedActions":["device/view","project/view","v_device_ebox/view","v_device_lamp/view","v_device_warning/view","v_device_wiresafe/view","v_ticket/view","menu_light/main","menu_light/home","device_ebox/view","device_lamp/view","device_wiresafe/view","device_warning/view","menu_video/main","menu_video/vehicle","device/view","project/view","v_device_ebox/view","v_device_lamp/view","v_device_wiresafe/view","v_device_warning/view","v_ticket/view","device/new","project/new","project/edit","device/edit","v_device_ebox/new","v_device_lamp/new","v_device_wiresafe/new","v_ticket/new","v_ticket/edit","v_device_warning/delete","v_device_warning/close","v_device_wiresafe/edit","v_device_lamp/edit","v_device_ebox/edit","v_device_wiresafe/control","v_device_lamp/control","v_device_ebox/control","v_device_ebox/metrics","v_device_lamp/metrics","v_device_wiresafe/metrics","menu_light/main","menu_video/main","menu_light/home","menu_video/vehicle","device_ebox/view","device_lamp/view","device_wiresafe/view","device_warning/view","ticket/view","ticket/new","menu_video/person","device_ebox/new","device_lamp/new","device_wiresafe/new","device_wiresafe/edit","device_lamp/edit","device_ebox/edit","device_ebox/control","device_wiresafe/control","device_lamp/control","menu_alarm/main"]}
     */

    private int errno;
    private String errmsg;
    private LoginInfo.DataBean data;

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

    public LoginInfo.DataBean getData() {
        return data;
    }

    public void setData(LoginInfo.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : {"username":"ld","token":"1f706c00-8145-11eb-8d74-8b5e97bc2b27","expired":1615428344512}
         * userProfile : {"_id":27,"username":"ld","phone":"18011223344","fullname":"洛丁","roles":"[\"维护员\"]","openid":"oa89fv9pPkNdTKw5XKqcR6KOwcMg"}
         * grantedActions : ["device/view","project/view","v_device_ebox/view","v_device_lamp/view","v_device_warning/view","v_device_wiresafe/view","v_ticket/view","menu_light/main","menu_light/home","device_ebox/view","device_lamp/view","device_wiresafe/view","device_warning/view","menu_video/main","menu_video/vehicle","device/view","project/view","v_device_ebox/view","v_device_lamp/view","v_device_wiresafe/view","v_device_warning/view","v_ticket/view","device/new","project/new","project/edit","device/edit","v_device_ebox/new","v_device_lamp/new","v_device_wiresafe/new","v_ticket/new","v_ticket/edit","v_device_warning/delete","v_device_warning/close","v_device_wiresafe/edit","v_device_lamp/edit","v_device_ebox/edit","v_device_wiresafe/control","v_device_lamp/control","v_device_ebox/control","v_device_ebox/metrics","v_device_lamp/metrics","v_device_wiresafe/metrics","menu_light/main","menu_video/main","menu_light/home","menu_video/vehicle","device_ebox/view","device_lamp/view","device_wiresafe/view","device_warning/view","ticket/view","ticket/new","menu_video/person","device_ebox/new","device_lamp/new","device_wiresafe/new","device_wiresafe/edit","device_lamp/edit","device_ebox/edit","device_ebox/control","device_wiresafe/control","device_lamp/control","menu_alarm/main"]
         */

        private LoginInfo.DataBean.TokenBean token;
        private LoginInfo.DataBean.UserProfileBean userProfile;
        private List<String> grantedActions;

        public LoginInfo.DataBean.TokenBean getToken() {
            return token;
        }

        public void setToken(LoginInfo.DataBean.TokenBean token) {
            this.token = token;
        }

        public LoginInfo.DataBean.UserProfileBean getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(LoginInfo.DataBean.UserProfileBean userProfile) {
            this.userProfile = userProfile;
        }

        public List<String> getGrantedActions() {
            return grantedActions;
        }

        public void setGrantedActions(List<String> grantedActions) {
            this.grantedActions = grantedActions;
        }

        public static class TokenBean {
            /**
             * username : ld
             * token : 1f706c00-8145-11eb-8d74-8b5e97bc2b27
             * expired : 1615428344512
             */

            private String username;
            private String token;
            private long expired;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public long getExpired() {
                return expired;
            }

            public void setExpired(long expired) {
                this.expired = expired;
            }
        }

        public static class UserProfileBean {
            /**
             * _id : 27
             * username : ld
             * phone : 18011223344
             * fullname : 洛丁
             * roles : ["维护员"]
             * openid : oa89fv9pPkNdTKw5XKqcR6KOwcMg
             */

            private int _id;
            private String username;
            private String phone;
            private String fullname;
            private String roles;
            private String openid;

            public int get_id() {
                return _id;
            }

            public void set_id(int _id) {
                this._id = _id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getFullname() {
                return fullname;
            }

            public void setFullname(String fullname) {
                this.fullname = fullname;
            }

            public String getRoles() {
                return roles;
            }

            public void setRoles(String roles) {
                this.roles = roles;
            }

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }
        }
    }



}
