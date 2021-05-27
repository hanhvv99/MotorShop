package com.example.motorshop.datasrc;

//public class ChiTietSanPhamDonHang extends DonHang {
//    private String maSP;
//    private int soLuong;
//    private int donGiaBan;
//
//    public ChiTietSanPhamDonHang() {
//        super();
//    }
//
//    public ChiTietSanPhamDonHang(String maDH, String ngayDat, String sdtKH, String tenNV, String maSP, int soLuong, int donGiaBan) {
//        super(maDH, ngayDat, sdtKH, tenNV);
//        this.maSP = maSP;
//        this.soLuong = soLuong;
//        this.donGiaBan = donGiaBan;
//    }
//
//    public String getMaSP() {
//        return maSP;
//    }
//
//    public void setMaSP(String maSP) {
//        this.maSP = maSP;
//    }
//
//    public int getSoLuong() {
//        return soLuong;
//    }
//
//    public void setSoLuong(int soLuong) {
//        this.soLuong = soLuong;
//    }
//
//    public int getDonGiaBan() {
//        return donGiaBan;
//    }
//
//    public void setDonGiaBan(int donGiaBan) {
//        this.donGiaBan = donGiaBan;
//    }
//}

public class ChiTietSanPhamDonHang{
    private String maSP;
    private int soLuong;
    private int donGiaBan;

    public ChiTietSanPhamDonHang() { }

    public ChiTietSanPhamDonHang(String maSP, int soLuong, int donGiaBan) {
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGiaBan = donGiaBan;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
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