package com.example.proj_api_retrofit.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_api_retrofit.R;

public class AlunoHolder extends RecyclerView.ViewHolder {
    public TextView nome;
    public TextView ra;
    public TextView cep;
    public ImageView btnExcluir;
    public ImageView btnEditar;

    public AlunoHolder(View itemView) {
        super(itemView);
        nome = (TextView) itemView.findViewById(R.id.txtNome);
        ra = (TextView) itemView.findViewById(R.id.txtRa);
        cep = (TextView) itemView.findViewById(R.id.txtCep);
        btnExcluir = (ImageView) itemView.findViewById(R.id.btnExcluir);
        btnEditar = (ImageView) itemView.findViewById(R.id.btnEditar);
    }
}
