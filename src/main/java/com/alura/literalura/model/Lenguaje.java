package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Lenguaje(
        @JsonAlias("lenguage") String lenguaje,
        @JsonAlias("codigo") String codigoLenguaje
) {

}
