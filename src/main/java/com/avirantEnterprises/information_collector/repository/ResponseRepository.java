package com.avirantEnterprises.information_collector.repository;

import com.avirantEnterprises.information_collector.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import com.avirantEnterprises.information_collector.model.Response;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByForm(Form form);
}
