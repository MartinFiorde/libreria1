package edu.libreria.libreria.repositorios;

import edu.libreria.libreria.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {

     @Query("SELECT a FROM Editorial a WHERE a.nombre = :nombreq")
    public Editorial buscarNombreEqual(@Param("nombreq") String nombre);

    @Query("SELECT a FROM Editorial a WHERE a.nombre LIKE %:nombreq%")
    public List<Editorial> buscarNombresLike(@Param("nombreq") String nombre);

    @Query("SELECT a FROM Editorial a WHERE a.nombre LIKE :inicialq%")
    public List<Editorial> buscarNombresInicial(@Param("inicialq") String inicial);

}
