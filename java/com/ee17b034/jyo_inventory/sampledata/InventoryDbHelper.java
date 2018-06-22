package com.ee17b034.jyo_inventory.sampledata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDbHelper extends SQLiteOpenHelper {

    public InventoryDbHelper(Context context) {
        super(context, "inventory.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StockContract.StockEntry.CREATE_TABLE_STOCK);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){

    }

    public void insertItem(StockItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StockContract.StockEntry.COLUMN_NAME, item.getProductName());
        values.put(StockContract.StockEntry.COLUMN_PRICE, item.getPrice());
        values.put(StockContract.StockEntry.COLUMN_QUANTITY, item.getQuantity());
        long id = db.insert(StockContract.StockEntry.TABLE_NAME, null, values);
    }
    public Cursor readStock() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                StockContract.StockEntry._ID,
                StockContract.StockEntry.COLUMN_NAME,
                StockContract.StockEntry.COLUMN_PRICE,
                StockContract.StockEntry.COLUMN_QUANTITY
        };
        Cursor cursor = db.query(
                StockContract.StockEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor readItem(long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                StockContract.StockEntry._ID,
                StockContract.StockEntry.COLUMN_NAME,
                StockContract.StockEntry.COLUMN_PRICE,
                StockContract.StockEntry.COLUMN_QUANTITY
        };
        String selection= StockContract.StockEntry._ID+"=?";
        String[] selectionArgs=new String[]{String.valueOf(itemId)};
        Cursor cursor = db.query(
                StockContract.StockEntry.TABLE_NAME,projection,selection,selectionArgs,
                null,null,null
        );
        return cursor;
    }
    public void updateItem(long currentItemId, int quantity){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values=new ContentValues();

        values.put(StockContract.StockEntry.COLUMN_QUANTITY,quantity);
        String selection= StockContract.StockEntry._ID+"=?";
        String[] selectionArgs=new String[]{String.valueOf(currentItemId)};
        db.update(StockContract.StockEntry.TABLE_NAME,values,selection,selectionArgs);

    }
    public void sellOneItem(long itemId, int quantity){
        SQLiteDatabase db = getReadableDatabase();
        int newQuantity = 0;
        if(quantity>0){
            newQuantity=quantity-1;
        }
        ContentValues values=new ContentValues();
        values.put(StockContract.StockEntry.COLUMN_QUANTITY,newQuantity);
        String selection= StockContract.StockEntry._ID+"=?";
        String[] selectionArgs=new String[]{String.valueOf(itemId)};
        db.update(StockContract.StockEntry.TABLE_NAME,values,selection,selectionArgs);

    }

}
