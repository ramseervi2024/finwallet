package com.rps.finwallet.repository;

import com.rps.finwallet.model.TransactionLedger;
import com.rps.finwallet.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionLedgerRepository extends JpaRepository<TransactionLedger, Long> {

    Optional<TransactionLedger> findByTransactionRef(String transactionRef);

    Page<TransactionLedger> findByFromAccountOrToAccountOrderByCreatedAtDesc(String fromAccount, String toAccount, Pageable pageable);

    Page<TransactionLedger> findByTransactionTypeOrderByCreatedAtDesc(TransactionType transactionType, Pageable pageable);
}
