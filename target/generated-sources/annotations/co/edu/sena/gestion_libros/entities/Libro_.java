package co.edu.sena.gestion_libros.entities;

import co.edu.sena.gestion_libros.entities.AutorLibro;
import co.edu.sena.gestion_libros.entities.Categoria;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-08-27T21:17:12")
@StaticMetamodel(Libro.class)
public class Libro_ { 

    public static volatile SingularAttribute<Libro, Categoria> catId;
    public static volatile SingularAttribute<Libro, Integer> libId;
    public static volatile SingularAttribute<Libro, String> libNombre;
    public static volatile SingularAttribute<Libro, Short> libNumeroPaginas;
    public static volatile SingularAttribute<Libro, Date> libFechaPublicacion;
    public static volatile ListAttribute<Libro, AutorLibro> autorLibroList;

}