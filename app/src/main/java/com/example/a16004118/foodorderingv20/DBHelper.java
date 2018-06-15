package com.example.a16004118.foodorderingv20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.lang.UScript;
import android.util.Log;

import com.example.a16004118.foodorderingv20.Object.Category;
import com.example.a16004118.foodorderingv20.Object.Menu;
import com.example.a16004118.foodorderingv20.Object.Order;
import com.example.a16004118.foodorderingv20.Object.OrderDetail;
import com.example.a16004118.foodorderingv20.Object.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //TODO Define the Database properties

    //log tag
    private static final String TAG = "DBHelper";

    //database name
    private static final String DATABASE_NAME = "foodOrderV20.db";
    private static final int DATABASE_VERSION = 1;

    //table name
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_MENU = "menu";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_USER = "user";
    private static final String TABLE_ORDERDETAIL = "orderDetail";

    //category table column
    private static final String CATEGORY_COLUMN_ID = "_categoryId";
    private static final String CATEGORY_COLUMN_NAME = "categoryName";
    private static final String CATEGORY_COLUMN_DESCRIPTION = "description";

    //menu table column
    private static final String MENU_COLUMN_ID = "_menuId";
    private static final String MENU_COLUMN_CATEGORYID = "categoryId";
    private static final String MENU_COLUMN_FOODNAME = "foodName";
    private static final String MENU_COLUMN_PRICE = "price";
    private static final String MENU_COLUMN_DESCRIPTION = "description";
    private static final String MENU_COLUMN_MOSTSELLER = "mostSeller";

    //orderDetail table column
    private static final String ORDERDETAIL_COLUMN_ID = "_orderDetailId";
    private static final String ORDERDETAIL_COLUMN_ORDERID = "orderId";
    private static final String ORDERDETAIL_COLUMN_MENUID = "menuId";
    private static final String ORDERDETAIL_COLUMN_QUANTITY = "quantity";

    //order table column
    private static final String ORDERS_COLUMN_ID = "orderId";
    private static final String ORDERS_COLUMN_USERID = "userId";
    private static final String ORDERS_COLUMN_ORDERTIME = "orderTime";

    //user table column
    private static final String USER_COLUMN_ID = "_userId";
    private static final String USER_COLUMN_FBID = "fbId";

    //tables create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
            + CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CATEGORY_COLUMN_NAME + " TEXT, "
            + CATEGORY_COLUMN_DESCRIPTION + " TEXT )";

    private static final String CREATE_TABLE_MENU = "CREATE TABLE " + TABLE_MENU + "("
            + MENU_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MENU_COLUMN_CATEGORYID + " INTEGER, "
            + MENU_COLUMN_FOODNAME + " TEXT , "
            + MENU_COLUMN_PRICE + " REAL , "
            + MENU_COLUMN_DESCRIPTION + " TEXT , "
            + MENU_COLUMN_MOSTSELLER + " INTEGER )";

    private static final String CREATE_TABLE_ORDERDETAIL = "CREATE TABLE " + TABLE_ORDERDETAIL + "("
            + ORDERDETAIL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ORDERDETAIL_COLUMN_ORDERID + " INTEGER, "
            + ORDERDETAIL_COLUMN_MENUID + " INTEGER , "
            + ORDERDETAIL_COLUMN_QUANTITY+ " INTEGER )";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + ORDERS_COLUMN_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
            + ORDERS_COLUMN_USERID + " INTEGER, "
            + ORDERS_COLUMN_ORDERTIME + " TEXT )";

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_COLUMN_FBID + " INTEGER )";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_MENU);
        db.execSQL(CREATE_TABLE_ORDERDETAIL);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_USER);

        //create demo category
//        for (int i = 0; i < 4; i++) {
//            ContentValues values = new ContentValues();
//            values.put(CATEGORY_COLUMN_NAME, "Category Name: name" + i);
//            values.put(CATEGORY_COLUMN_DESCRIPTION, "Category Description: description" + i);
//            db.insert(TABLE_CATEGORY, null, values);
//        }
//        Log.i("Info", "record inserted");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERDETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
//        db.execSQL("ALTER TABLE " + TABLE_SONG + " ADD COLUMN module_name TEXT ");
    }

    //create a new category
    public long insertCategory(Category category) {
        //TODO insert the data into the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, category.getCategoryName());
        values.put(CATEGORY_COLUMN_DESCRIPTION, category.getDescription());
        long result = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        Log.d(TAG + " insertCategory", "" + result); //id returned, shouldn’t be -1
        return result;
    }

    //create a new menu
    public long insertMenu(Menu menu) {
        //TODO insert the data into the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MENU_COLUMN_CATEGORYID, menu.getCategoryId());
        values.put(MENU_COLUMN_FOODNAME, menu.getDescription());
        values.put(MENU_COLUMN_PRICE, menu.getPrice());
        values.put(MENU_COLUMN_DESCRIPTION, menu.getDescription());
        values.put(MENU_COLUMN_MOSTSELLER, ((menu.isMostSeller()) ? 1 : 0));

        long result = db.insert(TABLE_MENU, null, values);
        db.close();
        Log.d(TAG + " insertMenu", "" + result); //id returned, shouldn’t be -1
        return result;
    }

    //create a new orderdetail
    public long insertOrderDetail(OrderDetail orderDetail) {
        //TODO insert the data into the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDERDETAIL_COLUMN_ID, orderDetail.getOrderDetailId());
        values.put(ORDERDETAIL_COLUMN_ORDERID, orderDetail.getOrderId());
        values.put(ORDERDETAIL_COLUMN_MENUID, orderDetail.getMenuId());
        values.put(ORDERDETAIL_COLUMN_QUANTITY, orderDetail.getQuantity());

        long result = db.insert(TABLE_MENU, null, values);
        db.close();
        Log.d(TAG + " insertOD", "" + result); //id returned, shouldn’t be -1
        return result;
    }

    //create a new order
    public long insertOrder(Order order) {
        //TODO insert the data into the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDERS_COLUMN_ID, order.getOrderId());
        values.put(ORDERS_COLUMN_USERID, order.getUserId());
        values.put(ORDERS_COLUMN_ORDERTIME, order.getOrderTime());;
        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();
        Log.d(TAG + " insertOrder", "" + result); //id returned, shouldn’t be -1
        return result;
    }

    //create a new user
    public long insertUser(User user) {
        //TODO insert the data into the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_FBID, user.getFbId());
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        Log.d(TAG + " insertUser", "" + result); //id returned, shouldn’t be -1
        return result;
    }


    //get all category
    public ArrayList<Category> getAllCategories() {
        //TODO return records in Java objects

        ArrayList<Category> categories = new ArrayList<>();
        String selectQuery = "SELECT " + CATEGORY_COLUMN_ID + ", "
                + CATEGORY_COLUMN_NAME + ", "
                + CATEGORY_COLUMN_DESCRIPTION +" FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String categoryName = cursor.getString(1);
                String description = cursor.getString(2);
                Category currentCategory = new Category(id,categoryName, description);
                categories.add(currentCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    //get all menu
    public ArrayList<Menu> getAllMenus() {
        //TODO return records in Java objects

        ArrayList<Menu> menus = new ArrayList<>();
        String selectQuery = "SELECT " + MENU_COLUMN_ID + ", "
                + MENU_COLUMN_CATEGORYID + ", "
                + MENU_COLUMN_FOODNAME + ", "
                + MENU_COLUMN_PRICE + ", "
                + MENU_COLUMN_DESCRIPTION + ", "
                + MENU_COLUMN_MOSTSELLER + " FROM " + TABLE_MENU;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int categoryId = cursor.getInt(1);
                String foodName = cursor.getString(2);
                double price = cursor.getDouble(3);
                String description = cursor.getString(4);
                boolean mostSeller = (cursor.getInt(5) == 1);
                Menu currentMenu = new Menu(id,categoryId, foodName, price, description, mostSeller);
                menus.add(currentMenu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menus;
    }

    //get all orderDetail
    public ArrayList<OrderDetail> getAllOrderDetail() {
        //TODO return records in Java objects

        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        String selectQuery = "SELECT " + ORDERDETAIL_COLUMN_ID + ", "
                + ORDERDETAIL_COLUMN_ORDERID + ", "
                + ORDERDETAIL_COLUMN_MENUID + ", "
                + ORDERDETAIL_COLUMN_QUANTITY + " FROM " + TABLE_ORDERDETAIL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int orderDetailId = cursor.getInt(0);
                int orderId = cursor.getInt(1);
                int menuId = cursor.getInt(2);
                int quantity = cursor.getInt(3);
                OrderDetail currentOrderDetail = new OrderDetail(orderDetailId, orderId, menuId, quantity);
                orderDetails.add(currentOrderDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderDetails;
    }

    //get all order
    public ArrayList<Order> getAllOrder() {
        //TODO return records in Java objects

        ArrayList<Order> orders = new ArrayList<>();
        String selectQuery = "SELECT " + ORDERS_COLUMN_ID + ", "
                + ORDERS_COLUMN_USERID + ", "
                + ORDERS_COLUMN_ORDERTIME + " FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                String orderTime = cursor.getString(2);
                Order currentOrder = new Order(orderId, userId, orderTime);
                orders.add(currentOrder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

    //get all user
    public ArrayList<User> getAllUser() {
        //TODO return records in Java objects

        ArrayList<User> users = new ArrayList<>();
        String selectQuery = "SELECT " + USER_COLUMN_ID + ", "
                + USER_COLUMN_FBID + " FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(0);
                int  fbId = cursor.getInt(1);
                User currentUser = new User(userId, fbId);
                users.add(currentUser);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }
/*
    public ArrayList<Song> getAllNotesbyStars(int stars) {
        ArrayList<Song> songs = new ArrayList<Song>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_TITLE, COLUMN_SINGERS, COLUMN_YEAR, COLUMN_STARS, COLUMN_ID};
        String condition = COLUMN_STARS + " =?";
        String[] args = {String.valueOf(stars)};
        Cursor cursor = db.query(TABLE_SONG, columns, condition, args,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String singers = cursor.getString(1);
                int year = cursor.getInt(2);
                int star = cursor.getInt(3);
                int id = cursor.getInt(4);
                Song song = new Song(id, title, singers, year, star);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songs;
    }

    public int updateSong(Song data) {
//        Log.i("DBHelper", "id: " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, data.getTitle());
        values.put(COLUMN_SINGERS, data.getSingers());
        values.put(COLUMN_YEAR, data.getYear());
        values.put(COLUMN_STARS, data.getStars());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.get_id())};
        int result = db.update(TABLE_SONG, values, condition, args);

        if (result < 1) {
            Log.i("DBHelper", "Update failed");
            Log.i("DBHelper", result + "");
        } else {
            Log.i("DBHelper", "Update successful");
            Log.i("DBHelper", result + "");
        }

        db.close();
        return result;
    }

    public int deleteSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_SONG, condition, args);
        if (result < 1) {
            Log.d("DBHelper", "Delete failed");
        } else {
            Log.i("DBHelper", "Delete successful");
            Log.i("DBHelper", result + "");
        }
        db.close();
        return result;
    }


    public ArrayList<Integer> getAllYears() {
        ArrayList<Integer> years = new ArrayList<Integer>();

        String selectQuery = "SELECT DISTINCT "
                + COLUMN_YEAR
                + " FROM " + TABLE_SONG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                int year = c.getInt(0);
                years.add(year);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return years;

    }

    public ArrayList<Song> getAllSongsByYear(int year) {
        ArrayList<Song> songs = new ArrayList<Song>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_TITLE,COLUMN_SINGERS,COLUMN_YEAR,COLUMN_STARS};
        String condition = COLUMN_YEAR + " =?";
        String[] args = {String.valueOf(year)};
        Cursor cursor = db.query(TABLE_SONG, columns, condition, args,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String singers = cursor.getString(2);
                int years = cursor.getInt(3);
                int stars = cursor.getInt(4);
                Song song = new Song(id, title, singers,years,stars);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songs;
    }
    */
}