package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController {
    @Autowired
    private ArtigoService artigoService;

    @GetMapping
    public List<Artigo> obterTodos() {
        return this.artigoService.obterTodos();
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo) {
        return artigoService.obterPorCodigo(codigo);
    }

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo) {
        System.out.println("Controller" + artigo.toString());
        return this.artigoService.criar(artigo);
    }

    @GetMapping("/maiordata")
    public List<Artigo> findByDataGreaterThan(@RequestParam("data") LocalDateTime dateTime) {
        return  this.artigoService.findByDataGreaterThan(dateTime);
    }

    @GetMapping("/data-status")
    public List<Artigo> findByDataAndStatus(
            @RequestParam("data") LocalDateTime dateTime,
            @RequestParam("status") Integer status) {
        return this.artigoService.findByDataAndStatus(dateTime, status);
    }

    @PutMapping
    public void atualizar(@RequestBody Artigo updateArtigo) {
        this.artigoService.atualizar(updateArtigo);
    }

    @PutMapping("/{Id}")
    public void atualizarArtigo(@PathVariable String Id, @RequestBody String novaURL) {
        System.out.println("NOVA URL ---------------------- >" + novaURL);
        this.artigoService.atualizarArtigo(Id, novaURL);
    }

    @DeleteMapping("/{id}")
    public void deleteArtigo(@PathVariable String id) {
        this.artigoService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public void deleteArtigoById(@RequestParam("Id") String id) {
        this.artigoService.deleteArtigoById(id);
    }

    //USING QUERY METHOD FROM REPOSITORY
    @GetMapping("/status-maiordata")
    public List<Artigo> findByStatusAndDataGreaterThan(
            @RequestParam("status") Integer Status,
            @RequestParam("data") LocalDateTime data){
        return this.artigoService.findByStatusAndDataGreaterThan(Status, data);
    }

    @GetMapping("/periodo")
    public List<Artigo> obterArtigoPorDataHora(@RequestParam("de") LocalDateTime de,
                                               @RequestParam("ate") LocalDateTime ate) {
        return this.artigoService.obterArtigoPorDataHora(de, ate);
    }
    @GetMapping("/artigo-complexo")
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo) {
        System.out.println(data);
        return this.artigoService.encontrarArtigosComplexos(status, data, titulo);
    }

    @GetMapping("/pagina-artigos")
    public ResponseEntity<Page<Artigo>> listaArtigosPaginado(Pageable pageable) {
        Page<Artigo> artigos = this.artigoService.listaArtigosPaginado(pageable);
        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/status-ordenado")
    public List<Artigo> findByStatusOrderByTituloAsc(@RequestParam("status") Integer Status) {
        return this.artigoService.findByStatusOrderByTituloAsc(Status);
    }

}
