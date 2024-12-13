package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
