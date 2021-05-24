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
    public long createDonHang(String madh, String ngaydat,
                              String cmnd, String manv) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MADH", madh);
        values.put("NGAYDAT", ngaydat);
        values.put("CMND", cmnd);
        values.put("MANV", manv);

        return db.insert("DONDATHANG", null, values);
    }

    public long createCTDX(String madh, String maxe,
                           String SOLUONG, String DONGIABAN) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MADH", madh);
        values.put("MAXE", maxe);
        values.put("SOLUONG", SOLUONG);
        values.put("DONGIABAN", DONGIABAN);

        return db.insert("CHITIETDONDATXE", null, values);
    }

    public long createCTDPT(String madh, String MAPT,
                            String SOLUONG, String DONGIABAN) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MADH", madh);
        values.put("MAPT", MAPT);
        values.put("SOLUONG", SOLUONG);
        values.put("DONGIABAN", DONGIABAN);

        return db.insert("CHITIETDONDATPHUTUNG", null, values);
    }

    public long createNCC(String MANCC, String TENNCC,
                          String DIACHI, String SDT,String EMAIL, String LOGO ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANCC", MANCC);
        values.put("TENNCC", TENNCC);
        values.put("DIACHI", DIACHI);
        values.put("SDT", SDT);
        values.put("EMAIL", EMAIL);
        values.put("LOGO", LOGO);

        return db.insert("NHACUNGCAP", null, values);
    }

    public long createXe(String MAXE, String TENXE,
                         String SOLUONG, String DONGIA,String HANBAOHANH, String HINHANH,String MANCC ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MAXE", MAXE);
        values.put("TENXE", TENXE);
        values.put("SOLUONG", SOLUONG);
        values.put("DONGIA", DONGIA);
        values.put("HANBAOHANH", HANBAOHANH);
        values.put("HINHANH", HINHANH);
        values.put("MANCC", MANCC);

        return db.insert("XE", null, values);
    }

    public long createPT(String MAPT, String TENPT,
                         String SOLUONG, String DONGIA,String HANBAOHANH, String HINHANH,String MANCC ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MAPT", MAPT);
        values.put("TENPT", TENPT);
        values.put("SOLUONG", SOLUONG);
        values.put("DONGIA", DONGIA);
        values.put("HANBAOHANH", HANBAOHANH);
        values.put("HINHANH", HINHANH);
        values.put("MANCC", MANCC);

        return db.insert("PHUTUNG", null, values);
    }

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

    public void insertSomeDH() {
        createDonHang("DH001","15/4/2021","052845714","NV001");
        createDonHang("DH002","4/5/2021","019235655874","NV001");
        Log.d("DON HANG", "add dh thanh cong!");
    }

    public void insertSomeCTSPDH(){
        createCTDX("DH001", "X002", "1", "78000000");
        createCTDX("DH001", "X003", "1", "23500000");
        createCTDPT("DH001", "P001", "1", "750000");
        Log.d("CTSPDH", "add ctsp thanh cong!");
    }

    public void insertSomeNCC(){
        createNCC("HD","Honda","Quan 1","0283444666","honda_quan1@honda.com","NULL");
        createNCC("OL","Ohlins","Thuy Dien","0283000111","onlins@ol.com","NULL");
        createNCC("SY","SYM","Dai Loan","0114566874","sym@taiwan.com","NULL");
        createNCC("YM","Yamaha","Nhat Ban","0283222888","yamaha@nippon.jp","NULL");
        Log.d("NHACUNGCAP", "add NCC thanh cong!");
    }

    public void insertSomeSP(){
        createXe("X001","Atila 125","20","25000000","36","NULL","SY");
        createXe("X002","Dylan 125","15","78000000","36","NULL","HD");
        createXe("X003","Dream 100","35","24000000","24","NULL","HD");
        createPT("P001","Phuoc Ohlins","5","750000","6","NULL","OL");
        Log.d("SAN PHAM", "add SAN PHAM thanh cong!");
    }

    public boolean deleteCTBHPT() {
        SQLiteDatabase db = this.getWritableDatabase();

        int doneDelete = 0;
        doneDelete = db.delete("CHITIETBAOHANHPHUTUNG", null , null);
        Log.w("Delete PHUTUNG", Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public boolean deleteBH(String input)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("BAOHANH", "MABH" + "=" + input, null) > 0;
    }

    public boolean deleteAllDonHangBaoHanh() {
        SQLiteDatabase db = this.getWritableDatabase();

        int doneDelete = 0;
        doneDelete = db.delete("DONDATHANG", null , null);
        Log.w("Delete DonHang", Integer.toString(doneDelete));
        return doneDelete > 0;

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
    public ArrayList<PhieuBaoHanhTemp> loadAllPhieuBH(String getInput) {
        ArrayList<PhieuBaoHanhTemp> phieuBaoHanhTemps = new ArrayList<>();

        String selectQuery = "SELECT BH.MABH AS MABH, X.TENXE AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHXE CTX ON CTX.MABH = BH.MABH "+
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE WHERE BH.MADH = '"+getInput+"'"+
                "UNION "+
                "SELECT BH.MABH AS MABH, PT.TENPT AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHPHUTUNG CTPT ON CTPT.MABH = BH.MABH "+
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT WHERE BH.MADH = '"+getInput+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhieuBaoHanhTemp phieuBaoHanhTemp = new PhieuBaoHanhTemp();

                phieuBaoHanhTemp.setMaBH(cursor.getString(0));
                Log.d("mabh", phieuBaoHanhTemp.getMaBH());

                phieuBaoHanhTemp.setTenSP(cursor.getString(1));
                Log.d("ten sp", phieuBaoHanhTemp.getTenSP());

                phieuBaoHanhTemp.setNgayBH(cursor.getString(2));
                Log.d("ngay bh", phieuBaoHanhTemp.getNgayBH());

                phieuBaoHanhTemps.add(phieuBaoHanhTemp);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return phieuBaoHanhTemps;
    }

    public ArrayList<ChiTietBaoHanh> loadAllPhieuXe(String getInput) {
        ArrayList<ChiTietBaoHanh> chiTietBaoHanhs = new ArrayList<>();
        ArrayList<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT BH.MABH AS MABH, X.TENXE AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHXE CTX ON CTX.MABH = BH.MABH "+
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE WHERE BH.MADH = '"+getInput+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ChiTietBaoHanh chiTietBaoHanh = new ChiTietBaoHanh();
                DanhSachSanPhamBaoHanh ds = new DanhSachSanPhamBaoHanh();
                chiTietBaoHanh.setMaBH(cursor.getString(0));
//                Log.d("mabh", cursor.getString(0));
                ds.setTenSP(cursor.getString(1));
                danhSachSanPhamBaoHanhs.add(ds);
                chiTietBaoHanh.setDanhSachSPBH(danhSachSanPhamBaoHanhs);
//                Log.d("ten sp", cursor.getString(1));
                chiTietBaoHanh.setNgayBH(cursor.getString(2));
//                Log.d("ngay bh", cursor.getString(2));
                chiTietBaoHanhs.add(chiTietBaoHanh);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return chiTietBaoHanhs;
    }

    public ArrayList<ChiTietBaoHanh> loadAllPhieuPT(String getInput) {
        ArrayList<ChiTietBaoHanh> chiTietBaoHanhs = new ArrayList<>();
        ArrayList<DanhSachSanPhamBaoHanh> danhSachSanPhamBaoHanhs = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT BH.MABH AS MABH, PT.TENPT AS TENSANPHAM, BH.NGAYBH AS NGAYTAO "+
                "FROM BAOHANH BH INNER JOIN CHITIETBAOHANHPHUTUNG CTPT ON CTPT.MABH = BH.MABH "+
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT WHERE BH.MADH = '"+getInput+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ChiTietBaoHanh chiTietBaoHanh = new ChiTietBaoHanh();
                DanhSachSanPhamBaoHanh ds = new DanhSachSanPhamBaoHanh();
                chiTietBaoHanh.setMaBH(cursor.getString(0));
//                Log.d("mabh", cursor.getString(0));
                ds.setTenSP(cursor.getString(1));
                danhSachSanPhamBaoHanhs.add(ds);
                chiTietBaoHanh.setDanhSachSPBH(danhSachSanPhamBaoHanhs);
//                Log.d("ten sp", cursor.getString(1));
                chiTietBaoHanh.setNgayBH(cursor.getString(2));
//                Log.d("ngay bh", cursor.getString(2));
                chiTietBaoHanhs.add(chiTietBaoHanh);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return chiTietBaoHanhs;
    }

    public List<String> get1Label(String getInput){
        List<String> list = new ArrayList<String>();

        String selectQuery = "SELECT X.TENXE AS TEN_SAN_PHAM " +
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATXE CTX ON CTX.MADH = DH.MADH " +
                "INNER JOIN XE X ON X.MAXE = CTX.MAXE " +
                "WHERE DH.MADH = '"+getInput+"' " +
                "UNION " +
                "SELECT PT.TENPT AS TEN_SAN_PHAM " +
                "FROM DONDATHANG DH INNER JOIN CHITIETDONDATPHUTUNG CTPT ON CTPT.MADH = DH.MADH " +
                "INNER JOIN PHUTUNG PT ON PT.MAPT = CTPT.MAPT " +
                "WHERE DH.MADH = '"+getInput+"' " +
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

    public String get1MaSP(String getInputName){
        String ma = "";
        String selectQuery = "SELECT MAXE AS MA " +
                "FROM XE WHERE TENXE = '"+getInputName+"' " +
                "UNION " +
                "SELECT MAPT AS MA " +
                "FROM PHUTUNG WHERE TENPT = '"+getInputName+"' " +
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

    public String get1MaBH(String getInputName){
        String ma = "";
        String selectQuery = "SELECT MABH FROM CHITIETBAOHANHXE " +
                "WHERE MAXE = '"+getInputName+"' " +
                "UNION " +
                "SELECT MABH FROM CHITIETBAOHANHPHUTUNG " +
                "WHERE MAPT = '"+getInputName+"'";
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

    public int setTonTaiTblXe(String inputValue){
        int check = 0;
        String selectQuery = "SELECT * FROM XE WHERE MAXE = '"+inputValue+"'";

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
        Log.d("TON TAI", inputValue +" trong Xe");
        return check;
    }

    public int setTonTaiMaBHtrongCTBH(String inputValue){
        int check = 0;
        String selectQuery = "SELECT MABH FROM CHITIETBAOHANHXE " +
                "WHERE MAXE = '"+inputValue+"' " +
                "UNION " +
                "SELECT MABH FROM CHITIETBAOHANHPHUTUNG " +
                "WHERE MAPT = '"+inputValue+"'";
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
        Log.d("TON TAI", inputValue +" da ton tai");
        return check;
    }

}