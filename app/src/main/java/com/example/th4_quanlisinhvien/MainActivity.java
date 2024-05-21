package com.example.th4_quanlisinhvien;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.th4_quanlisinhvien.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList=new ArrayList<>();
        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        binding.lv.setAdapter(arrayAdapter);
        mydatabase=openOrCreateDatabase("qlsinhvien.db",MODE_PRIVATE,null);
        try {
            String sql = "CREATE TABLE tbllop(malop TEXT primary key, tenlop TEXT, siso INTENGER)";
            mydatabase.execSQL(sql);
        }catch (Exception e){
            Log.e("Erorr","Table đã tồn tại");
        }
        binding.insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop= binding.edtMalop.getText().toString();
                String tenlop= binding.edtTenlop.getText().toString();
                int siso= Integer.parseInt(binding.edtSiso.getText().toString());

                ContentValues contentValues= new ContentValues();
                contentValues.put("malop",malop);
                contentValues.put("tenlop",tenlop);
                contentValues.put("siso",siso);

                String msg= "";
                if(mydatabase.insert("tbllop",null,contentValues)== -1){
                    msg="Fail to insert record";
                }else {
                    msg="Insert record successfully";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop= binding.edtMalop.getText().toString();
                int n= mydatabase.delete("tbllop","malop = ?",new String[] {malop});
                String msg="";
                if(n==0){
                    msg="No record to delete";
                }else {
                    msg= n+ "record is deleted";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop= binding.edtMalop.getText().toString();
                int siso= Integer.parseInt(binding.edtSiso.getText().toString());
                ContentValues contentValues = new ContentValues();
                contentValues.put("siso",siso);
                int n= mydatabase.delete("tbllop","malop = ?",new String[] {malop});
                String msg="";
                if(n==0){
                    msg="No record to delete";
                }else {
                    msg= n+ "record is deleted";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
        binding.edtQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                Cursor cursor = mydatabase.query("tbllop",null,null,null,null,null,null);
                cursor.moveToNext();
                String data="";
                while (cursor.isAfterLast()== false){
                    data= cursor.getString(0) +"-"+cursor.getString(1)+"-"+cursor.getString(2);
                    cursor.moveToNext();
                    arrayList.add(data);
                }
                cursor.close();
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }
}