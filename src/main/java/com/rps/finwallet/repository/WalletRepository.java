package com.rps.finwallet.repository;

import com.rps.finwallet.model.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByWalletNumber(String walletNumber);

    Optional<Wallet> findByEmployeeId(Long employeeId);

    /**
     * Pessimistic Lock on Employee Wallet:
     * Prevents race conditions such as double withdrawals, concurrent peer transfers,
     * or concurrent salary credits.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.walletNumber = :walletNumber")
    Optional<Wallet> findAndLockByWalletNumber(@Param("walletNumber") String walletNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.employee.id = :employeeId")
    Optional<Wallet> findAndLockByEmployeeId(@Param("employeeId") Long employeeId);
}
