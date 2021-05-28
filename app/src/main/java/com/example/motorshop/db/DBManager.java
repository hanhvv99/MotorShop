package com.example.motorshop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.motorshop.activity.guarantee.PhieuBaoHanhTemp;
import com.example.motorshop.activity.statistic.ThongKeTemp;
import com.example.motorshop.datasrc.BaoHanh;
import com.example.motorshop.datasrc.BoPhan;
import com.example.motorshop.datasrc.ChiTietDonHang;
import com.example.motorshop.datasrc.ChiTietSanPhamDonHang;
import com.example.motorshop.datasrc.ChiTietThongSoXe;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.datasrc.DonHang;
import com.example.motorshop.datasrc.KhachHang;
import com.example.motorshop.datasrc.NhaCungCap;
import com.example.motorshop.datasrc.NhanVien;
import com.example.motorshop.datasrc.SanPham;
import com.example.motorshop.datasrc.ThongSoPhuTung;
import com.example.motorshop.datasrc.ThongSoXe;
import com.example.motorshop.datasrc.Xe;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBManager extends SQLiteOpenHelper {

    private static final String TAG = "DBManager";

    public DBManager(Context context) {
        super(context, "dbMOTORSTORE.db", null, 1);
        Log.d(TAG,"Create DB: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArrayList<String> createTables = new ArrayList<>();
        createTables.add("CREATE TABLE IF NOT EXISTS BOPHAN (MABP text PRIMARY KEY, TENBP text not null)");
        createTables.add("CREATE TABLE IF NOT EXISTS NHANVIEN(MANV text PRIMARY KEY, HOTEN text not null, SDT text not null, MABP text not null, CONSTRAINT FK_NHANVIEN_BOPHAN FOREIGN KEY (MABP) REFERENCES BOPHAN(MABP))");
        createTables.add("CREATE TABLE IF NOT EXISTS KHACHHANG (CMND text PRIMARY KEY, HOTEN text not null, DIACHI text not null, SDT text null)");
        createTables.add("CREATE TABLE IF NOT EXISTS NHACUNGCAP (MANCC text PRIMARY KEY, TENNCC text not null, DIACHI text not null, SDT text not null, EMAIL text null, LOGO int not null)");
        createTables.add("CREATE TABLE IF NOT EXISTS XE (MAXE text PRIMARY KEY, TENXE text not null, SOLUONG int not null, DONGIA int not null, HANBAOHANH int not null, HINHANH int not null, MANCC text not null, CONSTRAINT FK_XE_NHACUNGCAP FOREIGN KEY (MANCC) REFERENCES NHACUNGCAP(MANCC))");
        createTables.add("CREATE TABLE IF NOT EXISTS PHUTUNG (MAPT text PRIMARY KEY, TENPT text not null, SOLUONG int not null, DONGIA int not null, HANBAOHANH int not null, HINHANH int not null, MANCC text not null, CONSTRAINT FK_PHUTUNG_NHACUNGCAP FOREIGN KEY (MANCC) REFERENCES NHACUNGCAP(MANCC))");
        createTables.add("CREATE TABLE IF NOT EXISTS THONGSOXE (MATS integer PRIMARY KEY AUTOINCREMENT, TENTS text not null)");
        createTables.add("CREATE TABLE IF NOT EXISTS CHITIETTHONGSOXE (MAXE text not null, MATS int not null, NOIDUNGTS text not null, CONSTRAINT FK_CHITIETTHONGSOXE_XE FOREIGN KEY (MAXE) REFERENCES XE(MAXE), CONSTRAINT FK_CHITIETTHONGSOXE_THONGSOXE FOREIGN KEY (MATS) REFERENCES THONGSOXE(MATS), PRIMARY KEY (MAXE, MATS))");
        createTables.add("CREATE TABLE IF NOT EXISTS THONGSOPHUTUNG (MAPT text not null, MAXE text not null, DONGIA int not null, CONSTRAINT FK_THONGSOPHUTUNG_PHUTUNG FOREIGN KEY (MAPT) REFERENCES PHUTUNG(MAPT), CONSTRAINT FK_THONGSOPHUTUNG_XE FOREIGN KEY (MAXE) REFERENCES XE(MAXE), PRIMARY KEY (MAPT, MAXE))");
        createTables.add("CREATE TABLE IF NOT EXISTS DONDATHANG (MADH text PRIMARY KEY, NGAYDAT text not null, CMND text not null, MANV text not null, CONSTRAINT FK_DONDATHANG_KHACHHANG FOREIGN KEY (CMND) REFERENCES KHACHHANG(CMND), CONSTRAINT FK_DONDATHANG_NHANVIEN FOREIGN KEY (MANV) REFERENCES NHANVIEN(MANV))");
        createTables.add("CREATE TABLE IF NOT EXISTS CHITIETDONDATXE (MADH text not null, MAXE text not null, SOLUONG int not null, DONGIABAN int not null, CONSTRAINT FK_CHITIETDONDATXE_DONDATHANG FOREIGN KEY (MADH) REFERENCES DONDATHANG(MADH), CONSTRAINT FK_CHITIETDONDATXE_XE FOREIGN KEY (MAXE) REFERENCES XE(MAXE), PRIMARY KEY (MADH, MAXE))");
        createTables.add("CREATE TABLE IF NOT EXISTS CHITIETDONDATPHUTUNG (MADH text not null, MAPT text not null, SOLUONG int not null, DONGIABAN int not null, CONSTRAINT FK_CHITIETDONDATPHUTUNG_DONDATHANG FOREIGN KEY (MADH) REFERENCES DONDATHANG(MADH), CONSTRAINT FK_CHITIETDONDATPHUTUNG_PHUTUNG FOREIGN KEY (MAPT) REFERENCES PHUTUNG(MAPT), PRIMARY KEY (MADH, MAPT))");
        createTables.add("CREATE TABLE IF NOT EXISTS BAOHANH (MABH text PRIMARY KEY, MADH text not null, NGAYBH text not null, MANV text not null, CONSTRAINT FK_BAOHANH_DONDATHANG FOREIGN KEY (MADH) REFERENCES DONDATHANG(MADH), CONSTRAINT FK_BAOHANH_NHANVIEN FOREIGN KEY (MANV) REFERENCES NHANVIEN(MANV))");
        createTables.add("CREATE TABLE IF NOT EXISTS CHITIETBAOHANHXE (MABH text not null, MAXE text not null, NOIDUNGBH text not null, PHIBH int null, CONSTRAINT FK_CHITIETBAOHANHXE_BAOHANH FOREIGN KEY (MABH) REFERENCES BAOHANH(MABH), CONSTRAINT FK_CHITIETBAOHANHXE_XE FOREIGN KEY (MAXE) REFERENCES XE(MAXE), PRIMARY KEY (MABH, MAXE, NOIDUNGBH))");
        createTables.add("CREATE TABLE IF NOT EXISTS CHITIETBAOHANHPHUTUNG (MABH text not null, MAPT text not null, NOIDUNGBH text not null, PHIBH int null, CONSTRAINT FK_CHITIETBAOHANHPHUTUNG_BAOHANH FOREIGN KEY (MABH) REFERENCES BAOHANH(MABH), CONSTRAINT FK_CHITIETBAOHANHPHUTUNG_PHUTUNG FOREIGN KEY (MAPT) REFERENCES PHUTUNG(MAPT), PRIMARY KEY (MABH, MAPT, NOIDUNGBH))");

        for(String str : createTables){
            db.execSQL(str);
            Log.d(TAG,"onCreate DB: " + str);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"onUpgrade DB: ");
    }


    //USED IN COMMONS
    public boolean ifIDExist(String IDColumnName, String tableName, String condition){
        boolean exist = false;
        String query = "select " +IDColumnName+ " from " +tableName+ " where " +IDColumnName+ " = " +condition;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String id_tmp_check = cursor.getString(0);                              //
            cursor.close();
            db.close();
            Log.d(TAG,"Check MaBP ifIDExist: " + IDColumnName + "=" + id_tmp_check);
            return true;
        }else{
            cursor.close();
            db.close();
            Log.d(TAG,"Check MaBP ifIDExist: not exist");
            return false;
        }
    }


    //BO PHAN (DEPARTMENT)
    public void insertDP(BoPhan department){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MABP", department.getMaBP());
        values.put("TENBP", department.getTenBP());
        db.insert("BOPHAN", null, values);
        db.close();
        Log.d(TAG,"Insert DEPARTMENT: ");
    }

    public void updateDP(BoPhan department){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Update  BOPHAN  set ";
        query += " TENBP  = '" + department.getTenBP() + "' ";
        query += " WHERE MABP  = '" + department.getMaBP() + "'";
        db.execSQL(query);
        db.close();
        Log.d(TAG,"Update DEPARTMENT: ");
    }

    public void deleteDP(BoPhan department){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM BOPHAN WHERE MABP = '" + department.getMaBP() + "'";
        db.execSQL(query);
        db.close();
        Log.d(TAG,"Delete DEPARTMENT: ");
    }

    public void deleteDP(String departmentID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM BOPHAN WHERE MABP = '" + departmentID + "'";
        db.execSQL(query);
        db.close();
        Log.d(TAG,"Delete DEPARTMENT with departmentID: ");
    }

    public void loadDPList(ArrayList<BoPhan> departmentList){
        departmentList.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from BOPHAN";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                BoPhan department = new BoPhan();
                department.setMaBP(cursor.getString(0));
                department.setTenBP(cursor.getString(1));
                departmentList.add(department);
            } while (cursor.moveToNext());
        }
        Log.d(TAG,"Load DEPARTMENT LIST: ");
    }


    //NHAN VIEN (STAFF)
    public void insertST(NhanVien staff){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANV", staff.getMaNV());
        values.put("HOTEN", staff.getHoTen());
        values.put("SDT", staff.getSdt());
        values.put("MABP", staff.getMaBP());
        db.insert("NHANVIEN", null, values);
        db.close();
        Log.d(TAG,"Insert STAFF: ");
    }
    public void updateST() { }
    public void deleteST() { }
    public void loadSTList() { }


    //KHACH HANG (CUSTOMER)
    public void insertCTM(KhachHang customer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CMND", customer.getCmnd());
        values.put("HOTEN", customer.getHoTen());
        values.put("DIACHI", customer.getDiaChi());
        values.put("SDT", customer.getSdt());
        db.insert("KHACHHANG", null, values);
        db.close();
        Log.d(TAG,"Insert CUSTOMER: ");
    }
    public void updateCTM() { }
    public void deleteCTM() { }
    public void loadCTMList() { }


    //NHA CUNG CAP (BRAND)
    public void insertBR(NhaCungCap brand){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANCC", brand.getMaNCC());
        values.put("TENNCC", brand.getTenNCC());
        values.put("DIACHI", brand.getDiaChi());
        values.put("SDT", brand.getSdt());
        values.put("EMAIL", brand.getEmail());
        values.put("LOGO", brand.getLogo());
        db.insert("NHACUNGCAP", null, values);
        db.close();
        Log.d(TAG,"Insert BRAND: ");
    }

    public void updateBR(NhaCungCap brand){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Update  NHACUNGCAP  set ";
        query += " TENNCC  = '"+ brand.getTenNCC()+"', ";
        query += " DIACHI  = '"+ brand.getDiaChi()+"', ";
        query += " SDT  = '"+ brand.getSdt()+"', ";
        query += " EMAIL  = '"+ brand.getEmail()+"', ";
        query += " LOGO  = '"+ brand.getLogo()+"' ";
        query += " WHERE MANCC  = '"+ brand.getMaNCC()+"'";
        db.execSQL(query);
        db.close();
        Log.d(TAG,"Update BRAND: ");
    }

    public void deleteBR(NhaCungCap brand){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM NHACUNGCAP WHERE MANCC = '" + brand.getMaNCC()+"'";
        db.execSQL(query);
        db.close();
        Log.d(TAG,"Delete BRAND: ");
    }

    public void deleteBR(String brandName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM NHACUNGCAP WHERE TENNCC = '" + brandName + "'";
        db.execSQL(query);
        db.close();
        Log.d(TAG,"Delete BRAND with brandID: ");
    }

    public void loadBRList(ArrayList<NhaCungCap> brandList){
        brandList.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM NHACUNGCAP";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                NhaCungCap brand = new NhaCungCap();
                brand.setMaNCC(cursor.getString(0));
                brand.setTenNCC(cursor.getString(1));
                brand.setDiaChi(cursor.getString(2));
                brand.setSdt(cursor.getString(3));
                brand.setEmail(cursor.getString(4));
                brand.setLogo(cursor.getInt(5));
                brandList.add(brand);
            } while (cursor.moveToNext());
        }
        Log.d(TAG,"Load BRAND: ");
    }


    //XE
    public void insertMotor(SanPham product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MAXE", product.getMaSP());
        values.put("TENXE", product.getTenSP());
        values.put("SOLUONG", product.getSoLuong());
        values.put("DONGIA", product.getDonGia());
        values.put("HANBAOHANH", product.getHanBH());
        values.put("HINHANH", product.getHinhAnh());
        values.put("MANCC", product.getMaNCC());
        db.insert("XE", null, values);
        db.close();
        Log.d(TAG,"Insert MOTOR: ");
    }
    public void updateXe() { }
    public void loadXe() { }
    public void deleteXe() { }


    //PHU TUNG
    public void insertAccessary(SanPham product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MAPT", product.getMaSP());
        values.put("TENPT", product.getTenSP());
        values.put("SOLUONG", product.getSoLuong());
        values.put("DONGIA", product.getDonGia());
        values.put("HANBAOHANH", product.getHanBH());
        values.put("HINHANH", product.getHinhAnh());
        values.put("MANCC", product.getMaNCC());
        db.insert("PHUTUNG", null, values);
        db.close();
        Log.d(TAG,"Insert ACCESSARY: ");
    }
    public void updatePT() { }
    public void loadPT() { }
    public void deletePT() { }


    //THONG SO XE
    public void insertMotorSpec(String motorSpecName) {      //Specification
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TENTS", motorSpecName);
        db.insert("THONGSOXE", null, values);
        db.close();
        Log.d(TAG,"Insert MOTOR SPECIFICATION: ");
    }
    public void updateMotorSpec() { }
    public void loadMotorSpec() { }
    public void deleteMotorSpec() { }

    //CHI TIET THONG SO XE
    public void insertMotorSpecDetail(ChiTietThongSoXe motorSpecDetail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MAXE", motorSpecDetail.getMaXe());
        values.put("MATS", motorSpecDetail.getMaTS());
        values.put("NOIDUNGTS", motorSpecDetail.getNoiDungTS());
        db.insert("CHITIETTHONGSOXE", null, values);
        db.close();
        Log.d(TAG,"Insert MOTOR SPECIFICATION DETAIL: ");
    }
    public void updateMotorSpecDetail() { }
    public void loadMotorSpecDetail() { }
    public void deleteMotorSpecDetail() { }


    //THONG SO PHU TUNG
    public void insertAccessorySpec(ThongSoPhuTung accessorySpec) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MAPT", accessorySpec.getMaPT());
        values.put("MAXE", accessorySpec.getMaXe());
        values.put("DONGIA", accessorySpec.getDonGia());
        db.insert("THONGSOPHUTUNG", null, values);
        db.close();
        Log.d(TAG,"Insert ACCESSORY SPECIFICATION: ");
    }
    public void updateTSPT() { }
    public void loadTSPT() { }
    public void deleteTSPT() { }


    //DON HANG
    public void insertBill(DonHang bill) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MADH", bill.getMaDH());
        values.put("NGAYDAT", bill.getNgayDat());
        values.put("CMND", bill.getCmnd());
        values.put("MANV", bill.getMaNV());
        db.insert("DONDATHANG", null, values);
        db.close();
        Log.d(TAG,"Insert BILL: ");
    }
    public void loadDH() { }


    //CHI TIET DON HANG XE
    public void insertMotorBillDetail(String maDH, ChiTietSanPhamDonHang productBillDetail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MADH", maDH);
        values.put("MAXE", productBillDetail.getMaSP());
        values.put("SOLUONG", productBillDetail.getSoLuong());
        values.put("DONGIABAN", productBillDetail.getDonGiaBan());
        db.insert("CHITIETDONDATXE", null, values);
        db.close();
        Log.d(TAG,"Insert MOTOR BILL DETAIL: ");
    }


    //CHI TIET DON HANG PHU TUNG
    public void insertAccessoryBillDetail(String maDH, ChiTietSanPhamDonHang productBillDetail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MADH", maDH);
        values.put("MAPT", productBillDetail.getMaSP());
        values.put("SOLUONG", productBillDetail.getSoLuong());
        values.put("DONGIABAN", productBillDetail.getDonGiaBan());
        db.insert("CHITIETDONDATPHUTUNG", null, values);
        db.close();
        Log.d(TAG,"Insert ACCESSORY BILL DETAIL: ");
    }


    //BAO HANH
    public void insertGuarantee(BaoHanh guarantee){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MABH", guarantee.getMaBH());
        values.put("MADH", guarantee.getMaDH());
        values.put("NGAYBH", guarantee.getNgayBH());
        values.put("MANV", guarantee.getMaNV());
        db.insert("BAOHANH", null, values);
        db.close();
        Log.d(TAG,"Insert GUARANTEE: ");
    }


    //CHI TIET BAO HANH XE
    public void insertMotorGuaranteeDetail(String maBH, DanhSachSanPhamBaoHanh motorGuaranteeDetail){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MABH", maBH);
        values.put("MAXE", motorGuaranteeDetail.getMaSP());
        values.put("NOIDUNGBH", motorGuaranteeDetail.getNoiDungBH());
        values.put("PHIBH", motorGuaranteeDetail.getPhiBH());
        db.insert("CHITIETBAOHANHXE", null, values);
        db.close();
        Log.d(TAG,"Insert MOTOR GUARANTEE DETAIL: ");
    }


    //CHI TIET BAO HANH XE
    public void insertAccessoryGuaranteeDetail(String maBH, DanhSachSanPhamBaoHanh accessoryGuaranteeDetail){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MABH", maBH);
        values.put("MAPT", accessoryGuaranteeDetail.getMaSP());
        values.put("NOIDUNGBH", accessoryGuaranteeDetail.getNoiDungBH());
        values.put("PHIBH", accessoryGuaranteeDetail.getPhiBH());
        db.insert("CHITIETBAOHANHPHUTUNG", null, values);
        db.close();
        Log.d(TAG,"Insert ACCESSORY GUARANTEE DETAIL: ");
    }



    public void initData(){
        BoPhan dp = new BoPhan("BH", "Ban Hang");
        insertDP(dp);
        dp = new BoPhan("BD", "Bao Duong");
        insertDP(dp);
        dp = new BoPhan("GD", "Giam Doc");
        insertDP(dp);

        NhanVien st = new NhanVien("NV01", "Tu An", "0123456111", "GD");
        insertST(st);
        st = new NhanVien("NV02", "Le Ngoc Khanh", "0123456222", "BH");
        insertST(st);
        st = new NhanVien("NV03", "Thu Huong", "0123456333", "BH");
        insertST(st);
        st = new NhanVien("NV04", "Kha Ngan", "0123456444", "BH");
        insertST(st);
        st = new NhanVien("NV05", "Vinh Nhat Khoa", "0123456555", "BD");
        insertST(st);
        st = new NhanVien("NV06", "Tan Loc", "0123456666", "BD");
        insertST(st);
        st = new NhanVien("NV07", "Pham Khiem", "0123456777", "BD");
        insertST(st);

        KhachHang ctm = new KhachHang("302456811", "Le Van Hau", "97 Man Thien, Ho Chi Minh", "0347895411");
        insertCTM(ctm);
        ctm = new KhachHang("302456822", "Tran Anh", "65 CMT8, Ho Chi Minh", "0347895422");
        insertCTM(ctm);
        ctm = new KhachHang("302456833", "Vo Chi Trung", "156 3/2, Ho Chi Minh", "0347895433");
        insertCTM(ctm);
        ctm = new KhachHang("302456844", "Le Anh Tuyet", "7 Ly Thai To, Ho Chi Minh", "0347895444");
        insertCTM(ctm);
        ctm = new KhachHang("302456855", "Nguyen Thuy Tran", "1 Hoa Hong, Ho Chi Minh", "0347895455");
        insertCTM(ctm);

        NhaCungCap br = new NhaCungCap("HD", "Honda", "Phuc Thang, Phuc Yen, Vinh Phuc, Viet Nam", "18008001", "cr@honda.com.vn", 2131165305);
        insertBR(br);
        br = new NhaCungCap("YM", "Yamaha", "Binh An, Trung Gia, Soc Son, Ha Noi", "18001588", "cskh@yamaha-motor.com.vn", 2131165360);
        insertBR(br);
        br = new NhaCungCap("SY", "SYM", "4 5C, KCN Nhon Trach 2, Dong Nai", "0912111918", "cskhmn@sym.com.vn", 2131165356);
        insertBR(br);
        br = new NhaCungCap("OL", "Ohlins", "Box 722, 194 27 Upplands Vasby, Sweden", "1245777432", "service@ohlins.se", 2131165352);
        insertBR(br);
        br = new NhaCungCap("AK", "Akrapovic", "1295 Ivancna Gorica, Slovenia", "3456778998", "service@akrapovic.si", 2131165279);
        insertBR(br);

        SanPham pd = new SanPham("HD01", "Wave Alpha", 8, 18099000, 24, 2131165303, "HD");
        insertMotor(pd);
        pd = new SanPham("HD02", "Winner X", 9, 45999000, 48, 2131165304, "HD");
        insertMotor(pd);
        pd = new SanPham("HD03", "Vision", 10, 29999000, 36, 2131165302, "HD");
        insertMotor(pd);
        pd = new SanPham("YM01", "Sirius", 8, 21099000, 36, 2131165363, "YM");
        insertMotor(pd);
        pd = new SanPham("YM02", "Exciter", 9, 50499000, 48, 2131165361, "YM");
        insertMotor(pd);
        pd = new SanPham("YM03", "Grande", 10, 49999000, 48, 2131165362, "YM");
        insertMotor(pd);
        pd = new SanPham("OH01", "Nhan dan trang tri Ohlins 1", 5, 403000, 3, 2131165351, "OH");
        insertAccessary(pd);
        pd = new SanPham("OH02", "Phuoc Ohlins Vario", 5, 8500000, 24, 2131165350, "OH");
        insertAccessary(pd);
        pd = new SanPham("AK01", "Nhan dan Akrapovic chong nhiet nhom", 5, 212000, 12, 2131165278, "AK");
        insertAccessary(pd);
        pd = new SanPham("AK02", "Po Akrapovic GP Titan Yamaha R3", 5, 8000000, 24, 2131165277, "AK");
        insertAccessary(pd);

        ThongSoXe motorSpec = new ThongSoXe();
        insertMotorSpec("Khối lượng");          //1
        insertMotorSpec("Dài x Rộng x Cao");
        insertMotorSpec("Độ cao yên");          //3
        insertMotorSpec("Khoảng sáng gầm xe");
        insertMotorSpec("Dung tích bình xăng"); //5
        insertMotorSpec("Loại động cơ");
        insertMotorSpec("Công suất tối đa");    //7
        insertMotorSpec("Dung tích nhớt máy");
        insertMotorSpec("Hộp số");              //9
        insertMotorSpec("Dung tích xy-lanh");

        ChiTietThongSoXe motorSpecDetail = new ChiTietThongSoXe("HD01", 1, "97kg");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("HD01", 2, "1.914mm x 688mm x 1.075mm");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("HD01", 5, "3,7 lít");
        insertMotorSpecDetail(motorSpecDetail);

        motorSpecDetail = new ChiTietThongSoXe("HD02", 1, "Phiên bản phanh thường: 123kg Phiên bản phanh ABS: 124kg");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("HD02", 2, "2.019 x 727 x 1.088 mm");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("HD02", 5, "4,5 lít");
        insertMotorSpecDetail(motorSpecDetail);

        motorSpecDetail = new ChiTietThongSoXe("HD03", 1, "Phiên bản Tiêu chuẩn: 96kg Phiên bản Đặc biệt và Cao cấp: 97kg");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("HD03", 2, "1.871mm x 686mm x 1.101mm");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("HD03", 5, "4,9 lít");
        insertMotorSpecDetail(motorSpecDetail);

        motorSpecDetail = new ChiTietThongSoXe("YM01", 1, "96kg");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("YM01", 2, "1.890mm x 665mm x 1.035mm");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("YM01", 5, "4,2 lít");
        insertMotorSpecDetail(motorSpecDetail);

        motorSpecDetail = new ChiTietThongSoXe("YM02", 1, "121 kg");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("YM02", 2, "1,975 mm × 665 mm × 1,085 mm");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("YM02", 5, "5.4 lít");
        insertMotorSpecDetail(motorSpecDetail);

        motorSpecDetail = new ChiTietThongSoXe("YM03", 1, "101 kg");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("YM03", 2, "1.820mm x 685mm x 1.150mm");
        insertMotorSpecDetail(motorSpecDetail);
        motorSpecDetail = new ChiTietThongSoXe("YM03", 5, "4,4 lít");
        insertMotorSpecDetail(motorSpecDetail);

        ThongSoPhuTung accessorySpec = new ThongSoPhuTung("OH01", "HD01", 350000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("OH01", "HD03", 450000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("OH01", "YM02", 400000);
        insertAccessorySpec(accessorySpec);

        accessorySpec = new ThongSoPhuTung("OH02", "YM01", 800000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("OH02", "YM03", 8500000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("OH02", "HD02", 9000000);
        insertAccessorySpec(accessorySpec);

        accessorySpec = new ThongSoPhuTung("AK01", "HD01", 150000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("AK01", "HD02", 250000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("AK01", "HD03", 200000);
        insertAccessorySpec(accessorySpec);

        accessorySpec = new ThongSoPhuTung("AK02", "YM01", 7500000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("AK02", "YM02", 8000000);
        insertAccessorySpec(accessorySpec);
        accessorySpec = new ThongSoPhuTung("AK02", "YM03", 8500000);
        insertAccessorySpec(accessorySpec);

        DonHang bill = new DonHang("HD01", "01/04/2021 09:00:00", "302456811", "NV02");
        insertBill(bill);
        bill = new DonHang("HD02", "01/04/2021 15:30:00", "302456822", "NV03");
        insertBill(bill);
        bill = new DonHang("HD03", "25/04/2021 10:05:00", "302456811", "NV02");
        insertBill(bill);
        bill = new DonHang("HD04", "02/05/2021 08:15:00", "302456844", "NV04");
        insertBill(bill);
        bill = new DonHang("HD05", "02/05/2021 16:33:00", "302456844", "NV04");
        insertBill(bill);
        bill = new DonHang("HD06", "08/05/2021 13:20:00", "302456833", "NV03");
        insertBill(bill);
        bill = new DonHang("HD07", "12/05/2021 07:10:00", "302456855", "NV04");
        insertBill(bill);

        ChiTietSanPhamDonHang productBillDetail;
        productBillDetail = new ChiTietSanPhamDonHang("HD01", 1, 18099000);
        insertMotorBillDetail("HD01", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("YM02", 3, 50499000);
        insertMotorBillDetail("HD01", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("OH01", 2, 400000);
        insertAccessoryBillDetail("HD01", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("YM02", 2, 50499000);
        insertMotorBillDetail("HD02", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("YM01", 1, 21000000);
        insertMotorBillDetail("HD03", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("AK01", 3, 212000);
        insertAccessoryBillDetail("HD03", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("HD03", 1, 29999000);
        insertMotorBillDetail("HD04", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("YM03", 1, 49999000);
        insertMotorBillDetail("HD05", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("AK02", 1, 8000000);
        insertAccessoryBillDetail("HD05", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("HD02", 4, 45999000);
        insertMotorBillDetail("HD06", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("YM01", 1, 21100000);
        insertMotorBillDetail("HD06", productBillDetail);
        productBillDetail = new ChiTietSanPhamDonHang("HD03", 1, 29999000);
        insertMotorBillDetail("HD07", productBillDetail);

        BaoHanh guarantee = new BaoHanh("BH01", "HD01", "01/05/2021 10:00:00", "NV05");
        insertGuarantee(guarantee);
        guarantee = new BaoHanh("BH02", "HD01", "05/05/2021 10:00:00", "NV05");
        insertGuarantee(guarantee);
        guarantee = new BaoHanh("BH03", "HD05", "15/05/2021 08:11:00", "NV07");
        insertGuarantee(guarantee);
        guarantee = new BaoHanh("BH04", "HD04", "20/05/2021 09:22:00", "NV06");
        insertGuarantee(guarantee);

        DanhSachSanPhamBaoHanh guaranteeProductDetail = new DanhSachSanPhamBaoHanh("HD01", "Thay nhot", 0);
        insertMotorGuaranteeDetail("BH01", guaranteeProductDetail);
        guaranteeProductDetail = new DanhSachSanPhamBaoHanh("HD01", "Tang sen", 0);
        insertMotorGuaranteeDetail("BH01", guaranteeProductDetail);
        guaranteeProductDetail = new DanhSachSanPhamBaoHanh("YM02", "Thay lop xe", 0);
        insertMotorGuaranteeDetail("BH01", guaranteeProductDetail);
        guaranteeProductDetail = new DanhSachSanPhamBaoHanh("OH01", "Thay tem", 0);
        insertAccessoryGuaranteeDetail("BH01", guaranteeProductDetail);
        guaranteeProductDetail = new DanhSachSanPhamBaoHanh("YM02", "Siet thang", 0);
        insertMotorGuaranteeDetail("BH02", guaranteeProductDetail);
        guaranteeProductDetail = new DanhSachSanPhamBaoHanh("AK02", "Han ong bo", 0);
        insertAccessoryGuaranteeDetail("BH03", guaranteeProductDetail);
        guaranteeProductDetail = new DanhSachSanPhamBaoHanh("HD03", "Thay day ga", 0);
        insertMotorGuaranteeDetail("BH04", guaranteeProductDetail);

    }

    //////BAO HÀNH CỦA HẠNH VÀ MY
    public long createPhieuBH(String MABH, String MADH,
                              String NGAYBH, String MANV) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MABH", MABH);
        values.put("MADH", MADH);
        values.put("NGAYBH", NGAYBH);
        values.put("MANV", MANV);

        Log.d("BAO HANH", "CREATE BAO HANH thanh cong!");
        return db.insert("BAOHANH", null, values);
    }

    public long createChiTietBHXE(String MABH, String MAXE,
                                  String NOIDUNGBH, int PHIBH) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MABH", MABH);
        values.put("MAXE", MAXE);
        values.put("NOIDUNGBH", NOIDUNGBH);
        values.put("PHIBH", PHIBH);

        Log.d("CTBH XE", "CREATE CTBH XE thanh cong!");
        return db.insert("CHITIETBAOHANHXE", null, values);
    }

    public long createChiTietBHPT(String MABH, String MAPT,
                                  String NOIDUNGBH, int PHIBH) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MABH", MABH);
        values.put("MAPT", MAPT);
        values.put("NOIDUNGBH", NOIDUNGBH);
        values.put("PHIBH", PHIBH);

        Log.d("CTBH PT", "CREATE CTBH PT thanh cong!");
        return db.insert("CHITIETBAOHANHPHUTUNG", null, values);
    }

    public boolean deleteAllDonHangBaoHanh() {
        SQLiteDatabase db = this.getWritableDatabase();

        int doneDelete = 0;
        doneDelete = db.delete("DONDATHANG", null , null);
        Log.w("Delete DonHang", Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public void deletePhieuBH(int pos, ArrayList<PhieuBaoHanhTemp> ds) {
        SQLiteDatabase db = this.getWritableDatabase();
        String maBH = ds.get(pos).getMaBH();
        String delSQL = "DELETE FROM BAOHANH WHERE MABH = '"+maBH+"'";
        db.execSQL(delSQL);
    }

    public void deleteAllNoiDungBHX(int pos, ArrayList<PhieuBaoHanhTemp> ds) {
        SQLiteDatabase db = this.getWritableDatabase();
        String maBH = ds.get(pos).getMaBH();
        String delSQL = "DELETE FROM CHITIETBAOHANHXE WHERE MABH = '"+maBH+"'";
        db.execSQL(delSQL);
    }

    public void deleteAllNoiDungBHPT(int pos, ArrayList<PhieuBaoHanhTemp> ds) {
        SQLiteDatabase db = this.getWritableDatabase();
        String maBH = ds.get(pos).getMaBH();
        String delSQL = "DELETE FROM CHITIETBAOHANHPHUTUNG WHERE MABH = '"+maBH+"'";
        db.execSQL(delSQL);
    }

    public void deleteNoiDungBHX(int pos, ArrayList<DanhSachSanPhamBaoHanh> ds){
        SQLiteDatabase db = this.getWritableDatabase();
        String noiDung = ds.get(pos).getNoiDungBH();
        String delSQL = "DELETE FROM CHITIETBAOHANHXE WHERE NOIDUNGBH = '"+noiDung+"'";
        db.execSQL(delSQL);
    }

    public void deleteNoiDungBHPT(int pos, ArrayList<DanhSachSanPhamBaoHanh> ds){
        SQLiteDatabase db = this.getWritableDatabase();
        String noiDung = ds.get(pos).getNoiDungBH();
        String delSQL = "DELETE FROM CHITIETBAOHANHPHUTUNG WHERE NOIDUNGBH = '"+noiDung+"'";
        db.execSQL(delSQL);
    }

    public Cursor timDonHangTheoCMND(String inputCMND) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.w("Search CMND", inputCMND);
        Cursor mCursor = null;
        if (inputCMND == null  ||  inputCMND.length () == 0)  {
            mCursor = db.query("DONDATHANG", new String[] {"MADH as _id","NGAYDAT"},
                    null, null, null, null, null);
        }
        else {
            mCursor = db.query(true,"DONDATHANG", new String[] {"MADH as _id","NGAYDAT"},
                    "CMND" + " like '%" + inputCMND + "%'",
                    null, null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor timAllDonHang() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.query("DONDATHANG", new String[] {"MADH as _id","NGAYDAT"},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //load tat ca don hang
    public ArrayList<DonHang> loadAllDonBH(){
        ArrayList<DonHang> donHangArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + "DONDATHANG";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setMaDH(cursor.getString(0));
                donHang.setNgayDat(cursor.getString(1));
                donHangArrayList.add(donHang);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return donHangArrayList;
    }

    public ArrayList<DanhSachSanPhamBaoHanh> loadAllNdBhXe(String maBH, String maSP) {
        ArrayList<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs = new ArrayList<>();

        String selectQuery = "SELECT * FROM CHITIETBAOHANHXE WHERE MABH = '"+maBH+"' AND MAXE = '"+maSP+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DanhSachSanPhamBaoHanh ds = new DanhSachSanPhamBaoHanh();
                ds.setNoiDungBH(cursor.getString(2));
                ds.setPhiBH(cursor.getInt(3));
                danhSachSanPhamBaoHanhs.add(ds);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return danhSachSanPhamBaoHanhs;
    }

    public ArrayList<DanhSachSanPhamBaoHanh> loadAllNdBhPT(String maBH, String maSP) {
        ArrayList<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs = new ArrayList<>();

        String selectQuery = "SELECT * FROM CHITIETBAOHANHPHUTUNG WHERE MABH = '"+maBH+"' AND MAPT = '"+maSP+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DanhSachSanPhamBaoHanh ds = new DanhSachSanPhamBaoHanh();
                ds.setNoiDungBH(cursor.getString(2));
                ds.setPhiBH(cursor.getInt(3));
                danhSachSanPhamBaoHanhs.add(ds);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return danhSachSanPhamBaoHanhs;
    }

    //PHIEU BH
    public ArrayList<PhieuBaoHanhTemp> loadAllPhieuBH(String maDH) {
        ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps = new ArrayList<>();

        String selectQuery = "SELECT BH.MABH AS MABH, X.TENXE AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHXE CTX ON CTX.MABH = BH.MABH "+
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE WHERE BH.MADH = '"+maDH+"' "+
                "UNION "+
                "SELECT BH.MABH AS MABH, PT.TENPT AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHPHUTUNG CTPT ON CTPT.MABH = BH.MABH "+
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT WHERE BH.MADH = '"+maDH+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhieuBaoHanhTemp p = new PhieuBaoHanhTemp();
                p.setMaBH(cursor.getString(0));
                p.setTenSP(cursor.getString(1));
                p.setNgayBH(cursor.getString(2));
                phieuBaoHanhTemps.add(p);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return phieuBaoHanhTemps;
    }

    public ArrayList<PhieuBaoHanhTemp> loadAllPhieuXe(String maDH) {
        ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT BH.MABH AS MABH, X.TENXE AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHXE CTX ON CTX.MABH = BH.MABH "+
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE WHERE BH.MADH = '"+maDH+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhieuBaoHanhTemp p = new PhieuBaoHanhTemp();
                p.setMaBH(cursor.getString(0));
                p.setTenSP(cursor.getString(1));
                p.setNgayBH(cursor.getString(2));
                phieuBaoHanhTemps.add(p);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return phieuBaoHanhTemps;
    }

    public ArrayList<PhieuBaoHanhTemp> loadAllPhieuPT(String maDH) {
        ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT BH.MABH AS MABH, PT.TENPT AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHPHUTUNG CTPT ON CTPT.MABH = BH.MABH "+
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT WHERE BH.MADH = '"+maDH+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhieuBaoHanhTemp p = new PhieuBaoHanhTemp();
                p.setMaBH(cursor.getString(0));
                p.setTenSP(cursor.getString(1));
                p.setNgayBH(cursor.getString(2));
                phieuBaoHanhTemps.add(p);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return phieuBaoHanhTemps;
    }

    public List<PhieuBaoHanhTemp> get1TenLabel(String maDH){
        List<PhieuBaoHanhTemp> list = new ArrayList<PhieuBaoHanhTemp>();

        String selectQuery = "SELECT X.TENXE AS TEN_SAN_PHAM " +
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATXE CTX ON CTX.MADH = DH.MADH " +
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE " +
                "WHERE DH.MADH = '"+maDH+"' " +
                "UNION " +
                "SELECT PT.TENPT AS TEN_SAN_PHAM " +
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATPHUTUNG CTPT ON CTPT.MADH = DH.MADH " +
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT " +
                "WHERE DH.MADH = '"+maDH+"' " +
                "ORDER BY TEN_SAN_PHAM";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PhieuBaoHanhTemp p = new PhieuBaoHanhTemp();
                p.setTenSP(cursor.getString(0));
                list.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    public String[] get1LabelNames(String maDH){
        List<String> list=new ArrayList<>();
        String[] sanPhamDonHang = list.toArray(new String[0]);

        String selectQuery = "SELECT X.TENXE AS TEN_SAN_PHAM " +
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATXE CTX ON CTX.MADH = DH.MADH " +
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE " +
                "WHERE DH.MADH = '"+maDH+"' " +
                "UNION " +
                "SELECT PT.TENPT AS TEN_SAN_PHAM " +
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATPHUTUNG CTPT ON CTPT.MADH = DH.MADH " +
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT " +
                "WHERE DH.MADH = '"+maDH+"' " +
                "ORDER BY TEN_SAN_PHAM";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
                sanPhamDonHang = list.toArray(new String[list.size()]);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return sanPhamDonHang;
    }

    public Integer[] get1LabelImage(String maDH){
        List<Integer> intList = new ArrayList<Integer>();
        Integer[] flags = intList.toArray(new Integer[0]);

        String selectQuery = "SELECT X.HINHANH AS HINHANH "+
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATXE CTX ON CTX.MADH = DH.MADH "+
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE "+
                "WHERE DH.MADH = '"+maDH+"' "+
                "UNION "+
                "SELECT PT.HINHANH AS HINHANH "+
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATPHUTUNG CTPT ON CTPT.MADH = DH.MADH "+
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT "+
                "WHERE DH.MADH = '"+maDH+"' "+
                "ORDER BY HINHANH ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                intList.add(cursor.getInt(0));
                flags = intList.toArray(new Integer[intList.size()]);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return flags;
    }

    public String get1MaSP(String tenSP){
        String ma = "";
        String selectQuery = "SELECT MAXE AS MA " +
                "FROM XE WHERE TENXE = '"+tenSP+"' " +
                "UNION " +
                "SELECT MAPT AS MA " +
                "FROM PHUTUNG WHERE TENPT = '"+tenSP+"' " +
                "ORDER BY MA";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ma = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return ma;
    }

    public String get1NgayDH(String maDH){
        String ma = "";
        String selectQuery = "SELECT NGAYDAT FROM DONDATHANG WHERE MADH = '"+maDH+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ma = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return ma;
    }

    public String getID(){
        String id =null;

        String selectQuery = "SELECT MAX(MABH) FROM BAOHANH ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return id;
    }

    public long get1HanBH(String maDH, String maSP){
        long han = 0;
        String selectQuery = "SELECT X.HANBAOHANH "+
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATXE CTX ON CTX.MADH = DH.MADH "+
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE "+
                "WHERE DH.MADH = '"+maDH+"' AND X.MAXE = '"+maSP+"' "+
                "UNION "+
                "SELECT PT.HANBAOHANH "+
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATPHUTUNG CTPT ON CTPT.MADH = DH.MADH "+
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT "+
                "WHERE DH.MADH = '"+maDH+"' AND PT.MAPT = '"+maSP+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                han = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return han;
    }

    public int setTonTaiTblXe(String maSP){
        int check = 0;
        String selectQuery = "SELECT * FROM XE WHERE MAXE = '"+maSP+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                check = 1;
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        Log.d("TON TAI", maSP +" trong Xe");
        return check;
    }

    public int setTonTaiMaBH(String maBH){
        int check = 0;
        String selectQuery = "SELECT * FROM BAOHANH WHERE MABH = '"+maBH+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                check = 1;
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        Log.d("TON TAI", maBH +" da ton tai");
        return check;
    }

    public int getTongPhi(String maBH, String maSP) {
        int tong = 0;

        String selectQuery = "SELECT SUM(PHIBH) AS PHIBH FROM CHITIETBAOHANHXE " +
                "WHERE MABH = '"+maBH+"' AND MAXE = '"+maSP+"' "+
                "GROUP BY MABH "+
                "UNION "+
                "SELECT SUM(PHIBH) AS PHIBH FROM CHITIETBAOHANHPHUTUNG " +
                "WHERE MABH = '"+maBH+"' AND MAPT = '"+maSP+"' "+
                "GROUP BY MABH";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tong = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return tong;
    }

    //THỐNG KÊ XE
    public int getAllSLxe(){
        String selectQuery = "SELECT SUM(C.SOLUONG) FROM chitietdondatxe C, DONDATHANG D WHERE C.MADH = D.MADH ";

        int tong = cursorSQLint(selectQuery);
        return tong;
    }

    public int getAllSLpt(){
        String selectQuery = "SELECT SUM(P.SOLUONG) FROM chitietdondatphutung P, DONDATHANG D WHERE P.MADH = D.MADH ";

        int tong = cursorSQLint(selectQuery);
        return tong;
    }

    public int getAllDTxe(){
        String selectQuery = "SELECT SUM(C.DONGIABAN) FROM chitietdondatxe C, DONDATHANG D WHERE C.MADH = D.MADH ";

        int tong = cursorSQLint(selectQuery);
        return tong;
    }

    public int getAllDTpt(){
        String selectQuery = "SELECT SUM(P.DONGIABAN) FROM chitietdondatphutung P, DONDATHANG D WHERE P.MADH = D.MADH ";

        int tong = cursorSQLint(selectQuery);
        return tong;
    }

    public ArrayList<ThongKeTemp> loadXeTheoTG() {
        String selectQuery = "SELECT X.TENXE,SUM(C.SOLUONG) AS SOLUONG, (C.DONGIABAN), D.NGAYDAT " +
                "FROM chitietdondatxe C, DONDATHANG D, XE X " +
                "WHERE X.MAXE = C.MAXE AND C.MADH = D.MADH "+
                "group BY X.MAXE ORDER BY C.SOLUONG DESC ";

        ArrayList<ThongKeTemp> list = cursorSQLArrTK(selectQuery);
        return list;
    }

    public ArrayList<ThongKeTemp> loadPTtheoTG() {
        String selectQuery = "SELECT PT.TENPT, SUM(P.SOLUONG) AS SOLUONG, (P.DONGIABAN) AS DONGIA, D.NGAYDAT " +
                "FROM CHITIETDONDATPHUTUNG P, DONDATHANG D, PHUTUNG PT "+
                "WHERE PT.MAPT = P.MAPT AND P.MADH = D.MADH "+
                "GROUP BY PT.TENPT ORDER BY P.SOLUONG DESC";

        ArrayList<ThongKeTemp> list = cursorSQLArrTK(selectQuery);
        return list;
    }

    public ArrayList<ThongKeTemp> loadXeTheoTGLimit(String i) {
        String selectQuery = "SELECT X.TENXE,SUM(C.SOLUONG) AS SOLUONG, (C.DONGIABAN), D.NGAYDAT " +
                "FROM chitietdondatxe C, DONDATHANG D, XE X " +
                "WHERE X.MAXE = C.MAXE AND C.MADH = D.MADH "+
                "group BY X.MAXE ORDER BY C.SOLUONG DESC LIMIT "+i;

        ArrayList<ThongKeTemp> list = cursorSQLArrTK(selectQuery);
        return list;
    }

    public ArrayList<ThongKeTemp> loadPTtheoTGLimit(String i) {
        String selectQuery = "SELECT PT.TENPT, SUM(P.SOLUONG) AS SOLUONG, (P.DONGIABAN) AS DONGIA, D.NGAYDAT " +
                "FROM CHITIETDONDATPHUTUNG P, DONDATHANG D, PHUTUNG PT "+
                "WHERE PT.MAPT = P.MAPT AND P.MADH = D.MADH "+
                "GROUP BY PT.TENPT ORDER BY P.SOLUONG DESC LIMIT "+i;

        ArrayList<ThongKeTemp> list = cursorSQLArrTK(selectQuery);
        return list;
    }

    public int cursorSQLint(String selectQuery){
        int tong = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tong = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        Log.d("SQL",selectQuery);
        Log.d("int tong", String.valueOf(tong));
        db.close();
        cursor.close();

        return tong;
    }

    public ArrayList<ThongKeTemp> cursorSQLArrTK(String selectQuery){
        ArrayList<ThongKeTemp> listTK = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ThongKeTemp tk = new ThongKeTemp();
                tk.setTenSP(cursor.getString(0));
                tk.setSoLuong(cursor.getInt(1));
                tk.setGiaBan(cursor.getInt(2));
                tk.setNgayDat(cursor.getString(3));
                listTK.add(tk);
            } while (cursor.moveToNext());
        }
        Log.d("SQL",selectQuery);
        db.close();
        cursor.close();
        return listTK;
    }


}
