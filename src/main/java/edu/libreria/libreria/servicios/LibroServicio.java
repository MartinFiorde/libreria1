package edu.libreria.libreria.servicios;

import edu.libreria.libreria.entidades.Libro;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.repositorios.LibroRepositorio;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {

    private LibroRepositorio libroRepositorio;
    private AutorServicio autorServicio;
    private EditorialServicio editorialServicio;

    @Autowired
    public LibroServicio(LibroRepositorio libroRepositorio, AutorServicio autorServicio, EditorialServicio editorialServicio) {
        this.libroRepositorio = libroRepositorio;
        this.autorServicio = autorServicio;
        this.editorialServicio = editorialServicio;
    }

    public void registrar(String titulo, Long isbn, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, String nombreAutor, String nombreEditorial) throws ErrorServicio {
        Libro libro = validarTituloIsbnAnioEjemplares(titulo, isbn, anio, ejemplares, ejemplaresPrestados);
        libro.setAutor(autorServicio.validarExistenciaAutor(nombreAutor));
        libro.setEditorial(editorialServicio.validarExistenciaEditorial(nombreEditorial));
        libro.setAlta(Boolean.TRUE);
        libro.setFechaAlta(new Date());
        libroRepositorio.save(libro);
    }

    public void modificar(String id, String titulo, Long isbn, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, String nombreAutor, String nombreEditorial) throws ErrorServicio {
        Libro libro = validarId(id);
        libro = validarTituloIsbnAnioEjemplares(titulo, isbn, anio, ejemplares, ejemplaresPrestados);
        libro.setAutor(autorServicio.validarExistenciaAutor(nombreAutor));
        libro.setEditorial(editorialServicio.validarExistenciaEditorial(nombreEditorial));
        libroRepositorio.save(libro);
    }

    public void eliminar(String id) throws ErrorServicio {
        Libro libro = validarId(id);
        libro.setAlta(Boolean.FALSE);
        libroRepositorio.save(libro);
    }

    public void reactivar(String id) throws ErrorServicio {
        Libro libro = validarId(id);
        libro.setAlta(Boolean.TRUE);
        libroRepositorio.save(libro);
    }

    public Libro validarId(String id) throws ErrorServicio {
        Optional<Libro> res = libroRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro el libro solicitado.");
        }
        return res.get();
    }

    public Libro validarTituloIsbnAnioEjemplares(String titulo, Long isbn, Integer anio, Integer ejemplares, Integer ejemplaresPrestados) throws ErrorServicio {

        // VALIDACIONES
        if (titulo.toLowerCase() == null || titulo.isEmpty() || titulo.trim() == "") {
            throw new ErrorServicio("Debe ingresar un titulo válido.");
        }
        if (libroRepositorio.buscarTituloEqual(titulo.toLowerCase()) != null) {
            throw new ErrorServicio("Ya existe un libro con el titulo seleccionado.");
        }

        if (isbn == null || isbn.toString().isEmpty() || isbn == 0) {
            throw new ErrorServicio("Debe ingresar un isbn válido.");
        }
        if (libroRepositorio.buscarIsbnEqual(isbn.toString()) != null) {
            throw new ErrorServicio("Ya existe un libro con el isbn seleccionado.");
        }

        if (anio == null || anio.toString().isEmpty() || anio < 0 || anio > (LocalDate.now().getYear() + 1)) {
            throw new ErrorServicio("Debe ingresar un año válido.");
        }

        if (ejemplares == null || ejemplares.toString().isEmpty() || ejemplares < 0) {
            throw new ErrorServicio("Debe ingresar una cantidada de ejemplares válida.");
        }

        if (ejemplaresPrestados == null || ejemplaresPrestados.toString().isEmpty() || ejemplaresPrestados < 0 || ejemplaresPrestados > ejemplares) {
            throw new ErrorServicio("Debe ingresar una cantidada de ejemplares prestados válida.");
        }

        // CARGA A OBJETO
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setIsbn(isbn);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
        return libro;
    }

    public Libro validarExistenciaLibro(String titulo) throws ErrorServicio {

        if (libroRepositorio.buscarTituloEqual(titulo.toLowerCase()) == null) {
            throw new ErrorServicio("No existe un libro con el titulo seleccionado. Por favor ingrese el libro e intente nuevamente");
        }

        if (libroRepositorio.buscarTitulosLike(titulo.toLowerCase()).size() > 1 && libroRepositorio.buscarTitulosLike(titulo.toLowerCase()).size() < 11) {
            List<Libro> libroes = libroRepositorio.buscarTitulosLike(titulo.toLowerCase());
            String tabOpciones = "Por favor ingrese un libro de entre las siguientes opciones:";
            for (Libro aux : libroes) {
                tabOpciones.concat("\n" + aux.getTitulo());
            }
            throw new ErrorServicio(tabOpciones);
        }

        if (libroRepositorio.buscarTitulosLike(titulo.toLowerCase()).size() >= 11) {
            throw new ErrorServicio("Muchos libroes tienen" + titulo + "como parte de su titulo. Por favor ingrese un titulo más específico.");
        }

        return libroRepositorio.buscarTituloEqual(titulo.toLowerCase());
    }

    public Libro buscarTituloEqual(String titulo) {
        return libroRepositorio.buscarTituloEqual(titulo);
    }

    public List<Libro> buscarTitulosLike(String titulo) {
        return libroRepositorio.buscarTitulosLike(titulo);
    }

    public List<Libro> buscarTitulosInicial(String titulo) {
        return libroRepositorio.buscarTitulosInicial(titulo);
    }

}
