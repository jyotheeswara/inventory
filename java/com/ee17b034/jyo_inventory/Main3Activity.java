package com.ee17b034.jyo_inventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.text.TextUtils;
import java.lang.Object;
import android.R.string;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;



import com.ee17b034.jyo_inventory.sampledata.InventoryDbHelper;
import com.ee17b034.jyo_inventory.sampledata.StockContract;
import com.ee17b034.jyo_inventory.sampledata.StockItem;

public class Main3Activity extends AppCompatActivity {
    private InventoryDbHelper dbHelper;
    EditText nameEdit;
    EditText priceEdit;
    EditText quantityEdit;
    long currentItemId;
    Button increaseQuant, decreaseQuant;
    Boolean infoItemHasChanged=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //CHECK THIS LATER************
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        nameEdit=(EditText) findViewById(R.id.editText6);
        priceEdit=(EditText) findViewById(R.id.editText7);
        quantityEdit=(EditText) findViewById(R.id.editText8);
        increaseQuant=(Button) findViewById(R.id.button4);
        decreaseQuant=(Button) findViewById(R.id.button5);

        dbHelper=new InventoryDbHelper(this);
        currentItemId=getIntent().getLongExtra("itemId",0);

        if(currentItemId==0){
            setTitle("Add new Item");
        }else {
            setTitle("Edit Item");
            addValuesToEditItem(currentItemId);
        }

        decreaseQuant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractOneToQuantity();
                infoItemHasChanged = true;
            }
        });

        increaseQuant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sumOneToQuantity();
                infoItemHasChanged = true;
            }
        });


    }

    private void subtractOneToQuantity() {
        String previousValueString = quantityEdit.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            quantityEdit.setText(String.valueOf(previousValue - 1));
        }
    }

    private void sumOneToQuantity() {
        String previousValueString = quantityEdit.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        quantityEdit.setText(String.valueOf(previousValue + 1));
    }

    private boolean addItemToDb() {
        boolean isAllOk = true;
        if (!checkIfValueSet(nameEdit, "name")) {
            isAllOk = false;
        }
        if (!checkIfValueSet(priceEdit, "price")) {
            isAllOk = false;
        }
        if (!checkIfValueSet(quantityEdit, "quantity")) {
            isAllOk = false;
        }
        if (!isAllOk) {
            return false;
        }

        if (currentItemId == 0) {
            StockItem item = new StockItem(
                    nameEdit.getText().toString().trim(),
                    priceEdit.getText().toString().trim(),
                    Integer.parseInt(quantityEdit.getText().toString().trim()));

            dbHelper.insertItem(item);
        } else {
            int quantity = Integer.parseInt(quantityEdit.getText().toString().trim());
            dbHelper.updateItem(currentItemId, quantity);
        }
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // save item in DB
                if (!addItemToDb()) {
                    // saying to onOptionsItemSelected that user clicked button
                    return true;
                }
                finish();
                return true;
            case android.R.id.home:
                if (!infoItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard, button,go to parent activity.
                                NavUtils.navigateUpFromSameTask(Main3Activity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            case R.id.action_delete_item:
                // delete one item
                showDeleteConfirmationDialog(currentItemId);
                return true;
            case R.id.action_delete_all_data:
                //delete all data
                showDeleteConfirmationDialog(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
                                          DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("There are unsaved changes");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!infoItemHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog(final long itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to proceed with the delete?");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (itemId == 0) {
                    deleteAllRowsFromTable();
                } else {
                    deleteOneItemFromTable(itemId);
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int deleteAllRowsFromTable() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(StockContract.StockEntry.TABLE_NAME, null, null);
    }

    private int deleteOneItemFromTable(long itemId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = StockContract.StockEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(itemId) };
        int rowsDeleted = database.delete(
                StockContract.StockEntry.TABLE_NAME, selection, selectionArgs);
        return rowsDeleted;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentItemId == 0) {
            MenuItem deleteOneItemMenuItem = menu.findItem(R.id.action_delete_item);
            MenuItem deleteAllMenuItem = menu.findItem(R.id.action_delete_all_data);
            deleteOneItemMenuItem.setVisible(false);
            deleteAllMenuItem.setVisible(false);
        }
        return true;
    }

    private boolean checkIfValueSet(EditText text, String description) {
            if (TextUtils.isEmpty(text.getText())) {
                text.setError("Missing product " + description);
                return false;
            } else {
                text.setError(null);
                return true;
            }
        }
    private void addValuesToEditItem(long itemId) {
        Cursor cursor = dbHelper.readItem(itemId);
        cursor.moveToFirst();
        nameEdit.setText(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_NAME)));
        priceEdit.setText(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_PRICE)));
        quantityEdit.setText(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_QUANTITY)));
        nameEdit.setEnabled(false);
        priceEdit.setEnabled(false);
    }


    }
