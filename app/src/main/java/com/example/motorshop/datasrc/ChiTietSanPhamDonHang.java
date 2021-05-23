package com.example.motorshop.datasrc;

import java.io.Serializable;

public class ChiTietSanPhamDonHang extends DonHang {
    private String tenSP;
    private int soLuong;
    private int donGiaBan;

    public ChiTietSanPhamDonHang() {
        super();
    }

    //sua sdtkh thanh cmnd cho hop voi don hang
    public ChiTietSanPhamDonHang(String maDH, String ngayDat, String cmnd, String maNV, String tenSP, int soLuong, int donGiaBan) {
        super(maDH, ngayDat, cmnd, maNV);
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.donGiaBan = donGiaBan;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getDonGiaBan() {
        return donGiaBan;
    }

    public void setDonGiaBan(int donGiaBan) {
        this.donGiaBan = donGiaBan;
    }
}