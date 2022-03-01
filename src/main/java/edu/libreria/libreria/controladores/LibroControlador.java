package edu.libreria.libreria.controladores;

import edu.libreria.libreria.entidades.Libro;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.repositorios.LibroRepositorio;
import edu.libreria.libreria.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/l")
public class LibroControlador {

    private LibroRepositorio libroRepositorio;
    private LibroServicio libroServicio;

    @Autowired
    public LibroControlador(LibroRepositorio libroRepositorio, LibroServicio libroServicio) {
        this.libroRepositorio = libroRepositorio;
        this.libroServicio = libroServicio;
    }

    //
    @GetMapping
    public String listarLibros(ModelMap model, @RequestParam(required = false) List<Libro> libros, @RequestParam(required = false) String id) {
//        List<Libro> libros = null;
        if (libros == null || libros.size() == 0) {
            libros = libroRepositorio.findAll();
        }
        model.addAttribute("libros", libros);
        return "libro-t/lista.html";
    }

    @PostMapping
    public String listarBusqueda(ModelMap model, @RequestParam(required = false) String titulo) {
        List<Libro> libros = null;
        if (titulo != null) {
            libros = libroRepositorio.buscarTitulosLike(titulo);
        } else {
            libros = libroRepositorio.findAll();
        }
        model.addAttribute("libros", libros);
        return "libro-t/lista.html";
    }

    @GetMapping("/form")
    public String mostrarFormLibro(ModelMap model, @RequestParam(required = false) String id, @RequestParam String titulo, @RequestParam Long isbn, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam String tituloAutor, @RequestParam String tituloEditorial) throws ErrorServicio {
        model.put("id", id);
        model.put("titulo", titulo);
        return "libro-t/formulario.html";
    }

    @PostMapping("/activar")
    public String activar(@RequestParam(required = false) String id) throws ErrorServicio {
        libroServicio.reactivar(id);
        return "redirect:/l";
    }

    @PostMapping("/desactivar")
    public String desactivar(@RequestParam(required = false) String id) throws ErrorServicio {
        libroServicio.eliminar(id);
        return "redirect:/l";
    }

    @PostMapping("/form1")
    public String precargarFormLibro(ModelMap model, @RequestParam(required = false) String id, @RequestParam String titulo, @RequestParam Long isbn, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam String tituloAutor, @RequestParam String tituloEditorial) throws ErrorServicio {
        model.put("id", id);
        model.put("titulo", titulo);
        return "libro-t/formulario.html";
    }

    @PostMapping("/form2")
    public String guardarFormLibro(ModelMap model, @RequestParam(required = false) String id, @RequestParam String titulo, @RequestParam Long isbn, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam String tituloAutor, @RequestParam String tituloEditorial) throws ErrorServicio {
        try {
            System.out.println(id);
            if (id == null || id.isEmpty() || id == "") {
                System.out.println("regis");
                libroServicio.registrar(titulo, isbn, anio, ejemplares, ejemplaresPrestados, tituloAutor, tituloEditorial);
            } else {
                System.out.println("mod");
                libroServicio.modificar(id, titulo, isbn, anio, ejemplares, ejemplaresPrestados, tituloAutor, tituloEditorial);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("titulo", titulo);
            return "libro-t/formulario.html";
//            return "redirect:/a/form";
        }
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
