package com.example.motorshop.datasrc;

import java.util.ArrayList;

public class SanPham {
    private String maSP;
    private String tenSP;
    private int soLuong;
    private int donGia;
    private int hanBH;
    private int hinhAnh;
    private String maNCC;

    public SanPham() { }

    public SanPham(String maSP, String tenSP, int soLuong, int donGia, int hanBH, int hinhAnh, String maNCC) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.hanBH = hanBH;
        this.hinhAnh = hinhAnh;
        this.maNCC = maNCC;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
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

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getHanBH() {
        return hanBH;
    }

    public void setHanBH(int hanBH) {
        this.hanBH = hanBH;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }
}
