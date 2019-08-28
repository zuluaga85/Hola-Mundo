package co.edu.sena.gestion_libros.apis.abstract_;

import co.edu.sena.gestion_libros.utils.JsonTransformer;
import spark.Spark;


public abstract class BasicApi {

    protected void init(){
        IApi iapi = (IApi) this;
        JsonTransformer jt = JsonTransformer.singleton();
        //Traer todos los registros
        Spark.get(iapi.getPath(),(rq,rs)->iapi.getAll(rq, rs),jt);
        //Traer por id
        Spark.get(iapi.getPath()+"/:id",(rq,rs)->iapi.getById(rq, rs),jt);
        //Crear
        Spark.post(iapi.getPath(),(rq,rs)->iapi.create(rq, rs),jt);
        //Actualizar
        Spark.put(iapi.getPath()+"/:id",(rq,rs)->iapi.update(rq, rs),jt);
        //Eliminar
        Spark.delete(iapi.getPath()+"/:id",(rq,rs)->iapi.delete(rq, rs),jt);
    }
    
}
