package com.example.bybocam.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewProfileOurUserModel {

@SerializedName("code")
@Expose
private String code;
@SerializedName("status")
@Expose
private String status;
@SerializedName("data")
@Expose
private List<Datum> data = null;
@SerializedName("totalfavouriteusers")
@Expose
private Integer totalfavouriteusers;
@SerializedName("followers")
@Expose
private Integer followers;
@SerializedName("userVideos")
@Expose
private List<UserVideo> userVideos = null;
@SerializedName("postData")
@Expose
private List<PostDatum> postData = null;

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

public Integer getTotalfavouriteusers() {
return totalfavouriteusers;
}

public void setTotalfavouriteusers(Integer totalfavouriteusers) {
this.totalfavouriteusers = totalfavouriteusers;
}

public Integer getFollowers() {
return followers;
}

public void setFollowers(Integer followers) {
this.followers = followers;
}

public List<UserVideo> getUserVideos() {
return userVideos;
}

public void setUserVideos(List<UserVideo> userVideos) {
this.userVideos = userVideos;
}

public List<PostDatum> getPostData() {
return postData;
}

public void setPostData(List<PostDatum> postData) {
this.postData = postData;
}

    public class UserVideo {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("videoName")
        @Expose
        private String videoName;
        @SerializedName("videoThumbnailimg")
        @Expose
        private String videoThumbnailimg;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoThumbnailimg() {
            return videoThumbnailimg;
        }

        public void setVideoThumbnailimg(String videoThumbnailimg) {
            this.videoThumbnailimg = videoThumbnailimg;
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

    public class PostDatum {

        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("postName")
        @Expose
        private String postName;
        @SerializedName("postDescription")
        @Expose
        private String postDescription;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("postImage")
        @Expose
        private String postImage;
        @SerializedName("postVideo")
        @Expose
        private String postVideo;
        @SerializedName("postVideoThumbnailImg")
        @Expose
        private String postVideoThumbnailImg;
        @SerializedName("postImageVideo")
        @Expose
        private String postImageVideo;
        @SerializedName("postStatus")
        @Expose
        private String postStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("postLikeCount")
        @Expose
        private Integer postLikeCount;
        @SerializedName("postCommentsCounts")
        @Expose
        private Integer postCommentsCounts;
        @SerializedName("isliked")
        @Expose
        private String isliked;

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getPostDescription() {
            return postDescription;
        }

        public void setPostDescription(String postDescription) {
            this.postDescription = postDescription;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public String getPostVideo() {
            return postVideo;
        }

        public void setPostVideo(String postVideo) {
            this.postVideo = postVideo;
        }

        public String getPostVideoThumbnailImg() {
            return postVideoThumbnailImg;
        }

        public void setPostVideoThumbnailImg(String postVideoThumbnailImg) {
            this.postVideoThumbnailImg = postVideoThumbnailImg;
        }

        public String getPostImageVideo() {
            return postImageVideo;
        }

        public void setPostImageVideo(String postImageVideo) {
            this.postImageVideo = postImageVideo;
        }

        public String getPostStatus() {
            return postStatus;
        }

        public void setPostStatus(String postStatus) {
            this.postStatus = postStatus;
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

        public Integer getPostLikeCount() {
            return postLikeCount;
        }

        public void setPostLikeCount(Integer postLikeCount) {
            this.postLikeCount = postLikeCount;
        }

        public Integer getPostCommentsCounts() {
            return postCommentsCounts;
        }

        public void setPostCommentsCounts(Integer postCommentsCounts) {
            this.postCommentsCounts = postCommentsCounts;
        }

        public String getIsliked() {
            return isliked;
        }

        public void setIsliked(String isliked) {
            this.isliked = isliked;
        }

    }
    public class Datum {

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
        @SerializedName("likeStatus")
        @Expose
        private String likeStatus;

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        @SerializedName("countryCode")
        @Expose
        private String countryCode;


        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }

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