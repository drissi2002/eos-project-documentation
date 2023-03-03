package com.example.workflow.repository;

import com.example.workflow.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form,String> {
}
