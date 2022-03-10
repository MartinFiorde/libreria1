package edu.libreria.libreria.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainControlador {
    
    @GetMapping
    public String index(){
        return "index.html";
    }
    
    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, ModelMap model){
        if (error != null) {
            model.put("error","Mail o contraseña invalidos.");
        }
        return "login.html";
    }
    
    
//    @GetMapping("/exito")
//    public String exito(ModelMap model){
//        return "exito-t.html";
//    }
}