package com.serviconli.patientservice.service;

import com.serviconli.patientservice.dto.BeneficiarioDTO;
import com.serviconli.patientservice.dto.BusquedaPacienteResponseDTO;
import com.serviconli.patientservice.dto.CotizanteDTO;

import java.util.List;

public interface PacienteService {

    List<BusquedaPacienteResponseDTO> buscarPorNombre(String nombre);
    BusquedaPacienteResponseDTO buscarPorIdentificacion(String numeroIdentificacion);

    CotizanteDTO crearCotizante(CotizanteDTO dto);
    BeneficiarioDTO crearBeneficiario(BeneficiarioDTO dto);

}
