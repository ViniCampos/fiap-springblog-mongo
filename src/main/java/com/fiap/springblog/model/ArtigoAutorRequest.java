package com.fiap.springblog.model;

public class ArtigoAutorRequest {
    private Artigo artigo;
    private Autor autor;

    public Artigo getArtigo() {
        return artigo;
    }

    public void setArtigo(Artigo artigo) {
        this.artigo = artigo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
