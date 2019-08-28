/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.sena.gestion_libros.controllers;

import co.edu.sena.gestion_libros.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.sena.gestion_libros.entities.Autor;
import co.edu.sena.gestion_libros.entities.AutorLibro;
import co.edu.sena.gestion_libros.entities.Libro;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USER
 */
public class AutorLibroJpaController implements Serializable {

    public AutorLibroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AutorLibro autorLibro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autor autId = autorLibro.getAutId();
            if (autId != null) {
                autId = em.getReference(autId.getClass(), autId.getAutId());
                autorLibro.setAutId(autId);
            }
            Libro libId = autorLibro.getLibId();
            if (libId != null) {
                libId = em.getReference(libId.getClass(), libId.getLibId());
                autorLibro.setLibId(libId);
            }
            em.persist(autorLibro);
            if (autId != null) {
                autId.getAutorLibroList().add(autorLibro);
                autId = em.merge(autId);
            }
            if (libId != null) {
                libId.getAutorLibroList().add(autorLibro);
                libId = em.merge(libId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AutorLibro autorLibro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AutorLibro persistentAutorLibro = em.find(AutorLibro.class, autorLibro.getAlId());
            Autor autIdOld = persistentAutorLibro.getAutId();
            Autor autIdNew = autorLibro.getAutId();
            Libro libIdOld = persistentAutorLibro.getLibId();
            Libro libIdNew = autorLibro.getLibId();
            if (autIdNew != null) {
                autIdNew = em.getReference(autIdNew.getClass(), autIdNew.getAutId());
                autorLibro.setAutId(autIdNew);
            }
            if (libIdNew != null) {
                libIdNew = em.getReference(libIdNew.getClass(), libIdNew.getLibId());
                autorLibro.setLibId(libIdNew);
            }
            autorLibro = em.merge(autorLibro);
            if (autIdOld != null && !autIdOld.equals(autIdNew)) {
                autIdOld.getAutorLibroList().remove(autorLibro);
                autIdOld = em.merge(autIdOld);
            }
            if (autIdNew != null && !autIdNew.equals(autIdOld)) {
                autIdNew.getAutorLibroList().add(autorLibro);
                autIdNew = em.merge(autIdNew);
            }
            if (libIdOld != null && !libIdOld.equals(libIdNew)) {
                libIdOld.getAutorLibroList().remove(autorLibro);
                libIdOld = em.merge(libIdOld);
            }
            if (libIdNew != null && !libIdNew.equals(libIdOld)) {
                libIdNew.getAutorLibroList().add(autorLibro);
                libIdNew = em.merge(libIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = autorLibro.getAlId();
                if (findAutorLibro(id) == null) {
                    throw new NonexistentEntityException("The autorLibro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AutorLibro autorLibro;
            try {
                autorLibro = em.getReference(AutorLibro.class, id);
                autorLibro.getAlId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autorLibro with id " + id + " no longer exists.", enfe);
            }
            Autor autId = autorLibro.getAutId();
            if (autId != null) {
                autId.getAutorLibroList().remove(autorLibro);
                autId = em.merge(autId);
            }
            Libro libId = autorLibro.getLibId();
            if (libId != null) {
                libId.getAutorLibroList().remove(autorLibro);
                libId = em.merge(libId);
            }
            em.remove(autorLibro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AutorLibro> findAutorLibroEntities() {
        return findAutorLibroEntities(true, -1, -1);
    }

    public List<AutorLibro> findAutorLibroEntities(int maxResults, int firstResult) {
        return findAutorLibroEntities(false, maxResults, firstResult);
    }

    private List<AutorLibro> findAutorLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AutorLibro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public AutorLibro findAutorLibro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AutorLibro.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutorLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AutorLibro> rt = cq.from(AutorLibro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
