package dao.impl;

import dao.IHistorialDAO;
import jakarta.persistence.EntityManager;
import modelo.HistorialCarrera;
import modelo.Jugador;
import util.JPAUtil;

import java.util.List;

public class HistorialDAOImpl implements IHistorialDAO {

    @Override
    public void guardar(HistorialCarrera historial) {
        EntityManager em = JPAUtil.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(historial);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HistorialCarrera> listarPorJugador(Jugador jugador) {
        EntityManager em = JPAUtil.getInstancia().getEntityManager();
        try {
            return em.createQuery(
                    "SELECT h FROM HistorialCarrera h WHERE h.jugador.id = :id ORDER BY h.fecha DESC",
                    HistorialCarrera.class)
                    .setParameter("id", jugador.getId())
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
