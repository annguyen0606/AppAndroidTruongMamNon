package com.annguyen.truongmamnon.Model;

public class ThongTinHocSinh {
    private String hoTen;
    private String diaChi;
    private String gioiTinh;
    private String maHocSinh;
    private String ngaySinh;
    private String lopHocSinh;
    private byte[] hinhAnh;
    private String maGiaoVien;

    public ThongTinHocSinh(String hoTen, String diaChi, String gioiTinh, String maHocSinh, String ngaySinh, String lopHocSinh, byte[] hinhAnh, String maGiaoVien) {
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.maHocSinh = maHocSinh;
        this.ngaySinh = ngaySinh;
        this.lopHocSinh = lopHocSinh;
        this.hinhAnh = hinhAnh;
        this.maGiaoVien = maGiaoVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMaHocSinh() {
        return maHocSinh;
    }

    public void setMaHocSinh(String maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getLopHocSinh() {
        return lopHocSinh;
    }

    public void setLopHocSinh(String lopHocSinh) {
        this.lopHocSinh = lopHocSinh;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMaGiaoVien() {
        return maGiaoVien;
    }

    public void setMaGiaoVien(String maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }
}
