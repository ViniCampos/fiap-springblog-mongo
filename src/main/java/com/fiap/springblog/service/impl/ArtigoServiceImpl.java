package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    @Autowired
    private ArtigoRepository artigoRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private MongoTransactionManager transactionManager; //Injeção da lib que garante transação no mongo


    ///PARA CONSULTAS COMPLEXAS////
    private final MongoTemplate mongoTemplate;

    public ArtigoServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    ///////////////////////////////

    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Transactional(readOnly = true) //Apenas leitura
    @Override
    public Artigo obterPorCodigo(String codigo) {
        return artigoRepository
                .findById(codigo)
                .orElseThrow(()-> new IllegalArgumentException("Artigo não existe"));
    }

/*    @Transactional //Garante concorrência
    @Override
    public Artigo criar(Artigo artigo) {
        if(artigo.getAutor().getCodigo() != null) {
            Autor autor =
                    this.autorRepository
                            .findById(artigo.getAutor().getCodigo())
                            .orElseThrow(() -> new IllegalArgumentException("Autor não existente"));
            artigo.setAutor(autor);
        }else
            artigo.setAutor(null);

        return this.artigoRepository.save(artigo);
    }*/

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime dateTime) {
        Query query = new Query(Criteria.where("data").gt(dateTime));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime dateTime, Integer status) {
        Query query = new Query(
                Criteria.where("data").is(dateTime)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Artigo.class);
    }

    @Transactional
    @Override
    public void atualizar(Artigo updateArtigo) {
        try {
            this.artigoRepository.save(updateArtigo);
        } catch (OptimisticLockingFailureException ex) {
            //DESENVOLVER ESTRATÉGIA
            //1. Recuperar o documento mais recente
            Artigo atualizado = this.artigoRepository.findById(updateArtigo.getCodigo()).orElse(null);
            if(atualizado != null) {
                //2. Atualizar os campos desejados
                atualizado.setTitulo(updateArtigo.getTitulo());
                atualizado.setTexto(updateArtigo.getTexto());
                atualizado.setStatus(updateArtigo.getStatus());
                atualizado.setData(updateArtigo.getData());

                //3. Incrementar a versão manualmente
                atualizado.setVersion(atualizado.getVersion()); //Erro tratado, comentar essa linha para ver o erro

                //Tentar salvar
                this.artigoRepository.save(atualizado);
            } else
                throw  new RuntimeException("Artigo nãoo encontrado" + updateArtigo.getCodigo());
        }
    }

    //Update Complexo, sempre utilizar assim por questão de segurança
    @Transactional
    @Override
    public void atualizarArtigo(String Id, String novaURL) {
        System.out.println("NOVA URL ---------------------- >" + novaURL);
        //Cria critério + condição de update
        Query query = new Query(Criteria.where("_id").is(Id));
        Update update = new Update().set("url",novaURL);
        //Executa comando
        this.mongoTemplate.updateFirst(query,update,Artigo.class);
    }

    //DELTA USANDO ARTIGO REPOSITORY
    @Transactional
    @Override
    public void deleteById(String id) {
        this.artigoRepository.deleteById(id);
    }

    //DELTA USANDO ARTIGO USANDO MONGOTEMPLATE
    @Override
    public void deleteArtigoById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        this.mongoTemplate.remove(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer Status, LocalDateTime data) {
        return this.artigoRepository.findByStatusAndDataGreaterThan(Status, data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return this.artigoRepository.obterArtigoPorDataHora(de, ate);
    }

    @Override
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo) {
        Criteria criteria = new Criteria();
        criteria.and("data").lte(data);
        if (status != null) {
            criteria.and("Status").is(status);
        }
        if(titulo != null && !titulo.isEmpty()) {
            criteria.and("titulo").regex(titulo, "i"); //ignora capital letters
        }
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public Page<Artigo> listaArtigosPaginado(Pageable pageable) {
        Sort sort = Sort.by("titulo").ascending();
        Pageable paginacao = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
                );
        return this.artigoRepository.findAll(paginacao);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer Status) {
        return this.artigoRepository.findByStatusOrderByTituloAsc(Status);
    }

    @Override
    public List<Artigo> obterArtigoPorStatusComOrdenacao(Integer Status) {
        return this.artigoRepository.obterArtigoPorStatusComOrdenacao(Status);
    }

    @Override
    public List<Artigo> findByTexto(String searchTerm) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm); //MongoDB
        Query query = TextQuery.queryText(criteria).sortByScore(); //Otimização para retornar o que mais importa
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() { //Contar quantidades e armazenar em DTO
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.group("status").count().as("quantidade"), //Agrupar
                Aggregation.project("quantidade").and("status").previousOperation() //O que será exibido da consulta
        );
        AggregationResults<ArtigoStatusCount> result = mongoTemplate.aggregate(
                aggregation,
                ArtigoStatusCount.class);

        return result.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.match( //FILTRO
                        Criteria.where("data")
                                .gte(dataInicio.atStartOfDay()) //Dia do servidor às 00h
                                .lte(dataFim.atStartOfDay())
                ),
                Aggregation.group("autor").count().as("totalArtigos"), //AGRUPAMENTO
                Aggregation.project("totalArtigos").and("autor").previousOperation() //Sempre da direita para esquerda
        );
        AggregationResults<AutorTotalArtigo> result = mongoTemplate.aggregate(
                aggregation,
                AutorTotalArtigo.class
        );
        return result.getMappedResults();
    }

    //////////////////////// AULA 5 //////////////////////////////
    @Override
    public ResponseEntity<?> criar(Artigo artigo) {
        if(artigo.getAutor().getCodigo() != null) {
            Autor autor =
                    this.autorRepository
                            .findById(artigo.getAutor().getCodigo())
                            .orElseThrow(() -> new IllegalArgumentException("Autor não existente"));
            artigo.setAutor(autor);
        }else
            artigo.setAutor(null);

        try {
            this.artigoRepository.save(artigo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateKeyException e) {  //CASO TENTAR ADICIONAR UM DOCUMENTO QUE JÁ EXISTE
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Artigo já existe na coleção!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar artigo ->  " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> atualizarArtigo(String id, Artigo artigo) {
        try{
            //Verifica se existe
            Artigo existenteArtigo = this.artigoRepository.findById(id).orElse(null);
            if(existenteArtigo == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado na coleção");

            //Caso exista, atualizar alguns dados
            existenteArtigo.setTitulo(artigo.getTitulo());
            existenteArtigo.setData(artigo.getData());
            existenteArtigo.setTexto(artigo.getTexto());

            //Atualiza artigo
            this.artigoRepository.save(existenteArtigo);
            return ResponseEntity.status(HttpStatus.OK).body(existenteArtigo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao editar artigo ->  " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> criarArtigoComAutor(Artigo artigo, Autor autor) {
        //O mongo por natureza não é um banco transacional, portanto precisamos implementar isso via aplicação
        //Utilizar a BEAN Injetada
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
           try {
               //Iniciar transação
               autorRepository.save(autor);
               artigo.setData(LocalDateTime.now());
               artigo.setAutor(autor);
               artigoRepository.save(artigo);
           } catch (Exception e){
               //Trata o erro e lança a transação de volta
               status.setRollbackOnly();
               throw new RuntimeException("Erro ao criar artigo com autor: " + e.getMessage());
           }
           return  null;
        });
        return ResponseEntity.status(HttpStatus.OK).body(artigo);
    }

}
