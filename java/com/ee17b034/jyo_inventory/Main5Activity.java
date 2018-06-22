package com.ee17b034.jyo_inventory;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ee17b034.jyo_inventory.sampledata.InventoryDbHelper;
import com.ee17b034.jyo_inventory.sampledata.StockItem;

public class Main5Activity extends AppCompatActivity {

    InventoryDbHelper dbHelper;
    StockCursorAdapter adapter;
    int lastVisibleItem=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        dbHelper= new InventoryDbHelper(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main5Activity.this, Main3Activity.class);
                startActivity(intent);
            }
        });

        final ListView listView =(ListView) findViewById(R.id.list_view);
        Cursor cursor=dbHelper.readStock();

        adapter= new StockCursorAdapter(this,cursor);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {///////////////
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0) return;
                final int currentFirstVisibleItem = view.getFirstVisiblePosition();
                if (currentFirstVisibleItem > lastVisibleItem) {
                    fab.show();
                } else if (currentFirstVisibleItem < lastVisibleItem) {
                    fab.hide();
                }
                lastVisibleItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });



    }

    protected void onResume() {
        super.onResume();
        adapter.swapCursor(dbHelper.readStock());
    }
    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, Main3Activity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }
    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        adapter.swapCursor(dbHelper.readStock());
    }

}
