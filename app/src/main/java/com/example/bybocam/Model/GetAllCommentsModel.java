package com.example.bybocam.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllCommentsModel {

@SerializedName("code")
@Expose
private String code;
@SerializedName("status")
@Expose
private String status;
@SerializedName("data")
@Expose
private List<Datum> data = null;

public String getCode() {
return code;
}

public void setCode(String code) {
this.code = code;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

    public class Datum {

        @SerializedName("postMessageId")
        @Expose
        private String postMessageId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("postMessage")
        @Expose
        private String postMessage;
        @SerializedName("postMessageStatus")
        @Expose
        private String postMessageStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("userData")
        @Expose
        private List<UserDatum> userData = null;

        public String getPostMessageId() {
            return postMessageId;
        }

        public void setPostMessageId(String postMessageId) {
            this.postMessageId = postMessageId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getPostMessage() {
            return postMessage;
        }

        public void setPostMessage(String postMessage) {
            this.postMessage = postMessage;
        }

        public String getPostMessageStatus() {
            return postMessageStatus;
        }

        public void setPostMessageStatus(String postMessageStatus) {
            this.postMessageStatus = postMessageStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<UserDatum> getUserData() {
            return userData;
        }

        public void setUserData(List<UserDatum> userData) {
            this.userData = userData;
        }

        public class UserDatum {

            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("firstName")
            @Expose
            private String firstName;
            @SerializedName("lastName")
            @Expose
            private String lastName;
            @SerializedName("userName")
            @Expose
            private String userName;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("dateOfBirth")
            @Expose
            private String dateOfBirth;
            @SerializedName("userImage")
            @Expose
            private String userImage;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("latitude")
            @Expose
            private String latitude;
            @SerializedName("longitude")
            @Expose
            private String longitude;
            @SerializedName("otp")
            @Expose
            private String otp;
            @SerializedName("varifiedStatus")
            @Expose
            private String varifiedStatus;
            @SerializedName("loginStatus")
            @Expose
            private String loginStatus;
            @SerializedName("userRole")
            @Expose
            private String userRole;
            @SerializedName("deviceType")
            @Expose
            private String deviceType;
            @SerializedName("deviceId")
            @Expose
            private String deviceId;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDateOfBirth() {
                return dateOfBirth;
            }

            public void setDateOfBirth(String dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getOtp() {
                return otp;
            }

            public void setOtp(String otp) {
                this.otp = otp;
            }

            public String getVarifiedStatus() {
                return varifiedStatus;
            }

            public void setVarifiedStatus(String varifiedStatus) {
                this.varifiedStatus = varifiedStatus;
            }

            public String getLoginStatus() {
                return loginStatus;
            }

            public void setLoginStatus(String loginStatus) {
                this.loginStatus = loginStatus;
            }

            public String getUserRole() {
                return userRole;
            }

            public void setUserRole(String userRole) {
                this.userRole = userRole;
            }

            public String getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

        }
    }

}