package com.example.bybocam.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikedPostData {

    @SerializedName("postLikeUnlike")
    @Expose
    private String postLikeUnlike;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("likeStatus")
    @Expose
    private String likeStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("postName")
    @Expose
    private Object postName;
    @SerializedName("postDescription")
    @Expose
    private Object postDescription;
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
    private Object postVideoThumbnailImg;
    @SerializedName("postImageVideo")
    @Expose
    private String postImageVideo;
    @SerializedName("postStatus")
    @Expose
    private String postStatus;
    @SerializedName("isliked")
    @Expose
    private String isliked;
    @SerializedName("postCommentsCounts")
    @Expose
    private Integer postCommentsCounts;
    @SerializedName("postLikeCount")
    @Expose
    private Integer postLikeCount;

    public String getPostLikeUnlike() {
        return postLikeUnlike;
    }

    public void setPostLikeUnlike(String postLikeUnlike) {
        this.postLikeUnlike = postLikeUnlike;
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

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
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

    public Object getPostName() {
        return postName;
    }

    public void setPostName(Object postName) {
        this.postName = postName;
    }

    public Object getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(Object postDescription) {
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

    public Object getPostVideoThumbnailImg() {
        return postVideoThumbnailImg;
    }

    public void setPostVideoThumbnailImg(Object postVideoThumbnailImg) {
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

    public String getIsliked() {
        return isliked;
    }

    public void setIsliked(String isliked) {
        this.isliked = isliked;
    }

    public Integer getPostCommentsCounts() {
        return postCommentsCounts;
    }

    public void setPostCommentsCounts(Integer postCommentsCounts) {
        this.postCommentsCounts = postCommentsCounts;
    }

    public Integer getPostLikeCount() {
        return postLikeCount;
    }

    public void setPostLikeCount(Integer postLikeCount) {
        this.postLikeCount = postLikeCount;
    }
}
