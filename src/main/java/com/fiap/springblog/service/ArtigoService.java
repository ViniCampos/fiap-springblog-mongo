package com.fiap.springblog.service;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.AutorTotalArtigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

    public List<Artigo> obterTodos();
    public Artigo obterPorCodigo(String codigo);
    // INITIATING SOLVING PROBLEMS - AULA 5
    //    public Artigo criar(Artigo artigo);
    public List<Artigo> findByDataGreaterThan(LocalDateTime dateTime);
    public List<Artigo> findByDataAndStatus(LocalDateTime dateTime, Integer status);
    public void atualizar(Artigo updateArtigo);
    public void atualizarArtigo(String Id, String novaURL);
    public void deleteById(String id);
    public void deleteArtigoById(String id);
    public List<Artigo> findByStatusAndDataGreaterThan(Integer Status, LocalDateTime data);
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo);
    public Page<Artigo> listaArtigosPaginado(Pageable pageable);
    public List<Artigo> findByStatusOrderByTituloAsc(Integer Status);
    public List<Artigo> obterArtigoPorStatusComOrdenacao(Integer Status);
    public List<Artigo> findByTexto(String searchTerm);
    public List<ArtigoStatusCount> contarArtigosPorStatus();
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDate dataInicio, LocalDate dataFim);


    //////////////////////// AULA 5 //////////////////////////////
    public ResponseEntity<?> criar(Artigo artigo);
    public ResponseEntity<?> atualizarArtigo(String id, Artigo artigo);


}
