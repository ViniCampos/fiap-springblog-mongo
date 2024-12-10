package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/artigos")
public class ArtigoController implements ArtigoService {
    @GetMapping
    public List<Artigo> obterTodos() {
        return null;
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo) {
        return null;
    }

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo) {
        return null;
    }
}
