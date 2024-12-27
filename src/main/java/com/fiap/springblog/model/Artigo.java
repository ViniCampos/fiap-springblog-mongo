package com.fiap.springblog.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document //MONGO DOC
public class Artigo {
    @Id
    private String codigo;

    //VALIDAÇÃO
    @NotBlank(message = "O título do artigo não pode estar em branco.")
    private String titulo;

    @NotNull(message = "A data do artigo não pode ser nula.")
    private LocalDateTime data;

    @NotBlank(message = "O texto do artigo não pode estar em branco.")
    @TextIndexed //Indexa texto para permitir o search
    private String texto;
    //No mongo criar o index
    // db.artigo.createIndex({
    //  texto: 'text'
    // })

    private String url;

    @NotNull(message = "O status do artigo não pode ser nulo.")
    private Integer status;

    @DBRef
    private Autor autor;

    @Version
    private Long version; //CONTROLE DE CONCORRÊNCIA

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Artigo{" +
                "codigo='" + codigo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", data=" + data +
                ", texto='" + texto + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                '}';
    }
}
