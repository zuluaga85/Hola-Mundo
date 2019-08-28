/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.sena.gestion_libros.controllers;

import co.edu.sena.gestion_libros.controllers.exceptions.IllegalOrphanException;
import co.edu.sena.gestion_libros.controllers.exceptions.NonexistentEntityException;
import co.edu.sena.gestion_libros.entities.Autor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.sena.gestion_libros.entities.AutorLibro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USER
 */
public class AutorJpaController implements Serializable {

    public AutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Autor autor) {
        if (autor.getAutorLibroList() == null) {
            autor.setAutorLibroList(new ArrayList<AutorLibro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<AutorLibro> attachedAutorLibroList = new ArrayList<AutorLibro>();
            for (AutorLibro autorLibroListAutorLibroToAttach : autor.getAutorLibroList()) {
                autorLibroListAutorLibroToAttach = em.getReference(autorLibroListAutorLibroToAttach.getClass(), autorLibroListAutorLibroToAttach.getAlId());
                attachedAutorLibroList.add(autorLibroListAutorLibroToAttach);
            }
            autor.setAutorLibroList(attachedAutorLibroList);
            em.persist(autor);
            for (AutorLibro autorLibroListAutorLibro : autor.getAutorLibroList()) {
                Autor oldAutIdOfAutorLibroListAutorLibro = autorLibroListAutorLibro.getAutId();
                autorLibroListAutorLibro.setAutId(autor);
                autorLibroListAutorLibro = em.merge(autorLibroListAutorLibro);
                if (oldAutIdOfAutorLibroListAutorLibro != null) {
                    oldAutIdOfAutorLibroListAutorLibro.getAutorLibroList().remove(autorLibroListAutorLibro);
                    oldAutIdOfAutorLibroListAutorLibro = em.merge(oldAutIdOfAutorLibroListAutorLibro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Autor autor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autor persistentAutor = em.find(Autor.class, autor.getAutId());
            List<AutorLibro> autorLibroListOld = persistentAutor.getAutorLibroList();
            List<AutorLibro> autorLibroListNew = autor.getAutorLibroList();
            List<String> illegalOrphanMessages = null;
            for (AutorLibro autorLibroListOldAutorLibro : autorLibroListOld) {
                if (!autorLibroListNew.contains(autorLibroListOldAutorLibro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AutorLibro " + autorLibroListOldAutorLibro + " since its autId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AutorLibro> attachedAutorLibroListNew = new ArrayList<AutorLibro>();
            for (AutorLibro autorLibroListNewAutorLibroToAttach : autorLibroListNew) {
                autorLibroListNewAutorLibroToAttach = em.getReference(autorLibroListNewAutorLibroToAttach.getClass(), autorLibroListNewAutorLibroToAttach.getAlId());
                attachedAutorLibroListNew.add(autorLibroListNewAutorLibroToAttach);
            }
            autorLibroListNew = attachedAutorLibroListNew;
            autor.setAutorLibroList(autorLibroListNew);
            autor = em.merge(autor);
            for (AutorLibro autorLibroListNewAutorLibro : autorLibroListNew) {
                if (!autorLibroListOld.contains(autorLibroListNewAutorLibro)) {
                    Autor oldAutIdOfAutorLibroListNewAutorLibro = autorLibroListNewAutorLibro.getAutId();
                    autorLibroListNewAutorLibro.setAutId(autor);
                    autorLibroListNewAutorLibro = em.merge(autorLibroListNewAutorLibro);
                    if (oldAutIdOfAutorLibroListNewAutorLibro != null && !oldAutIdOfAutorLibroListNewAutorLibro.equals(autor)) {
                        oldAutIdOfAutorLibroListNewAutorLibro.getAutorLibroList().remove(autorLibroListNewAutorLibro);
                        oldAutIdOfAutorLibroListNewAutorLibro = em.merge(oldAutIdOfAutorLibroListNewAutorLibro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = autor.getAutId();
                if (findAutor(id) == null) {
                    throw new NonexistentEntityException("The autor with id " + id + " no longer exists.");
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
            Autor autor;
            try {
                autor = em.getReference(Autor.class, id);
                autor.getAutId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AutorLibro> autorLibroListOrphanCheck = autor.getAutorLibroList();
            for (AutorLibro autorLibroListOrphanCheckAutorLibro : autorLibroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autor (" + autor + ") cannot be destroyed since the AutorLibro " + autorLibroListOrphanCheckAutorLibro + " in its autorLibroList field has a non-nullable autId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(autor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Autor> findAutorEntities() {
        return findAutorEntities(true, -1, -1);
    }

    public List<Autor> findAutorEntities(int maxResults, int firstResult) {
        return findAutorEntities(false, maxResults, firstResult);
    }

    private List<Autor> findAutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autor.class));
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

    public Autor findAutor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Autor.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autor> rt = cq.from(Autor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
