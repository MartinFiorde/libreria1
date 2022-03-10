package edu.libreria.libreria.controladores;

import edu.libreria.libreria.entidades.Cliente;
import edu.libreria.libreria.entidades.Libro;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/c")
public class ClienteControlador {

    ClienteServicio clienteServicio;

    @Autowired
    public ClienteControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    @GetMapping
    public String listarLibros() {
        return "index.html";
    }

    @GetMapping("/register")
    public String formularioUsuario(ModelMap model, @RequestParam(required = false) @PathVariable(required = false) String id) throws ErrorServicio {
        if (id != null) {
            Cliente cliente = clienteServicio.validarId(id);
            model.put("id",id);
            model.put("mail", cliente.getMail());
            model.put("clave", cliente.getClave());
        }
        return "cliente-t/registro.html";
    }

    @PostMapping("/register2")
    public String cargarUsuario(ModelMap model, @RequestParam(required = false) @PathVariable(required = false) String id, @RequestParam(required = false) String mail, @RequestParam(required = false) String clave, @RequestParam(required = false) String clave2) {
        try {
            if (id == null || id.trim().isEmpty()) {
                System.out.println("registrar");
                clienteServicio.registrar(mail, clave, clave2);
            } else {
                System.out.println("modificar");
                clienteServicio.modificar(id, mail, clave, clave2);
            }
            model.put("error","Se ha registrado correctamente!");
            return "index.html";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("id",id);
            model.put("mail", mail);
            model.put("clave", clave);
            model.put("clave2", clave2);
            return "cliente-t/registro.html";
        }

    }

    @GetMapping("/login")
    public String registroUsuario(ModelMap model, @RequestParam(required = false) List<Libro> libros, @RequestParam(required = false) String id) {
        model.addAttribute("libros", libros);
        return "index.html";
    }

    @PostMapping("/login2")
    public String ingresoUsuario(ModelMap model, @RequestParam(required = false) @PathVariable(required = false) String id, @RequestParam(required = false) String mail, @RequestParam(required = false) String clave, @RequestParam(required = false) String clave2) {
       try {
            if (id == null || id.trim().isEmpty()) {
                System.out.println("registrar");
                clienteServicio.registrar(mail, clave, clave2);
            } else {
                System.out.println("modificar");
                clienteServicio.modificar(id, mail, clave, clave2);
            }
            model.put("error","Bienvenido "+mail+"! Ya estas logueado al sistema.");
            return "index.html";
        } catch (Exception ex) {
            System.out.println(ex);
            model.put("error", ex.getMessage());
            model.put("id",id);
            model.put("mail", mail);
            model.put("clave", clave);
            model.put("clave2", clave2);
            return "cliente-t/registro.html";
        }
    }
}
