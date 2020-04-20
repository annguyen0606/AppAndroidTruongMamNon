package com.annguyen.truongmamnon.Controller;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import com.annguyen.truongmamnon.Model.HocSinhNopTien;
import com.annguyen.truongmamnon.Model.ThongTinGiaoVien;
import com.annguyen.truongmamnon.Model.ThongTinHocSinh;
import com.annguyen.truongmamnon.Model.ThongTinNguoiThan;
import com.annguyen.truongmamnon.Model.ThongTinThongKe;
import com.annguyen.truongmamnon.Model.TrangThaiHocSinhNopTien;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataProvider {
    private static DataProvider instance;
    private final String ipServer = "125.212.201.52";
    private final String userName = "coneknfc";
    private final String passWord = "Conek@123";
    private final String nameDB = "NFC";

    private DataProvider() { }

    public static DataProvider getInstance(){
        if (instance == null){
            instance = new DataProvider();
        }
        return instance;
    }

    @SuppressLint("NewApi")
    public Connection CONN(String _user, String _pass, String _DB,
                           String _server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";"; // c: // :c
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

    public ThongTinNguoiThan LayThongTinNguoiThan(String query){
        ThongTinNguoiThan thongTinNguoiThan = new ThongTinNguoiThan("ABC","ABC","ABC","ABC","ABC","ABC",new byte[0],"ABC");
        Connection connection;
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            if (resultSet != null && resultSet.next()) {
                thongTinNguoiThan.setMaUID(resultSet.getString("uid"));
                thongTinNguoiThan.setHoTen(resultSet.getString("hoten"));
                thongTinNguoiThan.setDiaChi(resultSet.getString("diachi"));
                thongTinNguoiThan.setQuanHe(resultSet.getString("quanhe"));
                thongTinNguoiThan.setMaHocSinh(resultSet.getString("mahs"));
                thongTinNguoiThan.setSoDienThoai(resultSet.getString("sodienthoai"));
                thongTinNguoiThan.setHinhAnh(resultSet.getBytes("hinhanh"));
                thongTinNguoiThan.setLop(resultSet.getString("malop"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thongTinNguoiThan;
    }

    public ThongTinHocSinh LayThongTinHocSinh(String query){
        ThongTinHocSinh thongTinHocSinh = new ThongTinHocSinh("ABC","ABC","ABC","ABC","ABC","ABC",new byte[0],"ABC");
        Connection connection;
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            if (resultSet != null && resultSet.next()) {
                thongTinHocSinh.setMaHocSinh(resultSet.getString("mahs"));
                thongTinHocSinh.setHoTen(resultSet.getString("hoten"));
                thongTinHocSinh.setNgaySinh(resultSet.getString("ngaysinh"));
                thongTinHocSinh.setLopHocSinh(resultSet.getString("malop"));
                thongTinHocSinh.setGioiTinh(resultSet.getString("gioitinh"));
                thongTinHocSinh.setDiaChi(resultSet.getString("diachi"));
                thongTinHocSinh.setHinhAnh(resultSet.getBytes("hinhanh"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thongTinHocSinh;
    }

    public ThongTinGiaoVien LayThongTinGiaoVien(String query){
        ThongTinGiaoVien thongTinGiaoVien = new ThongTinGiaoVien("ABC","ABC","ABC","ABC","ABC","ABC","ABC","ABC",5);
        Connection connection;
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            if (resultSet != null && resultSet.next()) {
                thongTinGiaoVien.setMaGiaoVien(resultSet.getString("magv"));
                thongTinGiaoVien.setTenGiaoVien(resultSet.getString("ten"));
                thongTinGiaoVien.setDiaChi(resultSet.getString("diachi"));
                thongTinGiaoVien.setSoDienThoai(resultSet.getString("sodienthoai"));
                thongTinGiaoVien.setUserName(resultSet.getString("username"));
                thongTinGiaoVien.setPassWord(resultSet.getString("password"));
                thongTinGiaoVien.setPortName(resultSet.getString("port"));
                thongTinGiaoVien.setLop(resultSet.getString("malop"));
                thongTinGiaoVien.setLoaiTaiKhoan(resultSet.getInt("loaitaikhoan"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thongTinGiaoVien;
    }

    public ArrayList<ThongTinHocSinh> LayDanhSachThongTinHocSinh(String query){
        ArrayList<ThongTinHocSinh> danhSachThongTinHocSinh = new ArrayList<ThongTinHocSinh>();

        Connection connection;
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                ThongTinHocSinh thongTinHocSinh = new ThongTinHocSinh("ABC","ABC","ABC","ABC","ABC","ABC",new byte[0],"ABC");
                thongTinHocSinh.setMaHocSinh(resultSet.getString("mahs"));
                thongTinHocSinh.setHoTen(resultSet.getString("hoten"));
                thongTinHocSinh.setNgaySinh(resultSet.getString("ngaysinh"));
                thongTinHocSinh.setLopHocSinh(resultSet.getString("malop"));
                thongTinHocSinh.setGioiTinh(resultSet.getString("gioitinh"));
                thongTinHocSinh.setDiaChi(resultSet.getString("diachi"));
                thongTinHocSinh.setHinhAnh(resultSet.getBytes("hinhanh"));
                thongTinHocSinh.setMaGiaoVien(resultSet.getString("magv"));
                danhSachThongTinHocSinh.add(thongTinHocSinh);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachThongTinHocSinh;
    }

    public ArrayList<ThongTinNguoiThan> LayDanhSachThongTinNguoiThan(String query){
        ArrayList<ThongTinNguoiThan> danhSachThongTinNguoiThan = new ArrayList<ThongTinNguoiThan>();

        Connection connection;
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                ThongTinNguoiThan thongTinNguoiThan = new ThongTinNguoiThan("ABC","ABC","ABC","ABC","ABC","ABC",new byte[0],"ABC");
                thongTinNguoiThan.setMaUID(resultSet.getString("uid"));
                thongTinNguoiThan.setHoTen(resultSet.getString("hoten"));
                thongTinNguoiThan.setDiaChi(resultSet.getString("diachi"));
                thongTinNguoiThan.setQuanHe(resultSet.getString("quanhe"));
                thongTinNguoiThan.setMaHocSinh(resultSet.getString("mahs"));
                thongTinNguoiThan.setSoDienThoai(resultSet.getString("sodienthoai"));
                thongTinNguoiThan.setHinhAnh(resultSet.getBytes("hinhanh"));
                thongTinNguoiThan.setLop(resultSet.getString("malop"));
                danhSachThongTinNguoiThan.add(thongTinNguoiThan);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachThongTinNguoiThan;
    }

    public int InsertThongTinHocSinh(String maHs, String hoTen, String ngaySinh, String lop, String gioiTinh, String diaChi, byte[] hinhAnh, String magv){
        int resultInsert = 0;
        Connection connection;
        connection = CONN(userName,passWord,nameDB,ipServer);
        String querryInsert = "insert into ThongTinHocSinh(mahs, hoten, ngaysinh, malop, gioitinh, diachi, magv, hinhanh) values(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(querryInsert);
            preparedStatement.setString(1,maHs);
            preparedStatement.setString(2,hoTen);
            preparedStatement.setString(3,ngaySinh);
            preparedStatement.setString(4,lop);
            preparedStatement.setString(5,gioiTinh);
            preparedStatement.setString(6,diaChi);
            preparedStatement.setString(7,magv);
            preparedStatement.setBytes(8,hinhAnh);
            resultInsert = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInsert;
    }

    public int InsertThongTinPhuHuynh(String uid, String hoTen, String diaChi, String quanHe, String mahs, String sodienthoai, byte[] hinhAnh, String lop){
        int resultInsert = 0;
        Connection connection;
        connection = CONN(userName,passWord,nameDB,ipServer);
        String querryInsert = "insert into ThongTinNguoiThan(uid, hoten, diachi, quanhe, mahs, sodienthoai, malop, hinhanh) values(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(querryInsert);
            preparedStatement.setString(1,uid);
            preparedStatement.setString(2,hoTen);
            preparedStatement.setString(3,diaChi);
            preparedStatement.setString(4,quanHe);
            preparedStatement.setString(5,mahs);
            preparedStatement.setString(6,sodienthoai);
            preparedStatement.setString(7,lop);
            preparedStatement.setBytes(8,hinhAnh);
            resultInsert = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInsert;
    }

    public int InsertThoiGianDonCon(String uid, String mahs, String lop, String ngay, int phut){
        int resultInsert = 0;
        Connection connection;
        connection = CONN(userName,passWord,nameDB,ipServer);
        String querryInsert = "insert into ThoiGianDonCon(uid, mahs, malop, ngay, phut) values(?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(querryInsert);
            preparedStatement.setString(1,uid);
            preparedStatement.setString(2,mahs);
            preparedStatement.setString(3,lop);
            preparedStatement.setString(4,ngay);
            preparedStatement.setInt(5,phut);
            resultInsert = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInsert;
    }


    public int GetLateMinute(String uid, String month){
        int lateMinute = 0;
        Connection connection;
        ResultSet resultSet;
        String query = "SELECT phut FROM ThoiGianDonCon WHERE uid = '" +uid+"' and ngay LIKE '"+month+"%'";
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                lateMinute += resultSet.getInt("phut");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lateMinute;
    }

    public int GetLateMinuteFromMaHs(String mahs, String month){
        int lateMinute = 0;
        Connection connection;
        ResultSet resultSet;
        String query = "SELECT phut FROM ThoiGianDonCon WHERE mahs = '" +mahs+"' and ngay LIKE '%"+month+"%'";
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                lateMinute += resultSet.getInt("phut");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lateMinute;
    }

    public String CheckConfirmTakeStudent(String uid, String ngay){
        String uidTemp = "";
        Connection connection;
        ResultSet resultSet;
        String query = "SELECT uid FROM ThoiGianDonCon WHERE mahs = '" +uid+"' and ngay LIKE '"+ngay+"%'";
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                uidTemp = resultSet.getString("uid");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uidTemp;
    }

    public String LayMaTruongTruong(String malop){
        String matruong = "";
        Connection connection;
        ResultSet resultSet;
        String query = "SELECT matruong FROM ThongTinLop WHERE malop = '" +malop+"'";
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            if (resultSet != null && resultSet.next()) {
                matruong = resultSet.getString("matruong");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matruong;
    }

    public ArrayList<String> LayDanhSachLop(String maTruong){
        Connection connection;
        ArrayList<String> arrayDanhSachLop = new ArrayList<>();
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT malop FROM ThongTinLop WHERE matruong = '"+maTruong+"'";
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                arrayDanhSachLop.add(resultSet.getString("malop"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayDanhSachLop;
    }

    public ArrayList<TrangThaiHocSinhNopTien> LayDanhSachHocSinhNopTien(String maLop, String ngayThang){
        Connection connection;
        ArrayList<TrangThaiHocSinhNopTien> arrayDanhSachNopTien = new ArrayList<>();
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT mahs,sotien,trangthai FROM ThongTinNopTien WHERE malop = '"+maLop+"' and thang LIKE'%"+ngayThang+"%'";
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                TrangThaiHocSinhNopTien hocSinhNopTien = new TrangThaiHocSinhNopTien("ABC","0","5");
                hocSinhNopTien.setMaHocSinh(resultSet.getString("mahs"));
                hocSinhNopTien.setTrangThaiThu(resultSet.getString("trangthai"));
                hocSinhNopTien.setSoTien(resultSet.getString("sotien"));
                arrayDanhSachNopTien.add(hocSinhNopTien);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayDanhSachNopTien;
    }

    public ArrayList<TrangThaiHocSinhNopTien> CheckConfirmPayment(String mahs, String ngay){
        ArrayList<TrangThaiHocSinhNopTien> trangThaiHocSinhNopTiens = new ArrayList<>();
        Connection connection;
        ResultSet resultSet;
        String query = "SELECT mahs,sotien,trangthai FROM ThongTinNopTien WHERE mahs = '" +mahs+"' and thang LIKE '%"+ngay+"%'";
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet != null && resultSet.next()) {
                TrangThaiHocSinhNopTien trangThaiHocSinhNopTien = new TrangThaiHocSinhNopTien("ABC","ABC","ABC");
                trangThaiHocSinhNopTien.setTrangThaiThu(resultSet.getString("trangthai"));
                trangThaiHocSinhNopTien.setMaHocSinh(resultSet.getString("mahs"));
                trangThaiHocSinhNopTien.setSoTien(resultSet.getString("sotien"));
                trangThaiHocSinhNopTiens.add(trangThaiHocSinhNopTien);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trangThaiHocSinhNopTiens;
    }
    public int InsertConfirmPayment(String maHs, String ngayThang){
        int resultInsert = 0;
        Connection connection;
        connection = CONN(userName,passWord,nameDB,ipServer);
        String querryInsert = "UPDATE ThongTinNopTien SET trangthai = '1' WHERE mahs = '" +maHs+"'and thang like '%"+ngayThang+"%'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(querryInsert);
            resultInsert = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultInsert;
    }
    public void ClearThongTin(String query){
        Connection connection;
        ResultSet resultSet;
        connection = CONN(userName,passWord,nameDB,ipServer);
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
