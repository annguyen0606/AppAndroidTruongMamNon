package com.annguyen.truongmamnon.Model;

public class TrangThaiHocSinhNopTien {
    private String maHocSinh;
    private String soTien;
    private String trangThaiThu;
    private String soTienReal;

    public TrangThaiHocSinhNopTien(String maHocSinh, String soTien, String trangThaiThu, String soTienReal) {
        this.maHocSinh = maHocSinh;
        this.soTien = soTien;
        this.trangThaiThu = trangThaiThu;
        this.soTienReal = soTienReal;
    }

    public String getMaHocSinh() {
        return maHocSinh;
    }

    public void setMaHocSinh(String maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public String getSoTien() {
        return soTien;
    }

    public void setSoTien(String soTien) {
        this.soTien = soTien;
    }

    public String getTrangThaiThu() {
        return trangThaiThu;
    }

    public void setTrangThaiThu(String trangThaiThu) {
        this.trangThaiThu = trangThaiThu;
    }

    public String getSoTienReal() {
        return soTienReal;
    }

    public void setSoTienReal(String soTienReal) {
        this.soTienReal = soTienReal;
    }
}
