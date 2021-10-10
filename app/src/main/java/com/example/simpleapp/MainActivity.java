package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected EditText editName, editPhoneNum, editEmail;
    protected Button btnInsert;
    protected ListView simpleList;
    protected DBActivity _db;//todo add a constructor
    public MainActivity(DBActivity db){
        _db= db;
    }

    public void FillListView()throws Exception{
        final ArrayList<String> listResult = new ArrayList<String>();
        _db.selectSQL("SELECT * FROM CONTACTS ORDER BY NAME",null,(id, name, phoneName, email) -> {
            listResult.add(id + "\t" + name+ "\t" +phoneName+ "\t" +email+ "\n");
        });
        simpleList.clearChoices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_listview,
                R.id.textView,
                listResult
        );
        simpleList.setAdapter(arrayAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhoneNum = findViewById(R.id.editPhoneNum);
        btnInsert = findViewById(R.id.btnInsert);
        simpleList = findViewById(R.id.simpleList);
        try {
            _db.InitDB();
            FillListView();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        btnInsert.setOnClickListener(view -> {
            try {
                _db.ExecSQL("INSERT INTO CONTACTS(NAME, PHONE_NUM, EMAIL) VALUES(?,?,?)",
                        new Object[]{
                        editName.getText().toString(),
                        editPhoneNum.getText().toString(),
                        editEmail.getText().toString()}, () -> Toast.makeText(getApplicationContext(),"Record inserted", Toast.LENGTH_LONG).show());
                FillListView();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Insert value failed "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}