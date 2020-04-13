package com.annguyen.truongmamnon.Model;

public class ThongTinGiaoVien {
    private String maGiaoVien;
    private String tenGiaoVien;
    private String diaChi;
    private String soDienThoai;
    private String userName;
    private String passWord;
    private String portName;
    private String lop;
    private Integer loaiTaiKhoan;

    public ThongTinGiaoVien(String maGiaoVien, String tenGiaoVien, String diaChi, String soDienThoai, String userName, String passWord, String portName, String lop, Integer loaiTaiKhoan) {
        this.maGiaoVien = maGiaoVien;
        this.tenGiaoVien = tenGiaoVien;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.userName = userName;
        this.passWord = passWord;
        this.portName = portName;
        this.lop = lop;
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public String getMaGiaoVien() {
        return maGiaoVien;
    }

    public void setMaGiaoVien(String maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }

    public String getTenGiaoVien() {
        return tenGiaoVien;
    }

    public void setTenGiaoVien(String tenGiaoVien) {
        this.tenGiaoVien = tenGiaoVien;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public Integer getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(Integer loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }
}
