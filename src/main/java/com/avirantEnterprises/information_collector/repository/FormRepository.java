package com.avirantEnterprises.information_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.avirantEnterprises.information_collector.model.Form;

public interface FormRepository extends JpaRepository<Form, Long> {
}
