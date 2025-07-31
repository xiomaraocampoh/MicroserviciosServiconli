package com.serviconli.patientservice.repository;


import com.serviconli.patientservice.model.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Long> {

    List<Beneficiario> findByNombreCompletoContainingIgnoreCase(String nombre);

    Optional<Beneficiario> findByNumeroIdentificacion(String numeroIdentificacion);
}
