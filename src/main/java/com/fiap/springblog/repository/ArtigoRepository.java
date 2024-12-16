package com.fiap.springblog.repository;

import com.fiap.springblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    //Método já criado pelo Mongo Repository, portanto precisa ser declarado aqui
    public void deleteById(String id);
}
