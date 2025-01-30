package pt.ipleiria.estg.dei.amsi.mobilesportwine.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class VinhoBDHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "dbVinhos";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "Vinhos";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String ID = "id";

    private final SQLiteDatabase db;

    public VinhoBDHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createVinhoTable = "CREATE TABLE " + TABLE_NAME +
                "(" + ID + " INTEGER, " +
                NAME + " TEXT NOT NULL, " +
                DESCRIPTION + " TEXT NOT NULL, " +
                PRICE + " REAL NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(createVinhoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);

    }

    public void adicionarVinhoBD (Vinho l){
        ContentValues values = new ContentValues();
        values.put(NAME, l.getName());
        values.put(DESCRIPTION, l.getDescription());
        values.put(PRICE, l.getPrice());

        this.db.insert(TABLE_NAME, null, values);
    }

    public boolean editarVinhoBD(Vinho l){
        ContentValues values= new ContentValues();
        values.put(NAME, l.getName());
        values.put(DESCRIPTION, l.getDescription());
        values.put(PRICE, l.getPrice());

        return this.db.update(TABLE_NAME,values, ID + "=?", new String[]{""+l.getId()})>0;

    }

    public boolean removerVinhoBD(int id){
        return (this.db.delete(TABLE_NAME, ID + " = ?", new String[]{"" + id})==1);
    }

    public ArrayList<Vinho> getAllVinhosBD(){
        ArrayList<Vinho> Vinhos = new ArrayList<>();

        Cursor cursor = this.db.query(TABLE_NAME, new String[]{ID,NAME,DESCRIPTION,PRICE},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                Vinho auxVinho = new Vinho(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2), cursor.getDouble(3));

                Vinhos.add(auxVinho);
            }while(cursor.moveToNext());
        }
        return Vinhos;
    }

    public void removerAllVinhosBD(){
        this.db.delete(TABLE_NAME, null, null);
    }
}
