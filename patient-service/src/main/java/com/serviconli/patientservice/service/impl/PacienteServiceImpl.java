package com.serviconli.patientservice.service.impl;

import com.serviconli.patientservice.dto.BeneficiarioDTO;
import com.serviconli.patientservice.dto.BusquedaPacienteResponseDTO;
import com.serviconli.patientservice.dto.CotizanteDTO;
import com.serviconli.patientservice.model.Beneficiario;
import com.serviconli.patientservice.model.Cotizante;
import com.serviconli.patientservice.repository.BeneficiarioRepository;
import com.serviconli.patientservice.repository.CotizanteRepository;
import com.serviconli.patientservice.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Importar Optional

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final CotizanteRepository cotizanteRepo;
    private final BeneficiarioRepository beneficiarioRepo;

    @Override
    public List<BusquedaPacienteResponseDTO> buscarPorNombre(String nombreCompleto) {
        List<BusquedaPacienteResponseDTO> resultado = new ArrayList<>();

        // Buscar cotizantes
        cotizanteRepo.findByNombreCompletoContainingIgnoreCase(nombreCompleto).forEach(c -> {
            var dto = new BusquedaPacienteResponseDTO();
            dto.setTipoPaciente("COTIZANTE");
            dto.setId(c.getId());
            dto.setNombreCompleto(c.getNombreCompleto());
            dto.setTipoIdentificacion(c.getTipoIdentificacion());
            dto.setNumeroIdentificacion(c.getNumeroIdentificacion());
            dto.setCelular(c.getCelular());
            resultado.add(dto);
        });

        // Buscar beneficiarios
        beneficiarioRepo.findByNombreCompletoContainingIgnoreCase(nombreCompleto).forEach(b -> {
            var dto = new BusquedaPacienteResponseDTO();
            dto.setTipoPaciente("BENEFICIARIO");
            dto.setId(b.getId());
            dto.setNombreCompleto(b.getNombreCompleto());
            dto.setTipoIdentificacion(b.getTipoIdentificacion());
            dto.setNumeroIdentificacion(b.getNumeroIdentificacion());
            dto.setCelular(b.getCelular());
            dto.setParentezco(b.getParentezco());
            // Acceder a los datos del cotizante a través de la relación JPA
            if (b.getCotizante() != null) { // Asegurarse de que el cotizante no sea nulo
                dto.setNombreCotizante(b.getCotizante().getNombreCompleto());
                dto.setNumeroIdentificacionCotizante(b.getCotizante().getNumeroIdentificacion());
            }
            resultado.add(dto);
        });

        return resultado;
    }

    @Override
    public BusquedaPacienteResponseDTO buscarPorIdentificacion(String numeroIdentificacion) {
        // Buscar primero en cotizantes
        Optional<Cotizante> cotizanteOptional = cotizanteRepo.findByNumeroIdentificacion(numeroIdentificacion);
        if (cotizanteOptional.isPresent()) {
            Cotizante c = cotizanteOptional.get();
            var dto = new BusquedaPacienteResponseDTO();
            dto.setTipoPaciente("COTIZANTE");
            dto.setId(c.getId());
            dto.setNombreCompleto(c.getNombreCompleto());
            dto.setTipoIdentificacion(c.getTipoIdentificacion());
            dto.setNumeroIdentificacion(c.getNumeroIdentificacion());
            dto.setCelular(c.getCelular());
            return dto;
        }

        // Si no se encuentra como cotizante, buscar en beneficiarios
        Optional<Beneficiario> beneficiarioOptional = beneficiarioRepo.findByNumeroIdentificacion(numeroIdentificacion);
        if (beneficiarioOptional.isPresent()) {
            Beneficiario b = beneficiarioOptional.get();
            var dto = new BusquedaPacienteResponseDTO();
            dto.setTipoPaciente("BENEFICIARIO");
            dto.setId(b.getId());
            dto.setNombreCompleto(b.getNombreCompleto());
            dto.setTipoIdentificacion(b.getTipoIdentificacion());
            dto.setNumeroIdentificacion(b.getNumeroIdentificacion());
            dto.setCelular(b.getCelular());
            dto.setParentezco(b.getParentezco());
            if (b.getCotizante() != null) { // Asegurarse de que el cotizante no sea nulo
                dto.setNombreCotizante(b.getCotizante().getNombreCompleto());
                dto.setNumeroIdentificacionCotizante(b.getCotizante().getNumeroIdentificacion());
            }
            return dto;
        }

        return null;
    }

    @Override
    public CotizanteDTO crearCotizante(CotizanteDTO dto) {
        Cotizante cotizante = new Cotizante();
        cotizante.setNombreCompleto(dto.getNombreCompleto());
        cotizante.setTipoIdentificacion(dto.getTipoIdentificacion());
        cotizante.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        cotizante.setCelular(dto.getCelular());
        cotizante.setCorreo(dto.getCorreo());

        Cotizante guardado = cotizanteRepo.save(cotizante);
        dto.setId(guardado.getId());
        return dto;
    }

    @Override
    public BeneficiarioDTO crearBeneficiario(BeneficiarioDTO dto) {
        Cotizante cotizante = cotizanteRepo.findById(dto.getCotizanteId())
                .orElseThrow(() -> new RuntimeException("Cotizante con ID " + dto.getCotizanteId() + " no encontrado. No se puede crear el beneficiario."));

        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setNombreCompleto(dto.getNombreCompleto());
        beneficiario.setTipoIdentificacion(dto.getTipoIdentificacion());
        beneficiario.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        beneficiario.setCelular(dto.getCelular());
        beneficiario.setParentezco(dto.getParentezco());
        beneficiario.setCotizante(cotizante);
        Beneficiario guardado = beneficiarioRepo.save(beneficiario);

        dto.setId(guardado.getId());
        return dto;
    }
}