package com.rps.finwallet.repository;

import com.rps.finwallet.model.CompanyTreasury;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyTreasuryRepository extends JpaRepository<CompanyTreasury, Long> {

    Optional<CompanyTreasury> findByAccountNumber(String accountNumber);

    /**
     * Pessimistic Write Lock: Ensures no two payroll processes or treasury deposits
     * modify the balance concurrently (Database Row Lock: SELECT ... FOR UPDATE).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM CompanyTreasury t WHERE t.accountNumber = :accountNumber")
    Optional<CompanyTreasury> findAndLockByAccountNumber(@Param("accountNumber") String accountNumber);

    /**
     * Get the primary company treasury account with a pessimistic lock.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM CompanyTreasury t WHERE t.id = (SELECT MIN(ct.id) FROM CompanyTreasury ct)")
    Optional<CompanyTreasury> findPrimaryTreasuryForUpdate();

    Optional<CompanyTreasury> findFirstByOrderByIdAsc();
}
