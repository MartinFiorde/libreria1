package edu.libreria.libreria.repositorios;

import edu.libreria.libreria.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT a FROM Libro a WHERE a.titulo = :tituloq")
    public Libro buscarTituloEqual(@Param("tituloq") String titulo);

    @Query("SELECT a FROM Libro a WHERE a.isbn = :isbnq")
    public Libro buscarIsbnEqual(@Param("isbnq") String isbn);

    @Query("SELECT a FROM Libro a WHERE a.titulo LIKE %:tituloq%")
    public List<Libro> buscarTitulosLike(@Param("tituloq") String titulo);

    @Query("SELECT a FROM Libro a WHERE a.titulo LIKE :inicialq%")
    public List<Libro> buscarTitulosInicial(@Param("inicialq") String inicial);

    @Query("SELECT a FROM Libro a WHERE a.autor.nombre LIKE %:nombreq%")
    public List<Libro> buscarAutoresLike(@Param("nombreq") String nombre);

    @Query("SELECT a FROM Libro a WHERE a.isbn LIKE %:nombreq%")
    public List<Libro> buscarIsbnLike(@Param("nombreq") Long isbn);

    @Query("SELECT a FROM Libro a WHERE a.anio LIKE %:nombreq%")
    public List<Libro> buscarAnioLike(@Param("nombreq") Integer anio);

    @Query("SELECT a FROM Libro a WHERE a.editorial.nombre LIKE %:nombreq%")
    public List<Libro> buscarEditorialesLike(@Param("nombreq") String nombre);

}
