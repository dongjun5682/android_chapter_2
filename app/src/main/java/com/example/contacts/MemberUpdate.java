package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.contacts.Index.*;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        Context _this = MemberUpdate.this;

        String[] spec = getIntent()
                .getStringExtra("spec")
                .split(",")
                ;
        ImageView photo = findViewById(R.id.profile);
        photo.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(
                                                _this.getPackageName()+":drawable/"
                                                        +spec[6],
                                                null,
                                                null),
                                _this.getTheme()
                        )
        );
        EditText name = findViewById(R.id.name);
        name.setText(spec[1]);
        EditText changePhone = findViewById(R.id.changePhone);
        changePhone.setText(spec[4]);
        EditText changeEmail = findViewById(R.id.changeEmail);
        changeEmail.setText(spec[3]);
        EditText changeAddress = findViewById(R.id.changeAddress);
        changeAddress.setText(spec[5]);

        findViewById(R.id.updateBtn).setOnClickListener((v)->{

        });

        findViewById(R.id.cancelBtn).setOnClickListener((v)->{
            Intent intent = new Intent(_this, MemberDitail.class);
            intent.putExtra("seq",spec[0]);
            startActivity(intent);
        });
    }

    private class UpdateQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public UpdateQuery(Context _this) {
            super(_this);
            helper = new SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemUpdate extends UpdateQuery{

        public ItemUpdate(Context _this) {
            super(_this);
        }

    }
}
