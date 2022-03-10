package edu.libreria.libreria.controladores;

import edu.libreria.libreria.entidades.Editorial;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.repositorios.EditorialRepositorio;
import edu.libreria.libreria.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/e")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class EditorialControlador {

    private EditorialRepositorio editorialRepositorio;
    private EditorialServicio editorialServicio;

    @Autowired
    public EditorialControlador(EditorialRepositorio editorialRepositorio, EditorialServicio editorialServicio) {
        this.editorialRepositorio = editorialRepositorio;
        this.editorialServicio = editorialServicio;
    }

    //
    @GetMapping
    public String listarEditoriales(ModelMap model, @RequestParam(required = false) List<Editorial> editoriales, @RequestParam(required = false) String id) {
//        List<Editorial> editoriales = null;
        if (editoriales == null || editoriales.size() == 0) {
            editoriales = editorialRepositorio.findAll();
        }
        model.addAttribute("editoriales", editoriales);
        return "editorial-t/lista.html";
    }

    @PostMapping
    public String listarBusqueda(ModelMap model, @RequestParam(required = false) String nombre) {
        List<Editorial> editoriales = null;
        if (nombre != null) {
            editoriales = editorialRepositorio.buscarNombresLike(nombre);
        } else {
            editoriales = editorialRepositorio.findAll();
        }
        model.addAttribute("editoriales", editoriales);
        return "editorial-t/lista.html";
    }

    @GetMapping("/form")
    public String mostrarFormEditorial(ModelMap model, @RequestParam(required = false) String id, @RequestParam(required = false) String nombre) {
        model.put("id", id);
        model.put("nombre", nombre);
        return "editorial-t/formulario.html";
    }

    @PostMapping("/form1")
    public String precargarFormEditorial(ModelMap model, @RequestParam(required = false) String id, @RequestParam(required = false) String nombre) {
        model.put("id", id);
        model.put("nombre", nombre);
        return "editorial-t/formulario.html";
    }

    @PostMapping("/form2")
    public String guardarFormEditorial(ModelMap model, @RequestParam String nombre, @RequestParam(required = false) String id) throws ErrorServicio {
        try {
            System.out.println(id);
            if (id == null || id.isEmpty() || id == "") {
                System.out.println("regis");
                editorialServicio.registrar(nombre);
            } else {
                System.out.println("mod");
                editorialServicio.modificar(id, nombre);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("nombre", nombre);
            return "editorial-t/formulario.html";
//            return "redirect:/a/form";
        }
        return "redirect:/e";
    }

    @PostMapping("/activar")
    public String activar(@RequestParam(required = false) String id) throws ErrorServicio {
        editorialServicio.reactivar(id);
        return "redirect:/e";
    }

    @PostMapping("/desactivar")
    public String desactivar(@RequestParam(required = false) String id) throws ErrorServicio {
        editorialServicio.eliminar(id);
        return "redirect:/e";
    }

    
    @GetMapping("/search")
    public String mostrarBuscadorEditoriales(ModelMap model) {
        return "editorial-t/buscador.html";
    }

    @PostMapping("/search")
    public String buscarEditorialesguardarFormEditorial(ModelMap model, @RequestParam String nombre) throws ErrorServicio {
        try {
            editorialServicio.registrar(nombre);
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("nombre", nombre);
            return "redirect:/e/search";
        }

        return "redirect:/e";
    }
}
