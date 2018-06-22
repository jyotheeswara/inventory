package com.ee17b034.jyo_inventory.sampledata;

import android.content.ContentValues;
import android.provider.BaseColumns;

public class StockContract {
    public StockContract(){}
    public static final class StockEntry implements BaseColumns{
        public static final String TABLE_NAME= "stock";

        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_PRICE="price";
        public static final String COLUMN_QUANTITY="quantity";

        public static final String CREATE_TABLE_STOCK= "CREATE TABLE "+
                StockContract.StockEntry.TABLE_NAME+"("+
                StockContract.StockEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                StockContract.StockEntry.COLUMN_NAME+" TEXT NOT NULL,"+
                StockContract.StockEntry.COLUMN_PRICE+" TEXT NOT NULL,"+
                StockContract.StockEntry.COLUMN_QUANTITY+" INTEGER NOT NULL DEFAULT 0"+");";


    }
}
