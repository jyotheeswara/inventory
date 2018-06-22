package com.ee17b034.jyo_inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button bl = (Button) findViewById(R.id.button);
        Button br = (Button) findViewById(R.id.button2);
        Button b6 = (Button) findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(MainActivity.this,Main5Activity.class);
                startActivity(intent5);
            }
        });
        bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1,str2;
                EditText Login=(EditText) findViewById(R.id.editText);
                EditText Password=(EditText) findViewById(R.id.editText2);

                str1=Login.getText().toString();
                str2=Password.getText().toString();

                Database dm=new Database(MainActivity.this);
                SQLiteDatabase db =dm.getReadableDatabase();
                String projection[]={"name","emailId","password"};
                Cursor c=db.query("login",projection,null,null,null,null,null);
                Log.d("COUNT", Arrays.toString(c.getColumnNames()));

                c.moveToFirst();
                Log.d("first",c.getString(1));
                while (!c.isAfterLast()){
                    if(c.getString(1).equals(str1)){
                        Log.d(str1, c.getString(0));
                        position =c.getPosition();
                        break;
                    }
                    else {
                        Log.d(str1,c.getString(0));
                        c.moveToNext();
                    }
                }
                c.moveToPosition(position);
                String username=c.getString(0);
                Log.d("name",username);
                String password=c.getString(2);
                Log.d("pass",password);
                if(str1.equals(username)&&str2.equals(password)){
                    Toast.makeText(getApplicationContext(),"successful login",Toast.LENGTH_LONG).show();
                    Intent intent4 = new Intent(MainActivity.this,Main5Activity.class);
                    startActivity(intent4);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Invalid Username or password",Toast.LENGTH_LONG).show();
                }

            }
        });
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent4);
            }
        });

    }

}
