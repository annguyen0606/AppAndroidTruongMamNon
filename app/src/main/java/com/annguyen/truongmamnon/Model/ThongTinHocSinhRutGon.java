package com.annguyen.truongmamnon.Model;

public class ThongTinHocSinhRutGon {
    private String ten;
    private String maHs;
    private String diaChi;
    private String maUid;
    private int status;
    private byte[] hinhAnh;

    public ThongTinHocSinhRutGon(String ten, String maHs, String diaChi, String maUid, int status, byte[] hinhAnh) {
        this.ten = ten;
        this.maHs = maHs;
        this.diaChi = diaChi;
        this.maUid = maUid;
        this.status = status;
        this.hinhAnh = hinhAnh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMaHs() {
        return maHs;
    }

    public void setMaHs(String maHs) {
        this.maHs = maHs;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaUid() {
        return maUid;
    }

    public void setMaUid(String maUid) {
        this.maUid = maUid;
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
