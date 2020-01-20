package com.example.carolinevallinhos.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContatoBD extends SQLiteOpenHelper {

    private static final String TAG = "contatobd";
    private static final String NAME = "contato.sqlite";
    private static final int VERSION = 1;
    private static ContatoBD contatoBD;

    private ContatoBD(Context context) {
        super(context, NAME, null, VERSION);
        getWritableDatabase();
    }
    public static ContatoBD getInstance(Context context){
        if(contatoBD == null){
            contatoBD = new ContatoBD(context);
            return contatoBD;
        }
        return contatoBD;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists contato " +
                "( _id integer primary key autoincrement, " +
                "nome text, " +
                "email text, " +
                "telefone text, " +
                "nascimento text, " +
                "detalhe text, " +
                "objetivo text, " +
                "imagem blob);";
        Log.d(TAG, "Criando a tabela contato. Aguarde...");
        sqLiteDatabase.execSQL(sql);
        Log.d(TAG, "Tabela contato criada com sucesso");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long save(Contato contato){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("nome", contato.nome);
            values.put("email", contato.email);
            values.put("telefone", contato.telefone);
            values.put("nascimento", contato.nascimento);
            values.put("detalhe", contato.detalhe);
            values.put("objetivo", contato.objetivo);
            values.put("imagem", contato.imagem);
            if(contato.id == 0){
                return sqLiteDatabase.insert("contato", null, values);
            }else{
                values.put("_id", contato.id);
                return sqLiteDatabase.update("contato", values, "_id=" + contato.id, null);
            }
        }finally {
            sqLiteDatabase.close();
        }
    }

    public long delete(Contato contato){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try{
            return sqLiteDatabase.delete("contato","_id=?", new String[]{String.valueOf(contato.id)});
        }finally {
            sqLiteDatabase.close();
        }
    }

    public List<Contato> getAll(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        try{
            return toList(sqLiteDatabase.rawQuery("select * from contato", null));
        }finally {
            sqLiteDatabase.close();
        }
    }

    public List<Contato> getByName(String nome){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try{
            return toList(sqLiteDatabase.rawQuery("select * from contato where nome like'" + nome + "%'",null));
        }finally {
            sqLiteDatabase.close();
        }
    }

    public  List<Contato> toList(Cursor cursor){

        List<Contato> contatos = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Contato contato = new Contato();
                contato.id = cursor.getLong(cursor.getColumnIndex("_id"));
                contato.nome =  cursor.getString(cursor.getColumnIndex("nome"));
                contato.email =  cursor.getString(cursor.getColumnIndex("email"));
                contato.telefone =  cursor.getString(cursor.getColumnIndex("telefone"));
                contato.nascimento =  cursor.getString(cursor.getColumnIndex("nascimento"));
                contato.detalhe =  cursor.getString(cursor.getColumnIndex("detalhe"));
                contato.objetivo =  cursor.getString(cursor.getColumnIndex("objetivo"));
                contato.imagem = cursor.getBlob(cursor.getColumnIndex("imagem"));

                contatos.add(contato);

            }while (cursor.moveToNext());
        }

        return contatos;
    }
}
