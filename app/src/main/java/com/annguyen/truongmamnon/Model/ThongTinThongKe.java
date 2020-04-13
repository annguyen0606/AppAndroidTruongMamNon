package com.annguyen.truongmamnon.Model;

public class ThongTinThongKe {
    private String maHS;
    private String hoTen;
    int thoiGian;
    private String money;
    private Integer statusPayment;

    public ThongTinThongKe(String maHS, String hoTen, int thoiGian, String money, Integer statusPayment) {
        this.maHS = maHS;
        this.hoTen = hoTen;
        this.thoiGian = thoiGian;
        this.money = money;
        this.statusPayment = statusPayment;
    }

    public String getMaHS() {
        return maHS;
    }

    public void setMaHS(String maHS) {
        this.maHS = maHS;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(int thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(Integer statusPayment) {
        this.statusPayment = statusPayment;
    }
}
