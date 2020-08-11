package com.example.bybocam.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchUserModel {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
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

    public List<PostDatum> getPostData() {
        return postData;
    }

    public void setPostData(List<PostDatum> postData) {
        this.postData = postData;
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

        public Object getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public Object getPostDescription() {
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

}