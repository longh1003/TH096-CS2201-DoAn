package com.doan.pharcity;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.doan.pharcity.Customers.Customers;
import com.doan.pharcity.Customers.InforCustomers;
import com.doan.pharcity.Customers.nameCustomers;
import com.doan.pharcity.Fragment.Orders;
import com.doan.pharcity.Fragment.Product;

import java.util.ArrayList;
import java.util.List;

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
    public static final String Address_COL ="address";


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
    public static final String QUANTITY_COL ="quantity";
    public static final String PRODUCT_ID_COL ="product_id";
    public static final String PRODUCT_NAME_COL ="product_name";
    public static final String Image_COL ="image";

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
            + Gender_COL + " TEXT,"
            + Address_COL + " TEXT)";

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
            + STATUS_COL + " INTEGER,"
            + QUANTITY_COL + " INTEGER,"
            + ID_COL_MEDICINE + " INTEGER,"
            + Image_COL + " TEXT,"
            + PRODUCT_NAME_COL + " TEXT,"
            + PRODUCT_ID_COL + " INTEGER,"
            + "FOREIGN KEY (" + CUSTOMER_ID_COL + ") REFERENCES " + TABLE_NAME_MEDICINE + "(" + ID_COL_MEDICINE + ")" + ")";

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

        insertSampleData(db);
    }

    //oders
    public void addOders(String customerID, Integer productID , String name_product, String orderDate, double totalPrice, Integer status, Integer quantity, int image){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + QUANTITY_COL + " FROM " + TABLE_NAME_ORDER + " WHERE " + PRODUCT_ID_COL + " = ?", new String[]{String.valueOf(productID)});

        if (cursor.moveToFirst()) {
            int existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY_COL));
            int newQuantity = existingQuantity + quantity;

            ContentValues values = new ContentValues();
            values.put(QUANTITY_COL, newQuantity);
            db.update(TABLE_NAME_ORDER, values, PRODUCT_ID_COL + " = ?", new String[]{String.valueOf(productID)});

        } else {

            ContentValues values = new ContentValues();
            values.put(CUSTOMER_ID_COL, customerID);
            values.put(PRODUCT_ID_COL, productID);
            values.put(PRODUCT_NAME_COL, name_product);
            values.put(ORDER_DATE_COL, orderDate);
            values.put(TOTAL_PRICE_COL, totalPrice);
            values.put(STATUS_COL, status);
            values.put(QUANTITY_COL, quantity); // Lưu số lượng vào cột `QUANTITY_COL`
            values.put(Image_COL, image);       // Lưu hình ảnh vào cột `Image_COL`


            db.insert(TABLE_NAME_ORDER, null, values);
        }
        cursor.close();
        db.close();
    }



    //Add Customers
    public void addNewCustomer(String name, String email, String phone, String birth, String gender, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Name_COL, name);
        values.put(Email_COL, email);
        values.put(Phone_COL, phone);
        values.put(Birth_COL, birth);
        values.put(Gender_COL, gender);
        values.put(Address_COL, address);

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


    public boolean deleteCustomer(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsAffected = db.delete(TABLE_NAME_CUSTOMER, Email_COL + "=?", new String[]{String.valueOf(email)});
        db.close();
        return rowsAffected > 0;
    }


    //Update Customers
    public boolean updateCustomers(String name, String email, String phone, String birth, String gender, String address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Name_COL, name);
        values.put(Email_COL, email);
        values.put(Phone_COL, phone);
        values.put(Birth_COL, birth);
        values.put(Gender_COL, gender);
        values.put(Address_COL, address);

        int rowsAffected = db.update(TABLE_NAME_CUSTOMER, values, ID_COL + "=?", new String[]{email});
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
    @SuppressLint("Range")
    public String getNameByPhone(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Phone_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});

        if (cursor != null && cursor.moveToFirst()) {
            String name;
            name = cursor.getString(cursor.getColumnIndex(Name_COL));
            cursor.close();
            db.close();
            return name;
        } else {
            cursor.close();
            return " ";
        }
    }

    @SuppressLint("Range")
    public String getIdByPhoneCus(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Phone_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});
        if (cursor != null && cursor.moveToFirst()) {
            String id;
            id = cursor.getString(cursor.getColumnIndex(ID_COL));
            cursor.close();
            db.close();
            return id;
        } else {
            cursor.close();
            return " ";
        }
    }


    @SuppressLint("Range")
    public String getIdByEmailCus(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Email_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String id;
            id = cursor.getString(cursor.getColumnIndex(ID_COL));
            cursor.close();
            db.close();
            return id;
        } else {
            cursor.close();
            return " ";
        }
    }

    @SuppressLint("Range")
    public String getNameByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Email_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            String name;
            name = cursor.getString(cursor.getColumnIndex(Name_COL));
            cursor.close();
            db.close();
            return name;
        } else {
            cursor.close();
            return " ";
        }
    }
    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_NAME_MEDICINE + " (" +
                Name_COL_MEDICINE + ", " +
                Usage_COL_MEDICINE + ", " +
                Price_COL_MEDICINE + ", " +
                Quantity_COL_MEDICINE + ", " +
                Exit_Date_COL_MEDICINE + ", " +
                Image_COL_MEDICINE + ", " +
                Description_COL_MEDICINE + ") VALUES " +
                "('Paracetamol', 'Giảm đau, hạ sốt', 50000, 100, '2023-12-31', 'paracetamol.png', 'Thuốc giảm đau, hạ sốt thông thường')," +
                "('Amoxicillin', 'Kháng sinh', 120000, 50, '2024-06-30', 'amoxicillin.png', 'Thuốc kháng sinh dùng điều trị nhiễm trùng')," +
                "('Vitamin C', 'Tăng cường miễn dịch', 70000, 200, '2024-03-15', 'vitamin_c.png', 'Bổ sung vitamin C để tăng sức đề kháng')," +
                "('Ibuprofen', 'Giảm đau, kháng viêm', 85000, 150, '2023-10-20', 'ibuprofen.png', 'Thuốc giảm đau, kháng viêm không steroid')," +
                "('Aspirin', 'Giảm đau, chống đông máu', 60000, 120, '2023-09-15', 'aspirin.png', 'Thuốc giảm đau và ngăn ngừa đông máu')," +
                "('Cetirizine', 'Giảm dị ứng', 45000, 80, '2024-01-10', 'cetirizine.png', 'Thuốc giảm các triệu chứng dị ứng')," +
                "('Azithromycin', 'Kháng sinh mạnh', 180000, 30, '2024-08-01', 'azithromycin.png', 'Kháng sinh dùng điều trị nhiễm trùng nặng')," +
                "('Loratadine', 'Giảm triệu chứng dị ứng', 55000, 90, '2024-02-28', 'loratadine.png', 'Thuốc giảm dị ứng không gây buồn ngủ')," +
                "('Metformin', 'Điều trị tiểu đường', 95000, 70, '2025-01-20', 'metformin.png', 'Thuốc điều trị bệnh tiểu đường type 2')," +
                "('Omeprazole', 'Giảm axit dạ dày', 85000, 110, '2024-05-10', 'omeprazole.png', 'Thuốc giảm tiết axit dạ dày')," +
                "('Prednisolone', 'Kháng viêm mạnh', 135000, 40, '2024-11-15', 'prednisolone.png', 'Thuốc kháng viêm và ức chế miễn dịch')," +
                "('Fexofenadine', 'Giảm dị ứng', 65000, 130, '2024-04-05', 'fexofenadine.png', 'Thuốc điều trị các triệu chứng dị ứng')," +
                "('Clarithromycin', 'Kháng sinh phổ rộng', 200000, 20, '2024-07-01', 'clarithromycin.png', 'Kháng sinh điều trị nhiễm trùng hô hấp và da')," +
                "('Doxycycline', 'Kháng sinh phổ rộng', 140000, 60, '2024-03-12', 'doxycycline.png', 'Kháng sinh dùng điều trị các loại nhiễm trùng khác nhau')," +
                "('Esomeprazole', 'Giảm triệu chứng dạ dày', 90000, 95, '2024-10-20', 'esomeprazole.png', 'Thuốc điều trị bệnh trào ngược dạ dày')," +
                "('Ceftriaxone', 'Kháng sinh tiêm', 300000, 10, '2024-12-30', 'ceftriaxone.png', 'Kháng sinh mạnh dùng qua đường tiêm')," +
                "('Acetylcysteine', 'Hỗ trợ đường hô hấp', 75000, 85, '2024-09-15', 'acetylcysteine.png', 'Thuốc làm loãng đờm và giảm triệu chứng hô hấp')," +
                "('Ranitidine', 'Giảm tiết axit', 65000, 100, '2023-12-01', 'ranitidine.png', 'Thuốc điều trị loét dạ dày và trào ngược')," +
                "('Hydroxychloroquine', 'Điều trị sốt rét', 220000, 15, '2025-06-01', 'hydroxychloroquine.png', 'Thuốc sốt rét và điều trị các bệnh tự miễn')," +
                "('Naproxen', 'Giảm đau kháng viêm', 85000, 60, '2023-11-25', 'naproxen.png', 'Thuốc giảm đau và kháng viêm dài hạn')");
    }

    public List<Product> getAllProducts(){
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_MEDICINE, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL_MEDICINE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Name_COL_MEDICINE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(Description_COL_MEDICINE));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(Price_COL_MEDICINE));
                Integer quantity = cursor.getInt(cursor.getColumnIndexOrThrow(Quantity_COL_MEDICINE));

                productList.add(new Product(id, name, description, price, 0, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    //Lấy danh sách của giỏ hàng
    public List<Orders> getAllOrders(){
        List<Orders> orderList = new ArrayList<>();

      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_ORDER, null);
      if (cursor.moveToFirst()) {
          do {
          int d = cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ID_COL));
          int productID = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID_COL));
          String productName = cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME_COL));
          double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(TOTAL_PRICE_COL));
          Integer quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY_COL));
          int image = cursor.getInt(cursor.getColumnIndexOrThrow(Image_COL));

          orderList.add(new Orders(String.valueOf(d), String.valueOf(productID), null,productName, image, null,totalPrice, null,quantity));
          } while (cursor.moveToNext());


          }
      cursor.close();
      db.close();
      return orderList;
    }

    public void deleteProductFromOrder(int productID) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(TABLE_NAME_ORDER, PRODUCT_ID_COL + " = ?", new String[]{String.valueOf(productID)});
            if (rowsDeleted > 0) {
                Log.d("SQLiteHelper", "Product with ID " + productID + " deleted successfully.");
            } else {
                Log.d("SQLiteHelper", "No product found with ID " + productID + " to delete.");
            }

        }catch (Exception e) {
            Log.d("SQLiteHelper", "Error deleting product: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void updateOrder(int productID, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUANTITY_COL, quantity);

        try{
            //Cập nhật thông tin

            int rowsUpdated = db.update(TABLE_NAME_ORDER, contentValues,
                    PRODUCT_ID_COL + " = ?",
                    new String[]{String.valueOf(productID)});
            if(rowsUpdated > 0){
                Log.d("SQLiteHelper", "Product with ID " + productID + " updated successfully.");
            }else {
                Log.d("SQLiteHelper", "No product found with ID " + productID + " to update.");
            }
        }catch (Exception e){
            Log.e("SQLiteHelper", "Error updating product: " + e.getMessage());
        } finally {
            db.close();
        }
    }


    public int getTotalQuantity() {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalQuantity = 0;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT SUM(" + QUANTITY_COL + ") AS TotalQuantity FROM " + TABLE_NAME_ORDER, null);
            if (cursor.moveToFirst()) {
                totalQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("TotalQuantity"));
            }
        } catch (Exception e) {
            Log.e("SQLiteHelper", "Error calculating total quantity: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return totalQuantity;
    }


    public InforCustomers getInforCusByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CUSTOMER + " WHERE " + Email_COL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {

            String name = cursor.getString(cursor.getColumnIndexOrThrow(Name_COL));
            String emails = cursor.getString(cursor.getColumnIndexOrThrow(Email_COL));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(Phone_COL));
            String birth = cursor.getString(cursor.getColumnIndexOrThrow(Birth_COL));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(Gender_COL));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(Address_COL));
            return new InforCustomers( name, phone, emails, birth, gender, address);
        } else {
            return null;

        }
    }

    public void saveUpdatedData(String name, String email, String phone, String birthDate, String gender, String address) {
        // Cập nhật dữ liệu vào cơ sở dữ liệu hoặc thực hiện các thao tác lưu trữ
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name_COL, name);
        values.put(Email_COL, email);
        values.put(Phone_COL, phone);
        values.put(Birth_COL, birthDate);
        values.put(Gender_COL, gender);
        values.put(Address_COL, address);
        db.update(TABLE_NAME_CUSTOMER, values, Email_COL + " = ?", new String[]{email});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ORDER, null, null);
        db.close();
    }


    public boolean updateOrderStatus(String userId, Integer newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_COL, newStatus);

        int rowsAffected = db.update(TABLE_NAME_ORDER, values, CUSTOMER_ID_COL + " = ?", new String[]{userId});
        db.close();
        return rowsAffected > 0;
    }





}
