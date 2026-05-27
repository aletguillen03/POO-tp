package dao.impl;

import dao.IJugadorDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import modelo.Jugador;
import util.JPAUtil;

import java.util.List;

public class JugadorDAOImpl implements IJugadorDAO {

    private EntityManager em;

    public JugadorDAOImpl() {
        this.em = JPAUtil.getInstancia().getEntityManager();
    }

    @Override
    public void guardar(Jugador jugador) {
        em = JPAUtil.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(jugador);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Jugador buscarPorMail(String mail) {
        em = JPAUtil.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT j FROM Jugador j WHERE j.mail = :mail", Jugador.class)
                     .setParameter("mail", mail)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(Jugador jugador) {
        em = JPAUtil.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(jugador);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Jugador> listarTodos() {
        em = JPAUtil.getInstancia().getEntityManager();
        try {
            return em.createQuery("SELECT j FROM Jugador j", Jugador.class).getResultList();
        } finally {
            em.close();
        }
    }
}
