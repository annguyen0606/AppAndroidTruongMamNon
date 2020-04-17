package com.annguyen.truongmamnon.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class DatabaseSQLite extends SQLiteOpenHelper {
    public DatabaseSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // truy van khong tra lai ket qua
    public void QuerryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void InsertPayment(String mahs, String malop, Integer status){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ThongTinNopTien VALUES(null,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,mahs);
        statement.bindString(2,malop);
        statement.bindLong(3,status);
        statement.executeInsert();
    }
    public void InsertThongTinNguoiThan(String uid, String ten, String diachi, String quanhe, String maHs, String soDienThoai, byte[] HinhAnh, String lop){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ThongTinNguoiThan VALUES(null,?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,uid);
        statement.bindString(2,ten);
        statement.bindString(3,diachi);
        statement.bindString(4,quanhe);
        statement.bindString(5,maHs);
        statement.bindString(6,soDienThoai);
        statement.bindBlob(7,HinhAnh);
        statement.bindString(8,lop);
        statement.executeInsert();
    }

    public void InsertThongTinGiaoVien(String magv, String ten, String diachi, String sodienthoai, String lop, Integer loaitaikhoan){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ThongTinGiaoVien VALUES(null,?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,magv);
        statement.bindString(2,ten);
        statement.bindString(3,diachi);
        statement.bindString(4,sodienthoai);
        statement.bindString(5,lop);
        statement.bindLong(6,loaitaikhoan);
        statement.executeInsert();
    }

    public void InsertThongTinHocSinh(String maHs, String ten, String ngaysinh, String lop, String gioitinh, String diachi, byte[] HinhAnh, String magv){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ThongTinHocSinh VALUES(null,?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,maHs);
        statement.bindString(2,ten);
        statement.bindString(3,ngaysinh);
        statement.bindString(4,lop);
        statement.bindString(5,gioitinh);
        statement.bindString(6,diachi);
        statement.bindBlob(7,HinhAnh);
        statement.bindString(8,magv);
        statement.executeInsert();
    }

    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
