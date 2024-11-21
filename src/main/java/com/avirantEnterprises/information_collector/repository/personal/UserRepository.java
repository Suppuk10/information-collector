package com.avirantEnterprises.information_collector.repository.personal;

import org.springframework.data.jpa.repository.JpaRepository;
import com.avirantEnterprises.information_collector.model.personal.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
