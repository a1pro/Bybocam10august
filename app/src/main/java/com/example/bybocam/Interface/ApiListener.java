package com.example.bybocam.Interface;

import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.BlockedUser;
import com.example.bybocam.Model.EditProfileModel;
import com.example.bybocam.Model.ForgotModel1;
import com.example.bybocam.Model.GetAllCommentsModel;
import com.example.bybocam.Model.GetAllFollowersModel;
import com.example.bybocam.Model.GetAllMessagesModel;
import com.example.bybocam.Model.GetAllNotificationsModel;
import com.example.bybocam.Model.GetAllPostModel;
import com.example.bybocam.Model.GetChatMessagesModel;
import com.example.bybocam.Model.GetFavouriteUserModel;
import com.example.bybocam.Model.GetFrindsModel;
import com.example.bybocam.Model.GetInfluence;
import com.example.bybocam.Model.GetRandom;
import com.example.bybocam.Model.LikedModel;
import com.example.bybocam.Model.LoginModel;
import com.example.bybocam.Model.RecommendationModel;
import com.example.bybocam.Model.ResponseData;
import com.example.bybocam.Model.SearchUserModel;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.Model.ViewProfileOurUserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiListener {
    @FormUrlEncoded
    @POST("signUp")
    Call<AddComentModel> signupclassAPI
            (@Field("UserName") String UserName,
             @Field("email") String email,
             @Field("password") String password,
             @Field("device_id") String device_id,
             @Field("latitude") String latitude,
             @Field("longitude") String longitude
            );

    @FormUrlEncoded
    @POST("signIn")
    Call<LoginModel> loginclassAPI
            (@Field("email") String email,
             @Field("password") String password,
             @Field("deviceType") String deviceType,
             @Field("deviceId") String deviceId
            );

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ForgotModel1> forgotoneclassAPI
            (@Field("email") String email
            );

    @FormUrlEncoded
    @POST("changePassword")
    Call<AddComentModel> forgotTwoclassAPI
            (@Field("otp") String otp,
             @Field("password") String newPassword,
             @Field("confirmPassword") String oldPassword
            );

    @FormUrlEncoded
    @POST("viewProfile")
    Call<ViewProfileModel> viewProfileAPI
            (@Field("userId") String userId);

    @FormUrlEncoded
    @POST("editProfile")
    Call<EditProfileModel> editProfileClassApi(
            @Field("userId") String userId,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("countryCode") String countryCode,
            @Field("phone") String phone,
            @Field("dateOfBirth") String dateOfBirth,
            @Field("userName") String userName
    );

    @Multipart
    @POST("editProfile")
    Call<EditProfileModel> uploadFile(
            @Part("userId") RequestBody userId,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("countryCode") RequestBody countryCode,
            @Part("phone") RequestBody phone,
            @Part("dateOfBirth") RequestBody dateOfBirt,
            @Part("userName") RequestBody userName,
            @Part MultipartBody.Part userImage);

    @Multipart
    @POST("addVideo")
    Call<AddComentModel> uploadVideo(
            @Part("userId") RequestBody userId,
            @Part("videoId") RequestBody videoId,
            @Part MultipartBody.Part addVideo,
            @Part MultipartBody.Part videoThumbnailimg);

    @FormUrlEncoded
    @POST("postSearch")
    Call<SearchUserModel> searchUserClassApi(
            @Field("userId") String userId,
            @Field("postSearch") String postSearch);

    @Multipart
    @POST("addPost")
    Call<AddComentModel> addPost(
            @Part("userId") RequestBody userId,
            @Part("postType") RequestBody postType,
            @Part MultipartBody.Part postVideoImg,
            @Part MultipartBody.Part postVideoThumbnailImg);

    @Multipart
    @POST("addPost")
    Call<AddComentModel> addPostImage(
            @Part("userId") RequestBody userId,
            @Part("postType") RequestBody postType,
            @Part MultipartBody.Part postVideoImg);

    @FormUrlEncoded
    @POST("getAllPost")
    Call<GetAllPostModel> getAllPostApi
            (@Field("userId") String userId
            );

    @FormUrlEncoded
    @POST("getAllUserByLatLong")
    Call<RecommendationModel> recommendationclassAPI
            (@Field("userId") String userId,
             @Field("lat") String lat,
             @Field("lng") String lng
            );


    @FormUrlEncoded
    @POST("userLikeDislike")
    Call<AddComentModel> likeDislikeclassAPI
            (@Field("senderId") String senderId,
             @Field("recevierId") String recevierId,
             @Field("userLikeStatus") String userLikeStatus
            );

    @FormUrlEncoded
    @POST("getFavouriteUsers")
    Call<GetFavouriteUserModel> likedUserclassAPI
            (@Field("userId") String userId
            );

    @FormUrlEncoded
    @POST("addComments")
    Call<AddComentModel> addMessageclassAPI
            (@Field("senderId") String senderId,
             @Field("recevierId") String recevierId,
             @Field("message") String message);

    @FormUrlEncoded
    @POST("viewProfileOurUser")
    Call<ViewProfileOurUserModel> viewProfileOurUserAPI
            (@Field("userId") String userId,
             @Field("loginUserId") String loginUserId
            );

    @FormUrlEncoded
    @POST("userPostLikeUnlike")
    Call<AddComentModel> postLikeAPI
            (@Field("userId") String userId,
             @Field("postId") String postId,
             @Field("likeStatus") String likeStatus
            );

    @FormUrlEncoded
    @POST("getAllCommentsOnPost")
    Call<GetAllCommentsModel> getAllCommentsclassAPI
            (@Field("postId") String postId
            );

    @FormUrlEncoded
    @POST("addCommentsOnPost")
    Call<AddComentModel> addCommentAPI
            (@Field("userId") String userId,
             @Field("postId") String postId,
             @Field("postMessage") String postMessage
            );

    @FormUrlEncoded
    @POST("getAllComment")
    Call<GetAllMessagesModel> getAllMessagesApi
            (@Field("userId") String userId
            );

    @FormUrlEncoded
    @POST("getChatMessage")
    Call<GetChatMessagesModel> getSingleMessagesApi
            (@Field("senderId") String senderId,
             @Field("recevierId") String recevierId);

    @FormUrlEncoded
    @POST("addSingleChatMessage")
    Call<AddComentModel> addSingleMessagesApi
            (@Field("senderId") String senderId,
             @Field("recevierId") String recevierId,
             @Field("message") String message);


    @Multipart
    @POST("addCommentAsFile")
    Call<AddComentModel> addFileMessagesApi(
            @Part("senderId") RequestBody senderId,
            @Part("recevierId") RequestBody recevierId,
            @Part("messageType") RequestBody messageType,
            @Part MultipartBody.Part messageFile);


    @FormUrlEncoded
    @POST("userLogout")
    Call<AddComentModel> logoutApi
            (@Field("userId") String userId);

    @FormUrlEncoded
    @POST("addUserReport")
    Call<AddComentModel> reportApi
            (@Field("userId") String userId,
             @Field("reportTitle") String reportTitle,
             @Field("reportMessage") String reportMessage);

    @FormUrlEncoded
    @POST("getAllnotificationData")
    Call<GetAllNotificationsModel> getAllNotificationApi
            (@Field("userId") String userId);

    @FormUrlEncoded
    @POST("get_User_List_Liked_Me")
    Call<GetAllFollowersModel> getAllFollowersApi
            (@Field("userId") String userId);


    @Multipart
    @POST("add-Influences")
    Call<ResponseData> AddInfluence
            (@Part("userId") RequestBody userId,
             @Part("name") RequestBody name,
             @Part("latitude") RequestBody latitude,
             @Part("longitude") RequestBody longitude,
             @Part("address") RequestBody address,
             @Part("race") RequestBody race,
             @Part("gender") RequestBody gender,
             @Part("industry") RequestBody industry,
             @Part("price") RequestBody price,
             @Part("discription") RequestBody discription,
             @Part MultipartBody.Part  picture);


    @FormUrlEncoded
    @POST("get-InfulencesData")
    Call<GetInfluence> GetInfluence
            (@Field("id") String id);


    @FormUrlEncoded
    @POST("get-RandomVideos")
    Call<GetRandom> GetRAndomVideo(@Field("userId") String userId);


    @FormUrlEncoded
    @POST("get-InfulencesData")
    Call<GetInfluence> GetInfluenceFilter(@Field("id") String id,
                                          @Field("Infulenlatitude") String Infulenlatitude,
                                          @Field("Infulenlongitude") String Infulenlongitude,
                                          @Field("price") String price,
                                          @Field("industry") String industry,
                                          @Field("gender") String gender,
                                          @Field("race") String race);



    @FormUrlEncoded
    @POST("getFavouriteUsers")
    Call<GetFrindsModel> Friendslist(@Field("userId") String userId);


    @Multipart
    @POST("addRandomVideos")
    Call<ResponseData> AddRandomVideo (@Part MultipartBody.Part RandomVideo,
                                       @Part MultipartBody.Part videoImage,
                                       @Part("senderId") RequestBody senderId,
                                       @Part("revicersIds") RequestBody revicersIds,
                                       @Part("discriptions") RequestBody discriptions);



    @FormUrlEncoded
    @POST("deletePost")
    Call<ResponseData> DeletePost(@Field("userId") String userId,
                                  @Field("postId") String postId);


    @FormUrlEncoded
    @POST("userBlock")
    Call<ResponseData> BlockUser(@Field("loginId") String loginId,
                                  @Field("userId") String userId);



    @FormUrlEncoded
    @POST("getBlockedUsersByMe")
    Call<BlockedUser> GetBlockUserList(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("getPostsLikedByMe")
    Call<LikedModel> GetLikedPosts(@Field("userId") String userId);







}
