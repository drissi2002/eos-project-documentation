package com.example.workflow.repository;

import com.example.workflow.entity.FormData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormDataRepository extends JpaRepository<FormData,String> {
}
