package com.example.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.contacts.Index.*;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        Context _this = MemberList.this;
        ListView memberList = findViewById(R.id.memberList);
        ItemList query = new ItemList(_this);

        memberList.setAdapter(
                new MemberAdapter(_this, (ArrayList<Index.Member>)new Index.ISupplier() {
                    @Override
                    public Object get() {
                        return query.get();
                    }
                }.get())
        ); //리스트 그려져있음

        memberList.setOnItemClickListener(
                (AdapterView<?> p,View v, int i,long l)->{
                    Index.Member m = (Index.Member)memberList.getItemAtPosition(i);
                    Intent intent = new Intent(_this,MemberDitail.class);
                    intent.putExtra("seq",m.seq);
                    startActivity(intent);
        });

        memberList.setOnItemLongClickListener(
                (AdapterView<?> p, View v, int i, long l)->{
                    Member m = (Member)memberList.getItemAtPosition(i);
                    new AlertDialog.Builder(_this)
                            .setTitle("DELETE")
                            .setMessage("정말 삭제할까요?")
                            .setPositiveButton(
                                    android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 삭제 쿼리 실행
                                            Toast.makeText(_this,"삭제완료 !!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(_this, MemberList.class));
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    android.R.string.no,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(_this,"삭제취소 !!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                            )
                            .show();
                    return true;
        });


    }
    private class MemberListQuery extends Index.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberListQuery(Context _this) {
            super(_this);
            helper = new Index.SQLiteHelper(_this); //this = index에 있는 디비
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase(); //읽기만 하기 떄문에 리드
        }
    }
    private class ItemList extends MemberListQuery{
        public ItemList(Context _this) {
            super(_this);
        }
        public ArrayList<Member> get(){
            ArrayList<Member> list = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(
                    "SELECT * FROM MEMBER",null);
            Member m = null;
            if(c != null){
                while (c.moveToNext()){
                    m = new Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(Index.MSEQ)));
                    m.name = c.getString(c.getColumnIndex(Index.MNAME));
                    m.pw = c.getString(c.getColumnIndex(Index.MPW));
                    m.email = c.getString(c.getColumnIndex(Index.MEMAIL));
                    m.phone = c.getString(c.getColumnIndex(Index.MPHONE));
                    m.photo = c.getString(c.getColumnIndex(Index.MPHONE));
                    list.add(m);
                }
            }else{
                Toast.makeText(_this, "등록된 회원이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            return list;
        }
    }
    private class MemberAdapter extends BaseAdapter{
        ArrayList<Member> list;
        LayoutInflater inflater;
        Context _this;

        public MemberAdapter(Context _this,ArrayList<Member> list) {
            this.list = list;
            this._this = _this;
            this.inflater = LayoutInflater.from(_this);
        } // Alt + Insert 키

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v == null){
                v = inflater.inflate(R.layout.member_item, null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.profile);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            MemberPhotoItem query = new MemberPhotoItem(_this);
            query.seq = list.get(i).seq+"";

            holder.photo
                    .setImageDrawable(
                            getResources()
                                    .getDrawable(
                                            getResources()
                                                    .getIdentifier(
                                                            _this.getPackageName()+":drawable/"+
                                                            ((String)new ISupplier() {
                                                                @Override
                                                                public Object get() {
                                                                    return query.get();
                                                                }
                                                            }.get()),
                                                            null,
                                                            null),
                                            _this.getTheme()
                                    )
                    );
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }
    static class ViewHolder {
        ImageView photo;
        TextView name, phone;
    }

    private class MemberPhotoQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public MemberPhotoQuery(Context _this) {
            super(_this);
            helper = new SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase(); //helper가 가져와서 SQLiteDatabse 사용
        }
    }

    private class MemberPhotoItem extends MemberPhotoQuery{
        String seq;
        public MemberPhotoItem(Context _this) {
            super(_this);
        }//index로 가는 안정장치
        public String get(){
            String result ="";
            Cursor c = this.getDatabase().rawQuery(String.format(
                    " SELECT %s FROM %s WHERE %s LIKE %s",MPHOTO,MEMBERS,MSEQ,seq), null);

            if(c != null){
                if(c.moveToNext()){
                    result = c.getString(c.getColumnIndex(MPHOTO));
                }
            }
            return result;
        }
    }
}
