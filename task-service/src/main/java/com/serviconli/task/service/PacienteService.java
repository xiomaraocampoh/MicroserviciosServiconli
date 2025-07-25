package com.serviconli.task.service;

import com.serviconli.task.dto.CotizanteDTO;
import com.serviconli.task.dto.BeneficiarioDTO;

public interface PacienteService {
    CotizanteDTO buscarCotizantePorNombre(String nombre);
    void guardarCotizante(CotizanteDTO dto);

    BeneficiarioDTO buscarBeneficiarioPorNombre(String nombre);
    void guardarBeneficiario(BeneficiarioDTO dto);
}
