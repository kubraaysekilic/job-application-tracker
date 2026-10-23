package dev.portfolio.tracker;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ApplicationRepository extends JpaRepository<ApplicationRecord,Long> {}
