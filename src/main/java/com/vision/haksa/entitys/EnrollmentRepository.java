package com.vision.haksa.entitys;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Serializable> {
}