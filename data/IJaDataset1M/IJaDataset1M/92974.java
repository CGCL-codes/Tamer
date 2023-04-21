package edu.cibertec.alquiler.dao.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import sun.misc.Perf;
import edu.cibertec.alquiler.dao.CombosDAO;
import edu.cibertec.alquiler.entity.Categoria;
import edu.cibertec.alquiler.entity.Estado;
import edu.cibertec.alquiler.entity.Genero;
import edu.cibertec.alquiler.entity.Perfil;

public class CombosJPADAO implements CombosDAO {

    private EntityManagerFactory emf;

    private EntityManager em;

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @SuppressWarnings("unchecked")
    public List<Genero> listarGeneroCombo() throws Exception {
        em = emf.createEntityManager();
        List<Genero> listaGenero = new ArrayList<Genero>();
        try {
            Query q = em.createQuery("Select g from Genero g ");
            listaGenero = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            em.clear();
            em.close();
        }
        return listaGenero;
    }

    @SuppressWarnings("unchecked")
    public List<Estado> listarEstadoCombo() throws Exception {
        em = emf.createEntityManager();
        List<Estado> listaEstado = new ArrayList<Estado>();
        try {
            Query q = em.createQuery("Select e from Estado e");
            listaEstado = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            em.clear();
            em.close();
        }
        return listaEstado;
    }

    @SuppressWarnings("unchecked")
    public List<Categoria> listarCategoriaCombo() throws Exception {
        em = emf.createEntityManager();
        List<Categoria> listaCategoria = new ArrayList<Categoria>();
        try {
            Query q = em.createQuery("Select c from Categoria c");
            listaCategoria = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            em.clear();
            em.close();
        }
        return listaCategoria;
    }

    @SuppressWarnings("unchecked")
    public List<Perfil> listarPerfilCombo() throws Exception {
        em = emf.createEntityManager();
        List<Perfil> listarPerfil = new ArrayList<Perfil>();
        try {
            Query q = em.createQuery("Select c from Perfil c");
            listarPerfil = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            em.clear();
            em.close();
        }
        return listarPerfil;
    }
}
