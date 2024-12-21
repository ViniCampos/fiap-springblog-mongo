package com.fiap.springblog.repository;

import com.fiap.springblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    //Método já criado pelo Mongo Repository, portanto precisa ser declarado aqui
    public void deleteById(String id);

    //INITIATING ADVANCED QUERY
    public List<Artigo> findByStatusAndDataGreaterThan(Integer Status, LocalDateTime data);

    @Query("{ $and: [ {'data':{$gte: ?0}}, {'data':{$lte: $1}} ]}")
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);
    //http://localhost:8080/artigos/periodoo?de=2023-12-10T15:34:56.000&ate=2025-12-10T15:34:56.000


}
