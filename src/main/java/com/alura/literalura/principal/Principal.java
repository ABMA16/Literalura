package com.alura.literalura.principal;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.RespuestaApi;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLenguaje;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Lenguaje;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String URL_LANGUAGE_CODE = "https://wiiiiams-c.github.io/language-iso-639-1-json-spanish/language-iso-639-1.json";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;
    private Libro libro;
    private List<Libro> libros;
    private List<Autor> autores;
    private Optional<Autor> autor;

    public Principal(AutorRepository repositoryAutor, LibroRepository repositoryLibro) {
        this.repositoryAutor = repositoryAutor;
        this.repositoryLibro = repositoryLibro;
    }

    //Menu loop
    public void mostrarMenu(){
        var opcionMenu = -1;

        while(opcionMenu != 0){
            var menu = """
                    ___________________________________________
                                Bienvenido a Literalura
                    ___________________________________________
                                Seleccione una opción:
                    ___________________________________________
    
                    1 -> Consultar libro por titulo
                    2 -> Mostrar libros en la base de datos
                    3 -> Mostrar autores en la base de datos
                    4 -> Listar autores vivos en un año determinado
                    5 -> Listar libros por lenguaje original
                    6 -> Top 10 Libros mas descargados en la base de datos
                    7 -> Estadisticas
    
                    0 -> Salir
                    """;

            try {
                System.out.println(menu);
                opcionMenu = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                teclado.nextLine();
                opcionMenu = -1;
            }

            switch (opcionMenu) {
                case 0:
                    System.out.println("\nCerrando aplicacion...\n");
                    break;
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    consultarAutoresVivosPorFecha();
                    break;
                case 5:
                    consultarLibrosPorLenguaje();
                    break;
                case 6:
                    listarTopTenLibros();
                    break;
                case 7:
                    muestraEstadisticasLiteralura();
                    break;
                default:
                    System.out.println("\nOpcion invalida\n");
            }
        }
    }

    private void muestraEstadisticasLiteralura() {
        libros = repositoryLibro.findAll();

        var headerEstadisticas = """
                -------------------------------------------
                    Datos estadisticos Literalura
                -------------------------------------------

                Total de libros       : %s
                Libro mas descargado  : %s
                Libro menos descargado: %s
                Media de descargas    : %s

                """;

        LongSummaryStatistics estadisticas = libros.stream()
                .filter(l -> l.getNumeroDescargas() > 0)
                .collect(Collectors.summarizingLong(Libro::getNumeroDescargas));

        System.out.println(headerEstadisticas.formatted(
                estadisticas.getCount(),
                estadisticas.getMax() + " -> " + libros.get(0).getTitulo(),
                estadisticas.getMin() + " -> " + libros.get(9).getTitulo(),
                Math.round(estadisticas.getAverage())));
    }

    private void listarTopTenLibros() {
        libros = repositoryLibro.findTop10ByOrderByNumeroDescargasDesc();

        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros para mostrar en este top...\n");
        } else {
            var headerTopTen = """
                _________________________________________________
                Top 10 libros mas descargados en la base de datos
                _________________________________________________
                """;
            System.out.println("\n" + headerTopTen + "\n");
            datosLibro(libros);
        }
    }

    private void mostrarLibros() {
        libros = repositoryLibro.findAll();

        var headerListaLibros = """
                -------------------------------------------
                    Libros almacenados en literalura
                -------------------------------------------
                """;
        System.out.println("\n" + headerListaLibros + "\n");

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en literalura...\n");
        } else {
            var cuentaLibros = libros.size();
            datosLibro(libros);
            System.out.println("Total de libros: %s\n".formatted(cuentaLibros));
        }
    }

    private void consultarAutoresVivosPorFecha() {
        try {
            System.out.println("\nIngrese año a consultar:\n");
            int fecha = teclado.nextInt();
            autores = repositoryAutor.obtenerAutorVivoFecha(fecha);

            var headerFechaAutor = """
                    ___________________________________________
                        Autores vivos para el año ingresado
                    ___________________________________________
                    """;
            System.out.println("\n" + headerFechaAutor);

            if (autores.isEmpty()) {
                System.out.println("No hay autores vivos para el año ingresado...\n");
            } else {
                datosAutor(autores);
                System.out.println("Total de autores: %s\n".formatted(autores.size()));
            }
        } catch (InputMismatchException e) {
            System.out.println("\nEl año ingresado no es un valor valido.\n");
            teclado.nextLine();
        }
    }

    private void consultarLibrosPorLenguaje() {
        var lenguajesLibro = repositoryLibro.obtenerListaUnicaLenguaje();
        var jsonLenguajes = consumoApi.obtenerDatos(URL_LANGUAGE_CODE);
        var datosLenguaje = conversor.obtenerDatos(jsonLenguajes, DatosLenguaje.class);
        List<Lenguaje> lenguajeDisponible = new ArrayList<>();

        if (lenguajesLibro.isEmpty()) {
            System.out.println("\nNo hay libros para listar.\n");
        } else {
            for (String codigoLenguaje : lenguajesLibro) {
                var datosLen = datosLenguaje.lenguajes().stream()
                        .filter(i -> i.codigoLenguaje()
                                .contains(codigoLenguaje))
                        .collect(Collectors.toList());
                lenguajeDisponible.add(datosLen.get(0));
            }

            var headerListaLenguajes = """
                _______________________________________________
                Lista lenguajes disponibles en la base de datos
                _______________________________________________
                """;
            System.out.println("\n"+headerListaLenguajes);
            lenguajeDisponible.forEach(i ->
                    System.out.println(i.codigoLenguaje() + " -> " + i.lenguaje()));
            System.out.println("\nEscribe el codigo de lenguaje a buscar ej: en (para ingles) o es (para español)\n");
            String inputCodigoLenguaje = teclado.nextLine();

            if (revisaInputTeclado(inputCodigoLenguaje)) {
                System.out.println("\nEl codigo ingresado no es válido\n");
            } else {
                libros = repositoryLibro.findByLenguaje(inputCodigoLenguaje);

                if (libros.isEmpty()) {
                    System.out.println("\nNo hay libros en ese lenguaje en la base de datos literalura...\n");
                } else {
                    var cuentaLibros = libros.size();
                    datosLibro(libros);
                    System.out.println("Total de libros: %s\n".formatted(cuentaLibros));
                }
            }
        }
    }

    private void mostrarAutores() {
        var headerListaAutor = """
                -------------------------------------------
                    Autores registrados en literalura
                -------------------------------------------
                """;
        System.out.println("\n" + headerListaAutor + "\n");
        autores = repositoryAutor.findAllByOrderByNombreAsc();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en literalura.\n");
        } else {
            var cuentaAutores = autores.size();
            datosAutor(autores);
            System.out.println("Total de autores: %s\n".formatted(cuentaAutores));
        }
    }

    private void buscarLibro() {
        System.out.println("\nIngrese el título del libro que desea buscar: ");

        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, RespuestaApi.class);

        Optional<DatosLibro> libroBuscado = datosBusqueda.libros().stream()
                .filter(l ->
                        l.titulo()
                                .toLowerCase()
                                .contains(nombreLibro
                                        .toLowerCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosAutor datosAutor = libroBuscado.get().autor().getFirst();

            //Valida existencia libro en la DB
            if (repositoryLibro.findByIdLibro(libroBuscado.get().idLibro()).isPresent()){
                System.out.println("\nLibro ya se encuentra guardado en la base de datos...\n");
            } else {
                System.out.println("\nLibro encontrado y agregado a la base de datos:\n");

                var libroHeaderEncontrado = """
                    -------------------------
                        Datos del Libro
                    -------------------------
                    Titulo   : %s
                    Autor    : %s
                    Idiomas  : %s
                    Descargas: %s
                    """;

                System.out.println(libroHeaderEncontrado.formatted(
                                libroBuscado.get().titulo(),
                                libroBuscado.get().autor().get(0).nombre(),
                                libroBuscado.get().lenguaje().get(0),
                                libroBuscado.get().numeroDescargas()
                        )
                );

                //Valida la existencia del autor en la base de datos
                autor = repositoryAutor.findByNombre(datosAutor.nombre());

                if (autor.isPresent()) {
                    libro = new Libro(libroBuscado.get());
                    libro.setAutor(autor.get());
                    repositoryLibro.save(libro);
                } else {
                    libros = libroBuscado.stream()
                            .map(l -> new Libro(l))
                            .collect(Collectors.toList());

                    Autor autorClass = new Autor(datosAutor);
                    autorClass.setLibros(libros);
                    repositoryAutor.save(autorClass);
                }
                System.out.println("\n Se ha guardado el libro en la base de datos.\n");
            }
        } else {
            System.out.println("\nNo se ha encontrado el libro o no es una busqueda válida.\n");
        }
    }

    private void datosLibro(List<Libro> listaAutores){
        var muestraLibro = """
            
            _________________________
                Datos del Libro
            _________________________
            Titulo             : %s
            Autor              : %s
            Lenguaje original  : %s
            Descargas          : %s
            """;

        listaAutores.forEach(l -> System.out.println(
                muestraLibro.formatted(
                        l.getTitulo(),
                        l.getAutor().getNombre(),
                        l.getLenguaje(),
                        l.getNumeroDescargas()
                )
        ));
    }

    private void datosAutor(List<Autor> listaAutores){
        var muestraAutor = """
            _________________________
                Datos del Autor
            _________________________
            Nombre             : %s
            Nacimiento         : %s
            Natalicio          : %s
            Libros escritos    : %s
            """;

        listaAutores.forEach(a -> System.out.println(
                muestraAutor.formatted(
                        a.getNombre(),
                        a.getFechaNacimiento()==null?"N/D":a.getFechaNacimiento(),
                        a.getFechaMuerte()==null?"N/D":a.getFechaMuerte(),
                        repositoryLibro.obtenerLibrosPorAutor(a.getIdAutor()).stream()
                                .map(l -> l.getTitulo())
                                .collect(Collectors.joining(" | ", "[", "]")))
        ));
    }

    private boolean revisaInputTeclado(String inputTeclado){
        //revisa si la variable recibida es un numero postitivo o negativo
        Pattern expresionRegular = Pattern.compile("^[\\+-]?\\d+$");
        Matcher haceMatch = expresionRegular.matcher(inputTeclado);
        return haceMatch.matches();
    }
}
