package shiful.android.needyserve;

public class Constant {

    public static final String MAIN_URL = "http://medicalsystem.cf/needyserve/android";

    public static final String SIGNUP_URL = MAIN_URL+"/register.php";
    public static final String LOGIN_URL = MAIN_URL+"/login.php";
    public static final String USER_VIEW_URL = MAIN_URL+"/view_user.php?cell=";
    public static final String EVENT_URL = MAIN_URL+"/events.php?";
    public static final String TASK_URL = MAIN_URL+"/task.php?";
    public static final String FOOD_DONATION_URL = MAIN_URL+"/donate_food.php";
    public static final String MONEY_DONATION_URL = MAIN_URL+"/donate_money.php";
    public static final String REVIEW_URL = MAIN_URL+"/review.php?cell=";
    public static final String UPDATE_PROFILE_URL = MAIN_URL+"/update_profile.php";
    public static final String VIEW_FOOD_DONATION_URL = MAIN_URL+"/view_food_donation_history.php?cell=";
    public static final String VIEW_MONEY_DONATION_URL = MAIN_URL+"/view_money_donation_history.php?cell=";
    public static final String VIEW_ALL_REVIEWS_URL = MAIN_URL+"/view_reviews.php";
    public static final String UPDATE_REVIEW_URL = MAIN_URL+"/update_review.php";


    //Keys for server communications
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CELL = "cell";
    public static final String KEY_DIV = "division";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_AC_TYPE = "account_type";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_STATUS = "status";

    public static final String KEY_UPDATE_NAME = "name";
    public static final String KEY_UPDATE_CELL = "cell";
    public static final String KEY_UPDATE_DIV = "division";
    public static final String KEY_UPDATE_ADDRESS = "address";
    public static final String KEY_UPDATE_AC_TYPE = "account_type";

    public static final String KEY_EVENT_NAME = "event_name";
    public static final String KEY_EVENT_DETAILS = "event_details";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_PLACE = "place";
    public static final String KEY_PHONE = "phone";

    public static final String KEY_DONOR_NAME = "donor_name";
    public static final String KEY_DONOR_MOBILE = "donor_mobile";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_DONOR_ADDRESS = "donor_address";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_DELIVERY = "delivery_option";
    public static final String KEY_STATUS_FOOD_DONATE = "status";

    public static final String KEY_FOOD_DONOR_NAME = "name";
    public static final String KEY_FOOD_DONOR_MOBILE = "cell";
    public static final String KEY_QUANTITY_FOOD = "quantity_in_person";
    public static final String KEY_USER_ADDRESS = "address";
    public static final String KEY_DONATION_DATE = "date";
    public static final String KEY_DONATION_TIME = "time";
    public static final String KEY_DELIVERY_OPTION = "delivery_option";
    public static final String KEY_FOOD_DONATION_CONFIRMATION = "confirm";

    public static final String KEY_MONEY_DONOR_NAME = "name";
    public static final String KEY_DONOR_CELL = "cell";
    public static final String KEY_MONEY_DONOR_ADDRESS = "address";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_TRX_ID = "trx_id";
    public static final String KEY_MD_DATE = "date";
    public static final String KEY_MD_TIME = "time";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_STATUS_MONEY_DONATE = "status";

    public static final String KEY_VIEW_MONEY_DONOR_NAME = "name";
    public static final String KEY_VIEW_DONOR_CELL = "cell";
    public static final String KEY_VIEW_MONEY_DONOR_ADDRESS = "address";
    public static final String KEY_VIEW_AMOUNT = "donation_amount";
    public static final String KEY_VIEW_TRX_ID = "bkash_trx_id";
    public static final String KEY_VIEW_MD_DATE = "date";
    public static final String KEY_VIEW_MD_TIME = "time";
    public static final String KEY_VIEW_COMMENT = "comment";
    public static final String KEY_MONEY_DONATION_CONFIRMATION = "confirmation";

    public static final String KEY_TASK = "volunteer_task";

    public static final String KEY_USERNAME = "name";
    public static final String KEY_USERCELL = "cell";
    public static final String KEY_RATING = "rating";
    public static final String KEY_RECOMMEND = "recommend";
    public static final String KEY_REVIEW = "review";

    public static final String KEY_VIEW_USERNAME = "name";
    public static final String KEY_VIEW_RATING = "rating";
    public static final String KEY_VIEW_RECOMMEND = "recommend_us";
    public static final String KEY_VIEW_REVIEW = "review";

    public static final String KEY_UPDATE_USERNAME = "name";
    public static final String KEY__UPDATE_USERCELL = "cell";
    public static final String KEY_UPDATE_RATING = "rating";
    public static final String KEY_UPDATE_RECOMMEND = "recommend_us";
    public static final String KEY_UPDATE_REVIEW = "review";
    //share preference
    //We will use this to store the user cell number into shared preference
    public static final String SHARED_PREF_NAME = "shiful.android.needyserve"; //pcakage name+ id

    //This would be used to store the cell of current logged in user
    public static final String CELL_SHARED_PREF = "cell";

    //json array name.We will received data in this array
    public static final String JSON_ARRAY = "result";
}