package com.example.motorshop.activity.guarantee;

import com.example.motorshop.datasrc.BaoHanh;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;


abstract class ChiTietBH extends BaoHanh {
    protected static String maBH;
    protected static String ngayBH;

    public ChiTietBH() { }

    public ChiTietBH(String maBH, String maDH, String ngayBH, String maNV) {
        super(maBH, maDH, ngayBH, maNV);
        this.maBH = maBH;
        this.ngayBH = ngayBH;
    }

}

abstract class DSSanPhamBH extends DanhSachSanPhamBaoHanh {
    protected static String tenSP;

    public DSSanPhamBH() { }

    public DSSanPhamBH(String tenSP, String noiDungBH, int phiBH) {
        super(tenSP, noiDungBH, phiBH);
        this.tenSP = tenSP;
    }
}

public class PhieuBaoHanhTemp {

    public PhieuBaoHanhTemp() { }

    public PhieuBaoHanhTemp(String maBH, String tenSP, String ngayBH) {
        ChiTietBH.maBH = maBH;
        DSSanPhamBH.tenSP = tenSP;
        ChiTietBH.ngayBH = ngayBH;
    }

    public String getMaBH() {
        return ChiTietBH.maBH;
    }

    public void setMaBH(String maBH) {
        ChiTietBH.maBH = maBH;
    }

    public String getTenSP() {
        return DSSanPhamBH.tenSP;
    }

    public void setTenSP(String tenSP) {
        DSSanPhamBH.tenSP = tenSP;
    }

    public String getNgayBH() {
        return ChiTietBH.ngayBH;
    }

    public void setNgayBH(String ngayBH) {
        ChiTietBH.ngayBH = ngayBH;
    }
}
