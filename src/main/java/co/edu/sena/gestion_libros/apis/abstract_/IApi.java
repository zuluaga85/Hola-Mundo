package co.edu.sena.gestion_libros.apis.abstract_;

import spark.Request;
import spark.Response;

public interface IApi {
    //Ruta base
    public String getPath();
    //Metodo para crear
    public Object create(Request rq, Response rs);
    //Metodo para editar 
    public Object update(Request rq, Response rs);
    //Metodo para eliminar
    public Object delete(Request rq, Response rs);
    //Metodo para listar
    public Object getAll(Request rq, Response rs);
    //Metodo para obtener detalle
    public Object getById(Request rq, Response rs);
}
