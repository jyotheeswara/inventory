package com.ee17b034.jyo_inventory;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ee17b034.jyo_inventory.sampledata.StockContract;

public class StockCursorAdapter extends CursorAdapter {

    private final Main5Activity activity;

    public StockCursorAdapter(Main5Activity context, Cursor c){
        super(context,c,0);
        this.activity=context;
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.activity_main4, viewGroup, false);
    }

    public void bindView(View view, final Context context,final Cursor cursor){
        TextView nameTextView=(TextView) view.findViewById(R.id.product_name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);

        String name = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_QUANTITY));
        String price = "$ "+cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_PRICE));
        final long id = cursor.getLong(cursor.getColumnIndex(StockContract.StockEntry._ID));

        nameTextView.setText(name);
        quantityTextView.setText(String.valueOf(quantity));
        priceTextView.setText(price);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.clickOnViewItem(id);
            }
        });
    }
}
