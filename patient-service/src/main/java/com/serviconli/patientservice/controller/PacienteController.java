package com.serviconli.patientservice.controller;

import com.serviconli.patientservice.dto.BeneficiarioDTO;
import com.serviconli.patientservice.dto.BusquedaPacienteResponseDTO;
import com.serviconli.patientservice.dto.CotizanteDTO;
import com.serviconli.patientservice.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/patients") // Define la ruta base para todos los endpoints en este controlador
@RequiredArgsConstructor // Genera un constructor con todos los campos 'final' para inyección de dependencias
public class PacienteController {

    private final PacienteService pacienteService; // Inyección del servicio

    // Endpoint para registrar un nuevo cotizante
    @PostMapping("/cotizantes")
    public ResponseEntity<CotizanteDTO> crearCotizante(@RequestBody CotizanteDTO cotizanteDTO) {
        CotizanteDTO nuevoCotizante = pacienteService.crearCotizante(cotizanteDTO);
        return new ResponseEntity<>(nuevoCotizante, HttpStatus.CREATED); // Retorna 201 Created
    }

    // Endpoint para registrar un nuevo beneficiario
    @PostMapping("/beneficiarios")
    public ResponseEntity<BeneficiarioDTO> crearBeneficiario(@RequestBody BeneficiarioDTO beneficiarioDTO) {
        BeneficiarioDTO nuevoBeneficiario = pacienteService.crearBeneficiario(beneficiarioDTO);
        return new ResponseEntity<>(nuevoBeneficiario, HttpStatus.CREATED); // Retorna 201 Created
    }

    // Endpoint para buscar pacientes por nombre (cotizantes o beneficiarios)
    @GetMapping("/search/by-name")
    public ResponseEntity<List<BusquedaPacienteResponseDTO>> buscarPacientesPorNombre(@RequestParam String name) {
        List<BusquedaPacienteResponseDTO> pacientesEncontrados = pacienteService.buscarPorNombre(name);
        if (pacientesEncontrados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 No Content si no se encuentran resultados
        }
        return new ResponseEntity<>(pacientesEncontrados, HttpStatus.OK); // Retorna 200 OK
    }

    // Endpoint para buscar un paciente por número de identificación
    @GetMapping("/search/by-id")
    public ResponseEntity<BusquedaPacienteResponseDTO> buscarPacientePorIdentificacion(@RequestParam String id) {
        BusquedaPacienteResponseDTO pacienteEncontrado = pacienteService.buscarPorIdentificacion(id);
        if (pacienteEncontrado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 Not Found si el paciente no existe
        }
        return new ResponseEntity<>(pacienteEncontrado, HttpStatus.OK); // Retorna 200 OK
    }

    // Consideraciones para futuras funcionalidades:
    // @PutMapping para actualizar un paciente (tanto cotizante como beneficiario)
    // @DeleteMapping para eliminar un paciente (considerar las implicaciones de FK)
}