/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.sena.gestion_libros.controllers;

import co.edu.sena.gestion_libros.controllers.exceptions.IllegalOrphanException;
import co.edu.sena.gestion_libros.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.sena.gestion_libros.entities.Categoria;
import co.edu.sena.gestion_libros.entities.AutorLibro;
import co.edu.sena.gestion_libros.entities.Libro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USER
 */
public class LibroJpaController implements Serializable {

    public LibroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libro libro) {
        if (libro.getAutorLibroList() == null) {
            libro.setAutorLibroList(new ArrayList<AutorLibro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria catId = libro.getCatId();
            if (catId != null) {
                catId = em.getReference(catId.getClass(), catId.getCatId());
                libro.setCatId(catId);
            }
            List<AutorLibro> attachedAutorLibroList = new ArrayList<AutorLibro>();
            for (AutorLibro autorLibroListAutorLibroToAttach : libro.getAutorLibroList()) {
                autorLibroListAutorLibroToAttach = em.getReference(autorLibroListAutorLibroToAttach.getClass(), autorLibroListAutorLibroToAttach.getAlId());
                attachedAutorLibroList.add(autorLibroListAutorLibroToAttach);
            }
            libro.setAutorLibroList(attachedAutorLibroList);
            em.persist(libro);
            if (catId != null) {
                catId.getLibroList().add(libro);
                catId = em.merge(catId);
            }
            for (AutorLibro autorLibroListAutorLibro : libro.getAutorLibroList()) {
                Libro oldLibIdOfAutorLibroListAutorLibro = autorLibroListAutorLibro.getLibId();
                autorLibroListAutorLibro.setLibId(libro);
                autorLibroListAutorLibro = em.merge(autorLibroListAutorLibro);
                if (oldLibIdOfAutorLibroListAutorLibro != null) {
                    oldLibIdOfAutorLibroListAutorLibro.getAutorLibroList().remove(autorLibroListAutorLibro);
                    oldLibIdOfAutorLibroListAutorLibro = em.merge(oldLibIdOfAutorLibroListAutorLibro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libro libro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro persistentLibro = em.find(Libro.class, libro.getLibId());
            Categoria catIdOld = persistentLibro.getCatId();
            Categoria catIdNew = libro.getCatId();
            List<AutorLibro> autorLibroListOld = persistentLibro.getAutorLibroList();
            List<AutorLibro> autorLibroListNew = libro.getAutorLibroList();
            List<String> illegalOrphanMessages = null;
            for (AutorLibro autorLibroListOldAutorLibro : autorLibroListOld) {
                if (!autorLibroListNew.contains(autorLibroListOldAutorLibro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AutorLibro " + autorLibroListOldAutorLibro + " since its libId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (catIdNew != null) {
                catIdNew = em.getReference(catIdNew.getClass(), catIdNew.getCatId());
                libro.setCatId(catIdNew);
            }
            List<AutorLibro> attachedAutorLibroListNew = new ArrayList<AutorLibro>();
            for (AutorLibro autorLibroListNewAutorLibroToAttach : autorLibroListNew) {
                autorLibroListNewAutorLibroToAttach = em.getReference(autorLibroListNewAutorLibroToAttach.getClass(), autorLibroListNewAutorLibroToAttach.getAlId());
                attachedAutorLibroListNew.add(autorLibroListNewAutorLibroToAttach);
            }
            autorLibroListNew = attachedAutorLibroListNew;
            libro.setAutorLibroList(autorLibroListNew);
            libro = em.merge(libro);
            if (catIdOld != null && !catIdOld.equals(catIdNew)) {
                catIdOld.getLibroList().remove(libro);
                catIdOld = em.merge(catIdOld);
            }
            if (catIdNew != null && !catIdNew.equals(catIdOld)) {
                catIdNew.getLibroList().add(libro);
                catIdNew = em.merge(catIdNew);
            }
            for (AutorLibro autorLibroListNewAutorLibro : autorLibroListNew) {
                if (!autorLibroListOld.contains(autorLibroListNewAutorLibro)) {
                    Libro oldLibIdOfAutorLibroListNewAutorLibro = autorLibroListNewAutorLibro.getLibId();
                    autorLibroListNewAutorLibro.setLibId(libro);
                    autorLibroListNewAutorLibro = em.merge(autorLibroListNewAutorLibro);
                    if (oldLibIdOfAutorLibroListNewAutorLibro != null && !oldLibIdOfAutorLibroListNewAutorLibro.equals(libro)) {
                        oldLibIdOfAutorLibroListNewAutorLibro.getAutorLibroList().remove(autorLibroListNewAutorLibro);
                        oldLibIdOfAutorLibroListNewAutorLibro = em.merge(oldLibIdOfAutorLibroListNewAutorLibro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = libro.getLibId();
                if (findLibro(id) == null) {
                    throw new NonexistentEntityException("The libro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro libro;
            try {
                libro = em.getReference(Libro.class, id);
                libro.getLibId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AutorLibro> autorLibroListOrphanCheck = libro.getAutorLibroList();
            for (AutorLibro autorLibroListOrphanCheckAutorLibro : autorLibroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Libro (" + libro + ") cannot be destroyed since the AutorLibro " + autorLibroListOrphanCheckAutorLibro + " in its autorLibroList field has a non-nullable libId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria catId = libro.getCatId();
            if (catId != null) {
                catId.getLibroList().remove(libro);
                catId = em.merge(catId);
            }
            em.remove(libro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libro> findLibroEntities() {
        return findLibroEntities(true, -1, -1);
    }

    public List<Libro> findLibroEntities(int maxResults, int firstResult) {
        return findLibroEntities(false, maxResults, firstResult);
    }

    private List<Libro> findLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libro.class));
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

    public Libro findLibro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libro> rt = cq.from(Libro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
