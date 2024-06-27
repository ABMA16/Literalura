package com.alura.literalura.model;

//Librerias
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity  //JPA entity
@Table(name = "autores") //Creates a table in out DB.
public class Autor {

    @Id //JPA recognizes this Id as part of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) //The ID has to be unique
    private Long idAutor;

    @Column(unique = true)
    private String nombre;

    private Integer fechaNacimiento;
    private Integer fechaMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER) //One author can have multiple books
    private List<Libro> libros;

    //Constructor to add an author from the API Response
    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaMuerte = datosAutor.fechaMuerte();
    }

    //Simple constructor required by JPA
    public Autor() {
    }

    //Getters and setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }
}