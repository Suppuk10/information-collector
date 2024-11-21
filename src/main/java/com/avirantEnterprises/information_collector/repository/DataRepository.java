package com.avirantEnterprises.information_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.avirantEnterprises.information_collector.model.Data;

public interface DataRepository extends JpaRepository<Data, Long> {
}
