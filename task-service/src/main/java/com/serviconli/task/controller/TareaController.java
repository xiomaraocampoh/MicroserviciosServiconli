package com.serviconli.task.controller;

import com.serviconli.task.dto.HistorialTareaResponseDTO;
import com.serviconli.task.dto.TareaRequestDTO;
import com.serviconli.task.dto.TareaResponseDTO;
import com.serviconli.task.dto.TareaUpdateDTO;
import com.serviconli.task.exception.InvalidTaskStateException;
import com.serviconli.task.exception.ResourceNotFoundException;
import com.serviconli.task.model.EstadoTarea;
import com.serviconli.task.model.Prioridad;
import com.serviconli.task.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tareas")
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    // Endpoint para crear una nueva tarea
    @PostMapping
    public ResponseEntity<TareaResponseDTO> crearTarea(@Valid @RequestBody TareaRequestDTO tareaRequestDTO) {
        TareaResponseDTO nuevaTarea = tareaService.crearTarea(tareaRequestDTO);
        return new ResponseEntity<>(nuevaTarea, HttpStatus.CREATED);
    }

    // Endpoint para obtener una tarea por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerTareaPorId(@PathVariable Long id) {
        TareaResponseDTO tarea = tareaService.obtenerTareaPorId(id);
        return ResponseEntity.ok(tarea);
    }

    // Endpoint para obtener todas las tareas
    @GetMapping
    public ResponseEntity<List<TareaResponseDTO>> obtenerTodasLasTareas() {
        List<TareaResponseDTO> tareas = tareaService.obtenerTodasLasTareas();
        return ResponseEntity.ok(tareas);
    }

    // Endpoint para actualizar una tarea existente (PUT para actualización completa o PATCH para parcial)
    @PutMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> actualizarTarea(@PathVariable Long id, @Valid @RequestBody TareaUpdateDTO tareaUpdateDTO) {
        TareaResponseDTO tareaActualizada = tareaService.actualizarTarea(id, tareaUpdateDTO);
        return ResponseEntity.ok(tareaActualizada);
    }

    // Endpoint para eliminar una tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Endpoint para cambiar el estado de una tarea
    @PatchMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<TareaResponseDTO> cambiarEstadoTarea(@PathVariable Long id, @PathVariable EstadoTarea nuevoEstado) {
        TareaResponseDTO tareaActualizada = tareaService.cambiarEstadoTarea(id, nuevoEstado);
        return ResponseEntity.ok(tareaActualizada);
    }

    // Endpoint para filtrar tareas
    // Ejemplo de uso: /api/v1/tareas/filtrar?estado=PENDIENTE&prioridad=ALTA
    @GetMapping("/filtrar")
    public ResponseEntity<List<TareaResponseDTO>> filtrarTareas(
            @RequestParam(required = false) EstadoTarea estado,
            @RequestParam(required = false) Prioridad prioridad,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String paciente,
            @RequestParam(required = false) String eps) {
        List<TareaResponseDTO> tareasFiltradas = tareaService.filtrarTareas(estado, prioridad, tipo, paciente, eps);
        return ResponseEntity.ok(tareasFiltradas);
    }

    // Endpoint para obtener el historial de una tarea
    @GetMapping("/{tareaId}/historial")
    public ResponseEntity<List<HistorialTareaResponseDTO>> obtenerHistorialPorTarea(@PathVariable Long tareaId) {
        List<HistorialTareaResponseDTO> historial = tareaService.obtenerHistorialPorTarea(tareaId);
        return ResponseEntity.ok(historial);
    }

    // --- Manejo de Excepciones Global (para este controlador) ---

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskStateException.class)
    public ResponseEntity<String> handleInvalidTaskStateException(InvalidTaskStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}