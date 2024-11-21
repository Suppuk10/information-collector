package com.avirantEnterprises.information_collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.avirantEnterprises.information_collector.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
