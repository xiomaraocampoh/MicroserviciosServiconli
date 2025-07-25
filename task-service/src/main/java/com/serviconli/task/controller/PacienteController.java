package com.serviconli.task.controller;

import com.serviconli.task.dto.BeneficiarioDTO;
import com.serviconli.task.dto.CotizanteDTO;
import com.serviconli.task.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/cotizante")
    public ResponseEntity<CotizanteDTO> buscarCotizante(@RequestParam String nombre) {
        CotizanteDTO dto = pacienteService.buscarCotizantePorNombre(nombre);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/cotizante")
    public ResponseEntity<Void> guardarCotizante(@RequestBody CotizanteDTO dto) {
        pacienteService.guardarCotizante(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/beneficiario")
    public ResponseEntity<BeneficiarioDTO> buscarBeneficiario(@RequestParam String nombre) {
        BeneficiarioDTO dto = pacienteService.buscarBeneficiarioPorNombre(nombre);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/beneficiario")
    public ResponseEntity<Void> guardarBeneficiario(@RequestBody BeneficiarioDTO dto) {
        pacienteService.guardarBeneficiario(dto);
        return ResponseEntity.ok().build();
    }
}
