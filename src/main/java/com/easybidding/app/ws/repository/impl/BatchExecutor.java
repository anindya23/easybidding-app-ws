package com.easybidding.app.ws.repository.impl;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BatchExecutor<T> {

    private static final Logger logger = LoggerFactory.getLogger(BatchExecutor.class);

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final EntityManagerFactory entityManagerFactory;

    public BatchExecutor(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <S extends T> void saveInBatch(Iterable<S> entities) {

        if (entities == null) {
            throw new IllegalArgumentException("The given Iterable of entities not be null!");
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            int i = 0;
            for (S entity : entities) {
                if (i % batchSize == 0 && i > 0) {
                    logger.info("Flushing the EntityManager containing {0} entities ...", batchSize);

                    entityTransaction.commit();
                    entityTransaction.begin();

                    entityManager.clear();
                }

                entityManager.merge(entity);
                i++;
            }

            logger.info("Flushing the remaining entities ...");

            entityTransaction.commit();
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            logger.info(e.getMessage());
            throw e;
        } finally {
            entityManager.close();
        }
    }
}