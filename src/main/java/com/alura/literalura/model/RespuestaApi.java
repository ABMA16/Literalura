package com.alura.literalura.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //With this command the record ignores the not mapped attributes from the response
public record RespuestaApi(
        @JsonAlias("results") List<DatosLibro> libros
) {
}
