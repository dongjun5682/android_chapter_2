package com.example.contacts;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.contacts.Index.*;

public class MemberDitail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_ditail);
        Context _this = MemberDitail.this;
        Intent intent = this.getIntent();
        ItemDetail query = new ItemDetail(_this);
        query.seq = String.valueOf(intent.getExtras().getInt("seq"));
        Member m = (Member)new ISupplier() {
            @Override
            public Object get() {
                return query.get();
            }
        }.get();

        MemberPhotoItem photoItem = new MemberPhotoItem(_this);
        photoItem.seq = m.seq + "";
        ImageView photo = findViewById(R.id.profile);
        photo.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(
                                                _this.getPackageName()+":drawable/"
                                                        +((String)new Index.ISupplier() {
                                                    @Override
                                                    public Object get() {
                                                        return photoItem.get();
                                                    }
                                                }.get()),
                                                null,
                                                null),
                                _this.getTheme()
                        )
        );

        TextView name = findViewById(R.id.name);
        name.setText(m.name);
        TextView phone = findViewById(R.id.phone);
        phone.setText(m.phone);
        TextView email = findViewById(R.id.email);
        email.setText(m.email);
        TextView addr = findViewById(R.id.addr);
        addr.setText(m.addr);

        findViewById(R.id.callBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.dialBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.smsBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.emailBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.albumBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.movieBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.mapBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.musicBtn).setOnClickListener(
                (v)->{}
        );
        findViewById(R.id.updateBtn).setOnClickListener(
                (v)->{
                    Intent intent2 = new Intent(_this, MemberUpdate.class);
                    intent2.putExtra("spec",
                            m.seq+","+
                                    m.name+","+
                                    m.pw+","+
                                    m.email+","+
                                    m.phone+","+
                                    m.addr+","+
                                    m.photo
                    );
                    startActivity(intent2);

                });
        findViewById(R.id.listBtn).setOnClickListener(
                (v)->{
                    startActivity(new Intent(_this, MemberList.class));
                }
        );

    }

    private class DetailQeury extends Index.QueryFactory {
        SQLiteOpenHelper helper;

        public DetailQeury(Context _this) {
            super(_this);
            helper = new Index.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class ItemDetail extends DetailQeury {
        String seq;
        public ItemDetail(Context _this) {
            super(_this);
        }

        public Member get() {
            Member m = null;
            Cursor c = getDatabase().rawQuery(String.format(
                    "SELECT * FROM %s WHERE %s LIKE '%s'",MEMBERS,MSEQ,seq), null);
                if (c != null && c.moveToNext()) {
                    m = new Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(Index.MSEQ)));
                    m.name = c.getString(c.getColumnIndex(Index.MNAME));
                    m.phone = c.getString(c.getColumnIndex(Index.MPHONE));
                    m.addr = c.getString(c.getColumnIndex(Index.MADDR));
                    m.email = c.getString(c.getColumnIndex(Index.MEMAIL));
                    m.photo = c.getString(c.getColumnIndex(Index.MPHOTO));
                }
            return m;

        }
    }

    private class MemberPhotoQuery extends Index.QueryFactory {
        SQLiteOpenHelper helper;

        public MemberPhotoQuery(Context _this) {
            super(_this);
            helper = new Index.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase(); //helper가 가져와서 SQLiteDatabse 사용
        }
    }
    private class MemberPhotoItem extends MemberPhotoQuery {
        String seq;
        public MemberPhotoItem(Context _this) {
            super(_this);
        }//index로 가는 안정장치

        public String get() {
            String result = "";
            Cursor c = this.getDatabase().rawQuery(String.format(
                    " SELECT %s FROM %s WHERE %s LIKE %s", Index.MPHOTO, Index.MEMBERS, Index.MSEQ, seq), null);

            if (c != null) {
                if (c.moveToNext()) {
                    result = c.getString(c.getColumnIndex(Index.MPHOTO));
                }
            }
            return result;
        }
    }

}
