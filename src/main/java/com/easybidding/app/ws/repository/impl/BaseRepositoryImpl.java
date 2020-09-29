package com.easybidding.app.ws.repository.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.easybidding.app.ws.EasyBiddingApplicationContext;
import com.easybidding.app.ws.repository.BaseRepository;

@Transactional(propagation = Propagation.NEVER)
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements BaseRepository<T, ID> {

	private final EntityManager entityManager;

	public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public <S extends T> void saveInBatch(Iterable<S> entities) {

		if (entities == null) {
			throw new IllegalArgumentException("The given Iterable of entities cannot be null!");
		}

		BatchExecutor batchExecutor = EasyBiddingApplicationContext.getBean(BatchExecutor.class);
		batchExecutor.saveInBatch(entities);
	}

	@Transactional
	public List<T> findByAttributeContainsText(String attributeName, String text) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
		Root<T> root = cQuery.from(getDomainClass());
		cQuery.select(root).where(builder.like(root.<String>get(attributeName), "%" + text + "%"));
		TypedQuery<T> query = entityManager.createQuery(cQuery);
		return query.getResultList();
	}

}