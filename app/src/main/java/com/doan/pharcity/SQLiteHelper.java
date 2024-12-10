package com.doan.pharcity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper<S> extends SQLiteOpenHelper {

    //Create database
    private static final String DB_NAME ="mySQLitePharCity";
    private static final int VERSION = 1;

    //Data Customer
    public static final String TABLE_NAME_CUSTOMER ="Customers";
    public static final String ID_COL ="id";
    public static final String Name_COL ="name";
    public static final String Email_COL ="email";
    public static final String Phone_COL ="phone";
    public static final String Birth_COL ="birth";
    public static final String Gender_COL ="gender";


    //Data Users
    public static final String TABLE_NAME_USER ="Users";
    public static final String ID_COL_USER ="id";
    public static final String Name_COL_USER ="name";
    public static final String Email_COL_USER ="email";

    //Data Medicine
    public static final String TABLE_NAME_MEDICINE ="Medicines";
    public static final String ID_COL_MEDICINE ="id";
    public static final String Name_COL_MEDICINE ="name";
    public static final String Usage_COL_MEDICINE ="usage";
    public static final String Price_COL_MEDICINE ="price";
    public static final String Quantity_COL_MEDICINE ="quantity";
    public static final String Exit_Date_COL_MEDICINE ="exit_date";
    public static final String Image_COL_MEDICINE ="image";
    public static final String Description_COL_MEDICINE ="description";


    //Data Oder
    public static final String TABLE_NAME_ORDER ="Orders";
    public static final String ORDER_ID_COL ="order_id";
    public static final String CUSTOMER_ID_COL ="customer_id";
    public static final String ORDER_DATE_COL ="order_date";
    public static final String TOTAL_PRICE_COL ="total_price";
    public static final String STATUS_COL ="status";

    //Data OrderDetail
    public static final String TABLE_NAME_ORDER_DETAIL ="Order_Details";
    public static final String ORDER_DETAIL_ID_COL ="order_detail_id";
    public static final String ORDER_ID_COL_ORDER_DETAIL ="order_id";

    //Data Payment
    public static final String TABLE_NAME_PAYMENT ="Payments";
    public static final String PAYMENT_ID_COL ="payment_id";
    public static final String ORDER_ID_COL_PAYMENT ="order_id";

    //Data Suppliers
    public static final String TABLE_NAME_SUPPLIERS ="Suppliers";
    public static final String SUPPLIER_ID_COL ="supplier_id";
    public static final String SUPPLIER_NAME_COL ="supplier_name";
    public static final String SUPPLIER_PHONE_COL ="supplier_phone";
    public static final String SUPPLIER_EMAIL_COL ="supplier_email";



    private static final String Customer ="CREATE TABLE " + TABLE_NAME_CUSTOMER + " ("
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Name_COL + " TEXT, "
            + Email_COL + " TEXT, "
            + Phone_COL + " TEXT, "
            + Birth_COL + " TEXT, "
            + Gender_COL + " TEXT)";

    private final String User ="CREATE TABLE " + TABLE_NAME_USER + " ("
            + ID_COL_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Name_COL_USER + " TEXT, "
            + Email_COL_USER + " TEXT)";



    private final String Medicine ="CREATE TABLE " + TABLE_NAME_MEDICINE + " ("
            + ID_COL_MEDICINE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Name_COL_MEDICINE + " TEXT, "
            + Usage_COL_MEDICINE + " TEXT, "
            + Price_COL_MEDICINE + " TEXT, "
            + Quantity_COL_MEDICINE + " TEXT, "
            + Exit_Date_COL_MEDICINE + " TEXT, "
            + Image_COL_MEDICINE + " TEXT, "
            + Description_COL_MEDICINE + " TEXT)";

    private final String Order ="CREATE TABLE " + TABLE_NAME_ORDER + " ("
            + ORDER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CUSTOMER_ID_COL + " INTEGER, "
            + ORDER_DATE_COL + " TEXT, "
            + TOTAL_PRICE_COL + " TEXT, "
            + STATUS_COL + " TEXT)";

    private final String OrderDetail ="CREATE TABLE " + TABLE_NAME_ORDER_DETAIL + " ("
            + ORDER_DETAIL_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ORDER_ID_COL_ORDER_DETAIL + " INTEGER)";

    private final String Payment ="CREATE TABLE " + TABLE_NAME_PAYMENT + " ("
            + PAYMENT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ORDER_ID_COL_PAYMENT + " INTEGER)";

    private final String Suppliers ="CREATE TABLE " + TABLE_NAME_SUPPLIERS + " ("
            + SUPPLIER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SUPPLIER_NAME_COL + " TEXT, "
            + SUPPLIER_PHONE_COL + " TEXT, "
            + SUPPLIER_EMAIL_COL + " TEXT)";


    public  SQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);

    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(Customer);
        db.execSQL(User);
        db.execSQL(Medicine);
        db.execSQL(Order);
        db.execSQL(OrderDetail);
        db.execSQL(Payment);
        db.execSQL(Suppliers);
    }



    //Add Customers
    public void addNewCustomer(String name, String email, String phone, String birth, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Name_COL, name);
        values.put(Email_COL, email);
        values.put(Phone_COL, phone);
        values.put(Birth_COL, birth);
        values.put(Gender_COL, gender);

        db.insert(TABLE_NAME_CUSTOMER,null, values);
        db.close();

    }


    //Add Users
    public void addNewUsers(String name, String email, String phone, String birth, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Name_COL_USER, name);
        values.put(Email_COL_USER, email);
        values.put(Phone_COL, phone);
        values.put(Birth_COL, birth);
        values.put(Gender_COL, gender);

        db.insert(TABLE_NAME_USER, null, values);
        db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ORDER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PAYMENT);
        onCreate(db);
    }


    public void deleteCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME_CUSTOMER, ID_COL + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


    //Update Customers
    public boolean updateCustomers(String name, String email, String phone, String birth, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Name_COL, name);
        values.put(Email_COL, email);
        values.put(Phone_COL, phone);
        values.put(Birth_COL, birth);
        values.put(Gender_COL, gender);

        int rowsAffected = db.update(TABLE_NAME_CUSTOMER, values, ID_COL + "=?", new String[]{phone});
        db.close();

        return rowsAffected > 0;
    }


    public boolean isEmailExist(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Email_COL + " = ?", new String[]{userEmail});
        boolean isExist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isExist;

    }

    public boolean isPhoneExist(String userPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Phone_COL + " = ?", new String[]{userPhone});
        boolean isExist = cursor.getCount() > 0;
        cursor.close();

        db.close();
        return isExist;
    }

    @SuppressLint("Range")
    public String getEmailByPhone(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Phone_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});

        if(cursor != null && cursor.moveToFirst()) {
            String email;
            email = cursor.getString(cursor.getColumnIndex(Email_COL));
            cursor.close();
            db.close();
            return email;
        }else {
            cursor.close();
            return " ";
        }

    }
}
