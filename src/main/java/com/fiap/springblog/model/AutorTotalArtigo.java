package com.fiap.springblog.model;

public class AutorTotalArtigo {
    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Long getTotalArtigos() {
        return totalArtigos;
    }

    public void setTotalArtigos(Long totalArtigos) {
        this.totalArtigos = totalArtigos;
    }

    private Autor autor;
    private Long totalArtigos;
}
