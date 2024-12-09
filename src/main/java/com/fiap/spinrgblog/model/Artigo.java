package com.fiap.spinrgblog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document //MONGO DOC
@Data //LOMBOK
public class Artigo {
    @Id
    private String codigo;
    private String titulo;
    private LocalDateTime data;
    private String texto;
    private String url;
    private Integer status;
}
