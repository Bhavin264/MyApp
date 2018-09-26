package com.fittrack.Constants;

import android.os.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Umesh on 7/26/2017.
 */
public class Constants {

    public static final String APP_PdfKG = "com.fittrack";
    public static final String AUTH_USERNAME = "fittrack";
    public static final String AUTH_PASSWORD = "jd3Z=fn[\"2#^su$Y";

    public static final String DATE_YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    public static final int PHOTO_GALLERY = 10;
    public static final int PHOTO_CAMERA = 20;
    public static int RESULT_CROP_GALLERY = 30;
    public static int RESULT_CROP_CAMERA = 40;

    public static final int PHOTO_GALLERY1 = 50;
    public static final int PHOTO_CAMERA1 = 60;
    public static int RESULT_CROP_GALLERY1 = 70;
    public static int RESULT_CROP_CAMERA1 = 80;

    /*Mint key*/
    public static final String Mint_key = "e53eeb53";
//    Mint.initAndStartSession(this.getApplication(), "e53eeb53");

    /*Register*/
    public static final String username = "username";
    public static final String gender = "gender";
    public static final String height = "height";
    public static final String weight = "weight";
    public static final String birthdate = "birthdate";
    public static final String mobile = "mobile";
    public static final String email = "email";
    public static final String activity_type = "activity_type";
    public static final String password = "password";
    public static final String profile_image = "profile_image";
    public static final String lang = "lang";
    public static final String device_token = "device_token";
    public static final String register_id = "register_id";
    public static final String device_type = "device_type";
    public static final String DEVICE_TYPE_ANDROID = "0";
    public static final String language = "0";
    public static final String login_type_1  = "1";

    public static final String fb_access_token  = "fb_access_token";
    public static String gcm_registration_id = "gcm_registration_id";
    public static String image_register = "image_register";
    public static String user_gson = "user_gson";
    public static String user_id = "user_id";
    public static String access_token = "access_token";
    public static String login_type = "login_type";

    /*crop*/
    public static final int DEFAULT_REGISTER_PICTURE_CROP_RATIO_WIDTH = 1;
    public static final int DEFAULT_REGISTER_PICTURE_CROP_RATIO_HEIGHT = 1;

    public static final int DEFAULT_PICTURE_CROP_RATIO_WIDTH = 1;
    public static final int DEFAULT_PICTURE_CROP_RATIO_HEIGHT = 1;
    public static final String IMAGE_CROP_RATIO_WIDTH = "image_crop_ratio_width";
    public static final String IMAGE_CROP_RATIO_HEIGHT = "image_crop_ratio_height";
    public static String FLAG_IS_SQUARE = "flag_is_square";
    public static final String App_ROOT_FOLDER = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath() + "/" + "FitTrack";
    public static final String IMAGE_FILE_NAME_PREFIX = "IMG_CAM" + "_X" + ".jpg";

    /*pagination*/
    public static String pagination_last_offset = "-1";

    /*Weight Sheet*/
    public static String offset = "offset";
    public static int ITEM_CLICK = 10;
    public static String date = "date";


    /*change password*/
    public static String current_password = "current_password";
    public static String new_password = "new_password";

    /*Add Forum*/
    public static String forum_topic = "forum_topic";
    public static String forum_title = "forum_title";
    public static String description = "description";
    public static String forum_id = "forum_id";

    /*comment*/
    public static String comment_text = "comment_text";

    /*Add photo*/
    public static String user_photo = "user_photo";
    public static String position = "position";
        public static String Imagelist = "Imagelist";

    /*Compare photo*/
    public static String user_before_photo = "user_before_photo";
    public static String before_date = "before_date";
    public static String before_weight = "before_weight";
    public static String after_weight = "after_weight";
    public static String user_after_photo = "user_after_photo";
    public static String after_date = "after_date";

    /*facebook*/
    public static List<String> FB_READ_PERMISSIONS = Arrays.asList(
            "public_profile", "email", "user_photos", "user_birthday");

    /*weight chart*/
    public static String month = "month";
    public static String date_type = "date_type";
    public static String date_type_year = "1";
    public static String date_type_month = "2";

    /*Feedback*/
    public static String message = "message";
    public static String name = "name";

    /*Photo*/
    public static String Image = "Image";
    public static String Image1 = "Image1";
    public static String Image2 = "Image2";
    public static final String weight1 = "weight1";
    public static final String weight2 = "weight2";
    public static String date1= "date1";
    public static String date2 = "date2";

    /*Diet food*/
    public static String category_id = "category_id";
    public static String food_id = "food_id";
    public static String food_avoid_id = "food_avoid_id";

    /*Chat*/
    public static String timezone = "timezone";
    public static final String DATE_FULL_FORMAT = "yyyy-MM-dd hh:mm a";

    /*push*/
    public static String push_type = "push_type";
    public static String push_message = "msg";
    public static String filterAction = "100";
    public static String custom_data = "custom_data";
    public static String notifyID = "notifyID";

    /*Initial profile*/
    public static final String firstnmae = "firstnmae";
    public static final String lastname = "lastname";
    public static final String id = "id";




}
