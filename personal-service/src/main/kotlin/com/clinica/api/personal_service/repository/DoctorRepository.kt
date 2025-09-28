package com.clinica.api.personal_service.repository

import com.clinica.api.personal_service.model.Doctor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DoctorRepository : JpaRepository<Doctor, Long>