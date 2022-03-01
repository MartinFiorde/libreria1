package edu.libreria.libreria.servicios;

import edu.libreria.libreria.entidades.Autor;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    private AutorRepositorio autorRepositorio;

    @Autowired
    public AutorServicio(AutorRepositorio autorRepositorio) {
        this.autorRepositorio = autorRepositorio;
    }

    //
    public void registrar(String nombre, String apellido) throws ErrorServicio {
        Autor autor = new Autor();
        String concat = nombre.concat(" " + apellido);
        autor.setNombre(validarNombre(concat));
        autor.setAlta(Boolean.TRUE);
        autorRepositorio.save(autor);
    }

    public void modificar(String id, String nombre, String apellido) throws ErrorServicio {
        Autor autor = validarId(id);
        String concat = nombre.concat(" " + apellido);
        autor.setNombre(validarNombre(concat));
        autorRepositorio.save(autor);
    }

    public void eliminar(String id) throws ErrorServicio {
        Autor autor = validarId(id);
        autor.setAlta(Boolean.FALSE);
        autorRepositorio.save(autor);
    }

    public void reactivar(String id) throws ErrorServicio {
        Autor autor = validarId(id);
        autor.setAlta(Boolean.TRUE);
        autorRepositorio.save(autor);
    }

    public Autor validarId(String id) throws ErrorServicio {
        Optional<Autor> res = autorRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro el autor solicitado.");
        }
        return res.get();
    }

    public String validarNombre(String nombre) throws ErrorServicio {
        if (nombre.toLowerCase() == null || nombre.isEmpty() || nombre.trim() == "") {
            throw new ErrorServicio("Debe ingresar un nombre.");
        }
        if (autorRepositorio.buscarNombreEqual(nombre.toLowerCase()) != null) {
            throw new ErrorServicio("Ya existe un autor con el nombre seleccionado.");
        }
        return nombre.toLowerCase();
    }

    public Autor validarExistenciaAutor(String nombre) throws ErrorServicio {

        if (autorRepositorio.buscarNombreEqual(nombre.toLowerCase()) == null) {
            throw new ErrorServicio("No existe un autor con el nombre seleccionado. Por favor ingrese el autor e intente nuevamente");
        }

        if (autorRepositorio.buscarNombresLike(nombre.toLowerCase()).size() > 1 && autorRepositorio.buscarNombresLike(nombre.toLowerCase()).size() < 11) {
            List<Autor> autores = autorRepositorio.buscarNombresLike(nombre.toLowerCase());
            String tabOpciones = "Por favor ingrese un autor de entre las siguientes opciones:";
            for (Autor aux : autores) {
                tabOpciones.concat("\n" + aux.getNombre());
            }
            throw new ErrorServicio(tabOpciones);
        }

        if (autorRepositorio.buscarNombresLike(nombre.toLowerCase()).size() >= 11) {
            throw new ErrorServicio("Muchos autores tienen" + nombre + "como parte de su nombre. Por favor ingrese un nombre más específico.");
        }

        return autorRepositorio.buscarNombreEqual(nombre.toLowerCase());
    }

    public Autor buscarNombreEqual(String nombre) {
        return autorRepositorio.buscarNombreEqual(nombre);
    }

    public List<Autor> buscarNombresLike(String nombre) {
        return autorRepositorio.buscarNombresLike(nombre);
    }

    public List<Autor> buscarNombresInicial(String nombre) {
        return autorRepositorio.buscarNombresInicial(nombre);
    }

}
