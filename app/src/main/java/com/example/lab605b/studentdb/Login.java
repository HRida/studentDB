package com.example.lab605b.studentdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText Username,Password;
    Button Login;
    SQLiteDatabase dbL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText) findViewById(R.id.editUsername);
        Password = (EditText) findViewById(R.id.editPassword);

        Login = (Button) findViewById(R.id.btnLogin);

        dbL = openOrCreateDatabase("LoginDB", Context.MODE_PRIVATE, null);
        dbL.execSQL("CREATE TABLE IF NOT EXISTS login(Username VARCHAR, Password VARCHAR);");

        String QueryInsert = "INSERT INTO login VALUES('HRida','123');";
        dbL.execSQL(QueryInsert);

        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void open(View view){

        String QuerySelectUsername = "SELECT * FROM login where Username='"+Username.getText()+"'",
               QuerySelectPassword = "SELECT * FROM login where Password='"+Password.getText()+"'";


        if(Username.getText().toString().trim().length() == 0
                || Login.getText().toString().trim().length() == 0) {
            showMessage("Error", "Please enter all values");
            return;
        }
        Cursor c = dbL.rawQuery(QuerySelectUsername, null);
        Cursor d = dbL.rawQuery(QuerySelectPassword, null);
        if(c.getCount()>0 && d.getCount()>0)
        {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else
        {
            showMessage("Error", "Username or Password is incorrect");
            clearText();
            return;
        }
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void clearText(){
        Username.setText("");
        Password.setText("");
        Username.requestFocus();
    }
}