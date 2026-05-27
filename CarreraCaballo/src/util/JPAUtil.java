package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static JPAUtil instancia;
    private EntityManagerFactory emf;

    private JPAUtil() {
        emf = Persistence.createEntityManagerFactory("CarrerasPU");
    }

    public static JPAUtil getInstancia() {
        if (instancia == null) {
            instancia = new JPAUtil();
        }
        return instancia;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
