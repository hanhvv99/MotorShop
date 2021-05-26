package com.example.motorshop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.motorshop.activity.guarantee.PhieuBaoHanhTemp;
import com.example.motorshop.datasrc.BaoHanh;
import com.example.motorshop.datasrc.BoPhan;
import com.example.motorshop.datasrc.ChiTietBaoHanh;
import com.example.motorshop.datasrc.DanhSachSanPhamBaoHanh;
import com.example.motorshop.datasrc.DonHang;
import com.example.motorshop.datasrc.NhaCungCap;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    private static final String TAG = "DBManager";

    public DBManager(Context context) {
        super(context, "dbMOTORSTORE.db", null, 2);
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
        createTables.add("CREATE TABLE IF NOT EXISTS THONGSOXE (MATS int PRIMARY KEY, TENTS text not null)");
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
    public void insertST(){ }
    public void updateST() { }
    public void deleteST() { }
    public void loadSTList() { }


    //KHACH HANG (CUSTOMER)
    public void insertCTM() { }
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
    public void insertXe() { }
    public void updateXe() { }
    public void loadXe() { }
    public void deleteXe() { }


    //PHU TUNG
    public void insertPT() { }
    public void updatePT() { }
    public void loadPT() { }
    public void deletePT() { }


    //THONG SO XE
    public void insertTSX() { }
    public void updateTSX() { }
    public void loadTSX() { }
    public void deleteTSX() { }


    //CHI TIET THONG SO XE
    public void insertCTTSX() { }
    public void updateCTTSX() { }
    public void loadCTTSX() { }
    public void deleteCTTSX() { }


    //THONG SO PHU TUNG
    public void insertTSPT() { }
    public void updateTSPT() { }
    public void loadTSPT() { }
    public void deleteTSPT() { }


    //DON HANG & CHI TIET DON HANG
    //public void insertDH() { }      ->        public void insertCTDH() { }
    public void loadDH() { }


//CAC PHAN CON LAI TUONG TU

    //KHU VUC CUA BAO HANH
    //tao don hang de select don hang bao hanh

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

    public List<String> get1Label(String maDH){
        List<String> list = new ArrayList<String>();

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
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
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

    public String get1HanBH(String maDH, String maSP){
        String han = "";
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
                han = cursor.getString(0);
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

    public int getTongPhieu() {
        int tong = 0;

        String selectQuery = "SELECT COUNT(MABH) AS MABH FROM BAOHANH ";

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
}