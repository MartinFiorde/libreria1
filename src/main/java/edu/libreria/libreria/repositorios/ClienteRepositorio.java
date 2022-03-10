package edu.libreria.libreria.repositorios;

import edu.libreria.libreria.entidades.Autor;
import edu.libreria.libreria.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

    @Query("SELECT a FROM Cliente a WHERE a.mail = :mailq")
    public Cliente buscarMailEqual(@Param("mailq") String mail);

    @Query("SELECT a FROM Cliente a WHERE a.mail LIKE %:mailq%")
    public List<Cliente> buscarMailsLike(@Param("mailq") String mail);

}
