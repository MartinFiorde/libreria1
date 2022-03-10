package edu.libreria.libreria.controladores;

import edu.libreria.libreria.entidades.Libro;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.repositorios.LibroRepositorio;
import edu.libreria.libreria.servicios.AutorServicio;
import edu.libreria.libreria.servicios.EditorialServicio;
import edu.libreria.libreria.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/l")
@PreAuthorize("isAuthenticated()")
public class LibroControlador {

    private LibroRepositorio libroRepositorio;
    private LibroServicio libroServicio;
    private AutorServicio autorServicio;
    private EditorialServicio editorialServicio;

    @Autowired
    public LibroControlador(LibroRepositorio libroRepositorio, LibroServicio libroServicio, AutorServicio autorServicio, EditorialServicio editorialServicio) {
        this.libroRepositorio = libroRepositorio;
        this.libroServicio = libroServicio;
        this.autorServicio = autorServicio;
        this.editorialServicio = editorialServicio;
    }

    //
    @GetMapping
    public String listarLibros(ModelMap model, @RequestParam(required = false) List<Libro> libros, @RequestParam(required = false) String id) {
//        List<Libro> libros = null;
        if (libros == null || libros.isEmpty()) {
            libros = libroRepositorio.findAll();
        }
        model.addAttribute("libros", libros);
        return "libro-t/lista.html";
    }

    @PostMapping
    public String listarBusqueda(ModelMap model, @RequestParam(required = false) String titulo) {
        List<Libro> libros = null;
        if (titulo != null) {
            libros = libroServicio.buscarTitulosLike(titulo);
        } else {
            libros = libroRepositorio.findAll();
        }
        model.addAttribute("libros", libros);
        return "libro-t/lista.html";
    }

    @GetMapping("/form")
    public String mostrarFormLibro(ModelMap model, @RequestParam(required = false) @PathVariable(required = false) String id, @ModelAttribute() Libro libro) throws ErrorServicio {
        if (id != null) {
            model.put("id", id);
            model.put("libro", libroServicio.validarId(id));
        }
        return "libro-t/formulario.html";
    }

    @PostMapping("/form1")
    public String precargarFormLibro(ModelMap model, @RequestParam(required = false) String id, @RequestParam(required = false) String nombreEditorial, @RequestParam(required = false) String nombreAutor) throws ErrorServicio {
        if (id != null) {
            Libro libro = libroServicio.validarId(id);
            model.put("libro", libro);
        }
        if (nombreEditorial != null) {
            model.put("nombreEditorial", nombreEditorial);
        }
        if (nombreAutor != null) {
            model.put("nombreAutor", nombreAutor);
        }
        return "libro-t/formulario.html";
    }

    @PostMapping("/form2")
    public String guardarFormLibro(ModelMap model, @ModelAttribute Libro libro, @RequestParam(required = false) @PathVariable(required = false) String id, @RequestParam String nombreEditorial, @RequestParam String nombreAutor) throws ErrorServicio {
        try {
//            System.out.println("ISBN class:" + libro.getIsbn().getClass());
            System.out.println(id);
            if (id == null || id.isEmpty() || id == "") {
                System.out.println("regis");
                libroServicio.registrar(libro.getTitulo(), libro.getIsbn(), libro.getAnio(), libro.getEjemplares(), libro.getEjemplaresPrestados(), nombreAutor, nombreEditorial);
            } else {
                System.out.println("mod");
                libroServicio.modificar(id, libro.getTitulo(), libro.getIsbn(), libro.getAnio(), libro.getEjemplares(), libro.getEjemplaresPrestados(), nombreAutor, nombreEditorial);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println(ex.getMessage().equals("Por favor ingrese un editorial de entre las siguientes opciones:"));
            if (ex.getMessage().equals("Por favor ingrese un autor de entre las siguientes opciones:")) {
                model.put("opcNombres", autorServicio.enviarListaNombres(nombreAutor));
            }
            if (ex.getMessage().equals("Por favor ingrese un editorial de entre las siguientes opciones:")) {
                model.put("opcNombres", editorialServicio.enviarListaNombres(nombreEditorial));
                List<String> names = editorialServicio.enviarListaNombres(nombreEditorial);
                for (String aux : names) {
                    System.out.println(aux);
                }
            }
            
            model.put("error", ex.getMessage());
            model.put("libro", libro);
            model.put("nombreEditorial", nombreEditorial);
            model.put("nombreAutor", nombreAutor);
            return "libro-t/formulario.html";
//            return "redirect:/a/form";
        }
        return "redirect:/l";
    }

    @PostMapping("/activar")
    public String activar(@PathVariable(required = false) @RequestParam(required = false) String id) throws ErrorServicio {
        libroServicio.reactivar(id);
        return "redirect:/l";
    }

    @PostMapping("/desactivar")
    public String desactivar(@PathVariable(required = false) @RequestParam(required = false) String id) throws ErrorServicio {
        libroServicio.eliminar(id);
        return "redirect:/l";
    }

    @GetMapping("/search")
    public String mostrarBuscadorLibros(ModelMap model) {
        return "libro-t/buscador.html";
    }

    @PostMapping("/search")
    public String buscarLibrosguardarFormLibro(ModelMap model, @RequestParam(required = false) String id, @RequestParam String titulo, @RequestParam Long isbn, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam String tituloAutor, @RequestParam String tituloEditorial) throws ErrorServicio {
        try {
            libroServicio.registrar(titulo, isbn, anio, ejemplares, ejemplaresPrestados, tituloAutor, tituloEditorial);
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("titulo", titulo);
            return "redirect:/l/search";
        }
        return "redirect:/l";
    }
}
