package com.easybidding.app.ws.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	<S extends T> void saveInBatch(Iterable<S> entites);

	public List<T> findByAttributeContainsText(String attributeName, String text);
}
