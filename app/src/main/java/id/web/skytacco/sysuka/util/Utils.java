package id.web.skytacco.sysuka.util;

import java.io.Serializable;

public class Utils implements Serializable {
    //public static final String SERVER_URL = "http://10.0.2.2/sysuka_recipe";// untuk localhost belum online database dan php
    public static final String SERVER_URL = "http://35.184.2.246/sysuka_recipe";

    public static final boolean ENABLE_EXIT_DIALOG = true;
    public static final int NUM_OF_COLUMNS = 2;
    public static final int NUM_OF_RECENT_RECIPES = 50;
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_CID = "cid";
    public static final String CATEGORY_IMAGE = "category_image";
    public static final String CATEGORY_ARRAY_NAME = "Json";
    public static final String CATEGORY_ITEM_CID = "cid";
    public static final String CATEGORY_ITEM_NAME = "category_name";
    public static final String CATEGORY_ITEM_IMAGE = "category_image";
    public static final String CATEGORY_ITEM_STATUS = "status";
    public static final String CATEGORY_ITEM_CAT_ID = "nid";
    public static final String CATEGORY_ITEM_NEWSHEADING = "news_heading";
    public static final String CATEGORY_ITEM_NEWSDESCRI = "news_description";
    public static final String CATEGORY_ITEM_NEWSIMAGE = "news_image";
    public static final String CATEGORY_ITEM_NEWSDATE = "news_date";
    public static final String CATEGORY_ITEM_NEWSSTATUS = "news_status";

    public static String CATEGORY_TITLE;
    public static int CATEGORY_IDD;
    public static String NEWS_ITEMID;
}
