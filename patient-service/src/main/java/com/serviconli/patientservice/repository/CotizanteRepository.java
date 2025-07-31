package com.serviconli.patientservice.repository;

import com.serviconli.patientservice.model.Cotizante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CotizanteRepository extends JpaRepository<Cotizante, Long> {

    Optional<Cotizante> findByNumeroIdentificacion(String numeroIdentificacion);
    List<Cotizante> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);
}
