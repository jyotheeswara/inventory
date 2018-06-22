package com.ee17b034.jyo_inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button b1=(Button) findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Name =(EditText) findViewById(R.id.editText3);
                String name= Name.getText().toString();
                EditText Email =(EditText) findViewById(R.id.editText4);
                String eMail= Email.getText().toString();
                EditText Password =(EditText) findViewById(R.id.editText5);
                String password= Password.getText().toString();

                Database dm = new Database(Main2Activity.this);
                SQLiteDatabase db = dm.getWritableDatabase();
                ContentValues values= new ContentValues();
                values.put("name",name);
                values.put("emailId",eMail);
                values.put("password",password);
                long row = db.insert("login",null,values);
                Toast.makeText(getApplicationContext(),"successful register",Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent2);
            }
        });

    }
}
