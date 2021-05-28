package com.example.motorshop.activity.statistic;

public class ThongKeTemp {
    String tenSP;
    int soLuong, giaBan;
    String ngayDat;
    public ThongKeTemp(){}

    public ThongKeTemp(String tenSP, int soLuong, int giaBan, String ngayDat){
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.ngayDat = ngayDat;
    }

    public String getTenSP() {
        return tenSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setGiaBan(int giaBan) {
        this.giaBan = giaBan;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    @Override
    public String toString() {
        return this.getTenSP()+","+this.getSoLuong()+","+this.getGiaBan();
    }
}
