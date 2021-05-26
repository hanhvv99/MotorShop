package com.example.motorshop.activity.guarantee;

public class PhieuBaoHanhTemp {
    String maBH,tenSP,ngayBH;
    public PhieuBaoHanhTemp() { }

    public PhieuBaoHanhTemp(String maBH, String tenSP, String ngayBH) {
        this.maBH = maBH;
        this.tenSP = tenSP;
        this.ngayBH = ngayBH;
    }

    public String getMaBH() {
        return this.maBH;
    }

    public void setMaBH(String maBH) {
        this.maBH = maBH;
    }

    public String getTenSP() {
        return this.tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getNgayBH() {
        return this.ngayBH;
    }

    public void setNgayBH(String ngayBH) {
        this.ngayBH = ngayBH;
    }

    @Override
    public String toString() {
        return (this.getMaBH()+","+this.getTenSP()+","+this.getNgayBH());
    }
}
