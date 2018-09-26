package com.fittrack.Interface;

import com.fittrack.Model.Chat.GsonChat;
import com.fittrack.Model.Chat.GsonSendMsg;
import com.fittrack.Model.CompareClg.GsonCompare;
import com.fittrack.Model.CompareClg.GsonCompareList;
import com.fittrack.Model.Feedback.GsonFeedback;
import com.fittrack.Model.FoodAvoid.GsonFoodAvoid;
import com.fittrack.Model.FoodCategory.GsonDietFood;
import com.fittrack.Model.FoodCategory.GsonFoodCategory;
import com.fittrack.Model.FoodCategory.GsonFoodDetails;
import com.fittrack.Model.ForgotPassword.GsonForgotPassword;
import com.fittrack.Model.Forum.Comment.GsonComment;
import com.fittrack.Model.Forum.ForumDetails.GsonForumDetails;
import com.fittrack.Model.Forum.GsonAddForum;
import com.fittrack.Model.Forum.GsonForum;
import com.fittrack.Model.GsonLogout;
import com.fittrack.Model.PhotoGallery.GsonAddPhoto;
import com.fittrack.Model.PhotoGallery.GsonPhoto;
import com.fittrack.Model.Profile.GsonChangePassword;
import com.fittrack.Model.Profile.GsonSaveProfile;
import com.fittrack.Model.Register.GsonRegister;
import com.fittrack.Model.WeightChart.GsonWeightChart;
import com.fittrack.Model.WeightSheet.GsonAddWeightSheet;
import com.fittrack.Model.WeightSheet.GsonWeightsheet;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiInterface {

    @Multipart
    @POST("user/register/")
    Call<GsonRegister> getJSON(
            @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("user/login/")
    Call<GsonRegister> getJSONLogin(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("weight/weight_list/")
    Call<GsonWeightsheet> getWeightSheetList(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("weight/save_weight/")
    Call<GsonAddWeightSheet> getAddWeightSheetList(@FieldMap Map<String, String> user);


    @FormUrlEncoded
    @POST("user/change_password/")
    Call<GsonChangePassword> getChangePasswordList(@FieldMap Map<String, String> user);

    @Multipart
    @POST("user/save_user_profile/")
    Call<GsonSaveProfile> getSaveProfile(
            @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("forum/forum_list/")
    Call<GsonForum> getForumList(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("forum/save_forum/")
    Call<GsonAddForum> getAddForum(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("forum/add_comment/")
    Call<GsonComment> getAddComment(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("forum/forum_details/")
    Call<GsonForumDetails> getForumDetails(@FieldMap Map<String, String> user);

    @Multipart
    @POST("photo/save_photo/")
    Call<GsonAddPhoto> getAddPhoto(
            @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("photo/user_photo_list/")
    Call<GsonPhoto> getPhotoList(@FieldMap Map<String, String> user);


    @FormUrlEncoded
    @POST("user/forgot_password/")
    Call<GsonForgotPassword> getForgotPassword(@FieldMap Map<String, String> user);

    /*multiple images upload*/
    @Multipart
    @POST("photo/save_compare_photo/")
    Call<GsonCompare> getComparePhoto(
            @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2);

//    @FormUrlEncoded
//    @POST("user/login_with_facebook/")
//    Call<GsonRegister> getFacebookLogin(@FieldMap Map<String, String> user);


    @Multipart
    @POST("user/login_with_facebook/")
    Call<GsonRegister> getLoginWithFacebook(
            @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("user/login_facebook_type/")
    Call<GsonRegister> getCheckFbInitialLogin(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("chart/user_weight_chart/")
    Call<GsonWeightChart> getWeightChart(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("user/logout/")
    Call<GsonLogout> getLogout(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("user/save_feedback/")
    Call<GsonFeedback> getFeedback(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("photo/user_compare_photo_list/")
    Call<GsonCompareList> getComparePhotoList(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("food/category_list/")
    Call<GsonFoodCategory> getFoodCategoryList(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("food/food_directory_list/")
    Call<GsonDietFood> getDietFoodDirectoryList(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("food/food_directory_details/")
    Call<GsonFoodDetails> getFoodDetailsList(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("food/food_avoids/")
    Call<GsonFoodAvoid> getFoodAvoid(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("chat/message_list/")
    Call<GsonChat> getChatList(@FieldMap Map<String, String> user);

    @FormUrlEncoded
    @POST("chat/send_message/")
    Call<GsonSendMsg> getSendMsg(@FieldMap Map<String, String> user);


//    @Multipart
//    @POST("upload")
//    Call<ResponseBody> uploadMultipleFiles(
//            @Part("description") RequestBody description,
//            @Part MultipartBody.Part file1,
//            @Part MultipartBody.Part file2);

}
