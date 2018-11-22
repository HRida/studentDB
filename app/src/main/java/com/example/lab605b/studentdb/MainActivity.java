package com.example.lab605b.studentdb;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.app.AlertDialog.Builder;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    EditText ID,Name,Marks;
    Button Add,Delete,Modify,View,ViewAll,ShowInfo;
    SQLiteDatabase db;
    // Called when the activity is first created.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ID = (EditText) findViewById(R.id.editID);
        Name = (EditText) findViewById(R.id.editName);
        Marks = (EditText) findViewById(R.id.editMarks);

        Add = (Button) findViewById(R.id.btnAdd);
        Delete = (Button) findViewById(R.id.btnDelete);
        Modify = (Button) findViewById(R.id.btnModify);
        View = (Button) findViewById(R.id.btnView);
        ViewAll = (Button) findViewById(R.id.btnViewAll);
        ShowInfo = (Button) findViewById(R.id.btnShowInfo);

        Add.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Modify.setOnClickListener(this);
        View.setOnClickListener(this);
        ViewAll.setOnClickListener(this);
        ShowInfo.setOnClickListener(this);

        /* In the below code, the openOrCreateDatabase() function is used to open
         * the StudentDB database if it exists or create a new database if it doesn't exist
         * The first parameter of this function specifies the name of the database to
         * be opened or created the second parameter, Context.MODE_PRIVATE indicates that
         * the database file can only be accessed by the calling application
         * or all applications sharing the same user ID. The third parameter is a Cursor
         * factory object which can be left null if no required*/

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(ID VARCHAR, Name VARCHAR,Marks VARCHAR);");
    }

    public void showMessage(String title, String message){
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void clearText(){
        ID.setText("");
        Name.setText("");
        Marks.setText("");
        ID.requestFocus();
    }

    public void onClick(View v) {

        String QuerySelectID = "SELECT * FROM student where ID='"+ID.getText()+"'",
               QueryDeleteID = "DELETE FROM student WHERE ID='"+ ID.getText()+"'",
               QueryInsert = "INSERT INTO student VALUES('"+ID.getText()+"','"+Name.getText()+"','"+Marks.getText()+"');",
               QuerySelectAll ="SELECT * FROM student",
               QueryUpdate ="UPDATE student SET Name='"+Name.getText()+"',Marks='"+Marks.getText()+"'WHERE ID='"+ID.getText()+"'";


        if(v == Add)
        {
            Cursor c = db.rawQuery(QuerySelectID,null );
            if(c.getCount()>0)
            {
                showMessage("Error : User Already Registered ", "The username is already registered");
                return;
            }
            if(ID.getText().toString().trim().length() == 0
                    || Name.getText().toString().trim().length() == 0
                    || Marks.getText().toString().trim().length() == 0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL(QueryInsert);
            showMessage("Success", "Record added");
            clearText();
        }
        if(v == ViewAll)
        {
            Cursor c = db.rawQuery(QuerySelectAll, null);
            if(c.getCount() == 0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("ID: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Marks: "+c.getString(2)+"\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
        if(v == View)
        {
            if(ID.getText().toString().trim().length() == 0)
            {
                showMessage("Error", "Please enter ID");
                return;
            }
            Cursor c = db.rawQuery(QuerySelectID, null);
            if(c.moveToFirst())
            {
                Name.setText(c.getString(1));
                Name.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid ID");
                clearText();
            }
        }
        if(v == Delete)
        {
            if(ID.getText().toString().trim().length() == 0)
            {
                showMessage("Error", "Please enter ID");
                return;
            }
            Cursor c = db.rawQuery(QuerySelectID, null);
            if(c.getCount() == 0)
            {
                showMessage("Error", "No records found");
                return;
            }
            if(c.moveToFirst())
            {
                db.execSQL(QueryDeleteID);
                showMessage("Success", "Record Deleted");
            }
            else
            {
                showMessage("Error", "Invalid ID");
                clearText();
            }
        }
        if(v == Modify)
        {
            if(ID.getText().toString().trim().length() == 0)
            {
                showMessage("Error", "Please enter ID");
                return;
            }
            Cursor c = db.rawQuery(QuerySelectID, null);
            if(c.moveToFirst())
            {
                db.execSQL(QueryUpdate);
                showMessage("Success", "Record Modified");
            }
            else
            {
                showMessage("Error", "Invalid ID");
            }
            clearText();
        }
        if(v == ShowInfo)
        {
            showMessage("Student Management Application", "Developed by HRida");
        }
    }
}


