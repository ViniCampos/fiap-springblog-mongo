package com.fiap.springblog.service;

import com.fiap.springblog.model.Autor;

import java.util.List;

public interface AutorService {
    public Autor criar(Autor autor);
    public Autor obterCodigo(String codigo);
    public List<Autor> obterTodosAutores();
}
