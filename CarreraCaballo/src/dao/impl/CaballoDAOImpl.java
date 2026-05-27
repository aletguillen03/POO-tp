package dao.impl;

import dao.ICaballoDAO;
import jakarta.persistence.EntityManager;
import modelo.caballos.Caballo;
import util.JPAUtil;

import java.util.List;

public class CaballoDAOImpl implements ICaballoDAO {

    private EntityManager em;

    public CaballoDAOImpl() {
        this.em = JPAUtil.getInstancia().getEntityManager();
    }

    @Override
    public void guardar(Caballo caballo) {
        em = JPAUtil.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(caballo);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Caballo buscarPorId(Long id) {
        em = JPAUtil.getInstancia().getEntityManager();
        try {
            return em.find(Caballo.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caballo> listarDisponibles() {
        em = JPAUtil.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Caballo c", Caballo.class).getResultList();
        } finally {
            em.close();
        }
    }
}
