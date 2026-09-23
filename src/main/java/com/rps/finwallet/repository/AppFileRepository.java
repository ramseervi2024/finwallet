package com.rps.finwallet.repository;

import com.rps.finwallet.model.AppFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppFileRepository extends JpaRepository<AppFile, Long> {
}
