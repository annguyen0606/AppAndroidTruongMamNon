package com.annguyen.truongmamnon.Model;

public class ThongTinNguoiThan {
    private String maUID;
    private String hoTen;
    private String diaChi;
    private String quanHe;
    private String maHocSinh;
    private String soDienThoai;
    private byte[] hinhAnh;
    private String lop;

    public ThongTinNguoiThan(String maUID, String hoTen, String diaChi, String quanHe, String maHocSinh, String soDienThoai, byte[] hinhAnh, String lop) {
        this.maUID = maUID;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.quanHe = quanHe;
        this.maHocSinh = maHocSinh;
        this.soDienThoai = soDienThoai;
        this.hinhAnh = hinhAnh;
        this.lop = lop;
    }

    public String getMaUID() {
        return maUID;
    }

    public void setMaUID(String maUID) {
        this.maUID = maUID;
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

    public String getQuanHe() {
        return quanHe;
    }

    public void setQuanHe(String quanHe) {
        this.quanHe = quanHe;
    }

    public String getMaHocSinh() {
        return maHocSinh;
    }

    public void setMaHocSinh(String maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }
}
