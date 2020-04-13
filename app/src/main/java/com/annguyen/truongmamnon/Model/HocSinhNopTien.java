package com.annguyen.truongmamnon.Model;

public class HocSinhNopTien {
    private String maHocSinh;
    private String statusPayment;

    public HocSinhNopTien(String maHocSinh, String statusPayment) {
        this.maHocSinh = maHocSinh;
        this.statusPayment = statusPayment;
    }

    public String getMaHocSinh() {
        return maHocSinh;
    }

    public void setMaHocSinh(String maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }
}
