package com.annguyen.truongmamnon.Model;

public class IconHomeActivity {
    private String Ten;
    private int HinhAnh;

    public IconHomeActivity(String ten, int hinhAnh) {
        Ten = ten;
        HinhAnh = hinhAnh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public int getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
