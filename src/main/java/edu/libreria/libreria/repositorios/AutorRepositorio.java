package edu.libreria.libreria.repositorios;

import edu.libreria.libreria.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombreq")
    public Autor buscarNombreEqual(@Param("nombreq") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.nombre LIKE %:nombreq%")
    public List<Autor> buscarNombresLike(@Param("nombreq") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.nombre LIKE :inicialq%")
    public List<Autor> buscarNombresInicial(@Param("inicialq") String inicial);

}
