package com.easybidding.app.ws.repository.impl;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.io.entity.VerificationTokenEntity;
import com.easybidding.app.ws.repository.BaseRepository;

public interface VerificationTokenRepository extends BaseRepository<VerificationTokenEntity, Long> {
	VerificationTokenEntity findByToken(String token);

	VerificationTokenEntity findByUser(UserEntity user);

	Stream<VerificationTokenEntity> findAllByExpiryDateLessThan(Date now);

	void deleteByExpiryDateLessThan(Date now);

	@Modifying
	@Query("DELETE FROM VerificationTokenEntity t WHERE t.expiryDate <= ?1")
	void deleteAllExpiredSince(Date now);
}
