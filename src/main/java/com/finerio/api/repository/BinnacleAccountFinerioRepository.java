package com.finerio.api.repository;

import com.finerio.api.model.entity.BinnacleAccountFinerio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinnacleAccountFinerioRepository extends JpaRepository<BinnacleAccountFinerio, String> {
}
