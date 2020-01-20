package com.example.carolinevallinhos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carolinevallinhos.R;
import com.example.carolinevallinhos.model.Contato;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Contato> {

    public ListViewAdapter(Context context, List<Contato> contatos) {
        super(context, 0, contatos);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contato contato = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_layout, parent, false);

        TextView tvNome = (TextView) convertView.findViewById(R.id.tv_item_nome);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tv_item_email);
        TextView tvTelefone = (TextView) convertView.findViewById(R.id.tv_item_telefone);
        TextView tvNascimento = (TextView) convertView.findViewById(R.id.tv_item_nascimento);
        TextView tvDetalhe = (TextView) convertView.findViewById(R.id.tv_item_detalhe);
        TextView tvObjetivo = (TextView) convertView.findViewById(R.id.tv_item_objetivo);


        tvNome.setText(contato.nome);
        tvEmail.setText(contato.email);
        tvTelefone.setText(contato.telefone);
        tvNascimento.setText(contato.nascimento);
        tvDetalhe.setText(contato.detalhe);
        tvObjetivo.setText(contato.objetivo);

        ImageView imvImagem = (ImageView) convertView.findViewById(R.id.imv_item);
        if(contato.imagem != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(contato.imagem, 0, contato.imagem.length);
            imvImagem.setImageBitmap(bitmap);
        }else{
            imvImagem.setImageResource(R.drawable.foto_sombra);
        }

        if(position % 2 == 0){
        }else{
            convertView.setBackgroundResource(R.drawable.gradient);

        }
        return convertView;
    }
}
