package com.fiap.springblog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Autor {
    @Id
    private String codigo;
    private String nome;
    private String biografia;
    private String imagem;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", biografia='" + biografia + '\'' +
                ", imagem='" + imagem + '\'' +
                '}';
    }
}
