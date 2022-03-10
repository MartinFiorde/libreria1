package edu.libreria.libreria.servicios;

import edu.libreria.libreria.entidades.Cliente;
import edu.libreria.libreria.enums.Rol;
import edu.libreria.libreria.errores.ErrorServicio;
import edu.libreria.libreria.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio implements UserDetailsService {

    private ClienteRepositorio clienteRepositorio;

    @Autowired
    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    //    
    @Transactional(rollbackOn = Exception.class)
    public void registrar(String mail, String clave, String clave2) throws ErrorServicio {
        Cliente cliente = new Cliente();
        cliente.setMail(validarMail(null, mail));
        String claveEncriptada = new BCryptPasswordEncoder().encode(validarClaves(clave, clave2));
        cliente.setClave(claveEncriptada);
        cliente.setAlta(Boolean.TRUE);
        cliente.setRol(Rol.USER);
        clienteRepositorio.save(cliente);
    }

    public void modificar(String id, String mail, String clave, String clave2) throws ErrorServicio {
        Cliente cliente = validarId(id);
        cliente.setMail(validarMail(null, mail));
        String claveEncriptada = new BCryptPasswordEncoder().encode(validarClaves(clave, clave2));
        cliente.setClave(claveEncriptada);
        clienteRepositorio.save(cliente);
    }

    public Cliente validarId(String id) throws ErrorServicio {
        Optional<Cliente> res = clienteRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
        return res.get();
    }

    public String validarMail(String id, String mail) throws ErrorServicio {
        // VALIDACIONES
        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("Debe ingresar un mail válido.");
        }
        if (clienteRepositorio.buscarMailEqual(mail.toLowerCase()) != null && clienteRepositorio.buscarMailEqual(mail.toLowerCase()).getId() != id) {
            throw new ErrorServicio("Ya existe un usuario con el mail seleccionado.");
        }
        return mail.toLowerCase();
    }

    public String validarClaves(String clave, String clave2) throws ErrorServicio {
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no coinciden.");
        }
        return clave;
    }

    public Cliente buscarMailEqual(String mail) {
        return clienteRepositorio.buscarMailEqual(mail);
    }

    public List<Cliente> buscarMailsLike(String mail) {
        return clienteRepositorio.buscarMailsLike(mail);
    }

    public List<Cliente> findAll() {
        return clienteRepositorio.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepositorio.buscarMailEqual(email);
        if (cliente == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        List<GrantedAuthority> permissions = new ArrayList();
        GrantedAuthority rolePermissions = new SimpleGrantedAuthority("ROLE_" + cliente.getRol().toString());
        System.out.println("------------ROL: "+rolePermissions.toString());
        permissions.add(rolePermissions);
        return new User(cliente.getMail(), cliente.getClave(), permissions);
    }
}
