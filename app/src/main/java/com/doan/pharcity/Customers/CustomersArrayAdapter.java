package com.doan.pharcity.Customers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.doan.pharcity.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class CustomersArrayAdapter {

    //Connect to SQLite
    private final SQLiteHelper dbHelper;
    private SQLiteDatabase db;
    private final String[] allColumns = {SQLiteHelper.ID_COL, SQLiteHelper.Name_COL, SQLiteHelper.Email_COL, SQLiteHelper.Phone_COL, SQLiteHelper.Birth_COL, SQLiteHelper.Gender_COL};

    CustomersArrayAdapter(Context context) {dbHelper = new SQLiteHelper(context);}

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() { dbHelper.close();}

    public List<Customers> getAllCustomers() {
        List<Customers> customers = new ArrayList<Customers>();
        Cursor cursor = db.query(SQLiteHelper.TABLE_NAME_CUSTOMER, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
        Customers name = cursorToCustomers(cursor);
        customers.add(name);
        cursor.moveToNext();
        }
        cursor.close();
        return customers;

    }

    private Customers cursorToCustomers(Cursor cursor) {
        Customers customers;
        customers = new Customers();
        customers.setId(cursor.getLong(0));
        customers.setName(cursor.getString(1));
        customers.setEmail(cursor.getString(2));
        customers.setPhone(cursor.getString(3));
        customers.setDateOfBirth(cursor.getString(4));
        customers.setGender(cursor.getString(5));
        return customers;
    }

}
