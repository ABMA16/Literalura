package com.alura.literalura.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    private Long idLibro;

    @Column(unique = true) //There can not be any duplicated title in the same column
    private String titulo;
    private String lenguaje;
    private Long numeroDescargas;

    @ManyToOne //This annotation means that many books can have a same author
    @JoinColumn(name = "idAutor", nullable = false) //The id has to be linked with the author.
    private Autor autor;

    //Constructor to add a new book from the response.
    public Libro(DatosLibro datosLibro){
        this.idLibro = datosLibro.idLibro();
        this.titulo = datosLibro.titulo();
        this.lenguaje = datosLibro.lenguaje().get(0);
        this.numeroDescargas = datosLibro.numeroDescargas();
    }

    //Simple constructor required by JPA
    public Libro() {
    }

    //Getters and Setters
    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Long getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Long numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}
