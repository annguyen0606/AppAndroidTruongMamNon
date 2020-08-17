package com.annguyen.truongmamnon.Model;

public class ThongTinHocSinhThuTien {
    private String ten;
    private String ngaySinh;
    private String maHs;
    private String numberMoney;
    private int status;
    private byte[] hinhAnh;

    public ThongTinHocSinhThuTien(String ten, String ngaySinh, String maHs, String numberMoney, int status, byte[] hinhAnh) {
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.maHs = maHs;
        this.numberMoney = numberMoney;
        this.status = status;
        this.hinhAnh = hinhAnh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getMaHs() {
        return maHs;
    }

    public void setMaHs(String maHs) {
        this.maHs = maHs;
    }

    public String getNumberMoney() {
        return numberMoney;
    }

    public void setNumberMoney(String numberMoney) {
        this.numberMoney = numberMoney;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
