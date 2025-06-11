package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.TransactionEntity;
import com.openclassrooms.paymybuddy.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    @Query("Select t from TransactionEntity t where t.sender = :user or t.receiver = :user order by t.id desc")
    List<TransactionEntity> findAllBySenderOrReceiver(@Param("user") UserEntity user);
}
