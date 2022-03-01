package edu.libreria.libreria.servicios;

import edu.libreria.libreria.entidades.Autor;
import edu.libreria.libreria.entidades.Editorial;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {

    private EditorialRepositorio editorialRepositorio;

    @Autowired
    public EditorialServicio(EditorialRepositorio editorialRepositorio) {
        this.editorialRepositorio = editorialRepositorio;
    }

    //
    public void registrar(String nombre) throws ErrorServicio {
        Editorial editorial = new Editorial();
        editorial.setNombre(validarNombre(nombre));
        editorial.setAlta(Boolean.TRUE);
        editorialRepositorio.save(editorial);
    }

    public void modificar(String id, String nombre) throws ErrorServicio {
        Editorial editorial = validarId(id);
        editorial.setNombre(validarNombre(nombre));
        editorialRepositorio.save(editorial);
    }

    public void eliminar(String id) throws ErrorServicio {
        Editorial editorial = validarId(id);
        editorial.setAlta(Boolean.FALSE);
        editorialRepositorio.save(editorial);
    }

    public void reactivar(String id) throws ErrorServicio {
        Editorial editorial = validarId(id);
        editorial.setAlta(Boolean.TRUE);
        editorialRepositorio.save(editorial);
    }

    public Editorial validarId(String id) throws ErrorServicio {
        Optional<Editorial> res = editorialRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro el editorial solicitado.");
        }
        return res.get();
    }

    public String validarNombre(String nombre) throws ErrorServicio {
        if (nombre.toLowerCase() == null || nombre.isEmpty() || nombre.trim() == "") {
            throw new ErrorServicio("Debe ingresar un nombre.");
        }
        if (editorialRepositorio.buscarNombreEqual(nombre.toLowerCase()) != null) {
            throw new ErrorServicio("Ya existe un editorial con el nombre seleccionado.");
        }
        return nombre.toLowerCase();
    }

    public Editorial validarExistenciaEditorial(String nombre) throws ErrorServicio {

        if (editorialRepositorio.buscarNombreEqual(nombre.toLowerCase()) == null) {
            throw new ErrorServicio("No existe un editorial con el nombre seleccionado. Por favor ingrese el editorial e intente nuevamente");
        }

        if (editorialRepositorio.buscarNombresLike(nombre.toLowerCase()).size() > 1 && editorialRepositorio.buscarNombresLike(nombre.toLowerCase()).size() < 11) {
            List<Editorial> editoriales = editorialRepositorio.buscarNombresLike(nombre.toLowerCase());
            String tabOpciones = "Por favor ingrese un editorial de entre las siguientes opciones:";
            for (Editorial aux : editoriales) {
                tabOpciones.concat("\n" + aux.getNombre());
            }
            throw new ErrorServicio(tabOpciones);
        }

        if (editorialRepositorio.buscarNombresLike(nombre.toLowerCase()).size() >= 11) {
            throw new ErrorServicio("Muchos editoriales tienen" + nombre + "como parte de su nombre. Por favor ingrese un nombre más específico.");
        }

        return editorialRepositorio.buscarNombreEqual(nombre.toLowerCase());
    }

    
    public Editorial buscarNombreEqual(String nombre) {
        return editorialRepositorio.buscarNombreEqual(nombre);
    }

    public List<Editorial> buscarNombresLike(String nombre) {
        return editorialRepositorio.buscarNombresLike(nombre);
    }

    public List<Editorial> buscarNombresInicial(String nombre) {
        return editorialRepositorio.buscarNombresInicial(nombre);
    }

}
