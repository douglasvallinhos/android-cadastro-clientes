package com.example.carolinevallinhos.model;

import java.io.Serializable;
import java.util.Arrays;

public class Contato implements Serializable {
    private static  final long serialVersionUID = 1L;

    public long id;
    public String nome;
    public String email;
    public String telefone;
    public String nascimento;
    public String detalhe;
    public String objetivo;
    public byte[] imagem;

    @Override
    public String toString() {
        return "Contato{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", nascimento='" + nascimento + '\'' +
                ", detalhe='" + detalhe + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", imagem=" + Arrays.toString(imagem) +
                '}';
    }
}
