package com.serviconli.task.service;

import com.serviconli.task.dto.HistorialTareaResponseDTO;
import com.serviconli.task.dto.TareaRequestDTO;
import com.serviconli.task.dto.TareaResponseDTO;
import com.serviconli.task.dto.TareaUpdateDTO;
import com.serviconli.task.model.EstadoTarea;
import com.serviconli.task.model.Prioridad;

import java.util.List;

public interface TareaService {
    TareaResponseDTO crearTarea(TareaRequestDTO tareaRequestDTO);
    TareaResponseDTO obtenerTareaPorId(Long id);
    List<TareaResponseDTO> obtenerTodasLasTareas();
    TareaResponseDTO actualizarTarea(Long id, TareaUpdateDTO tareaUpdateDTO);
    void eliminarTarea(Long id);
    TareaResponseDTO cambiarEstadoTarea(Long id, EstadoTarea nuevoEstado);

    // Métodos de filtrado
    List<TareaResponseDTO> filtrarTareas(EstadoTarea estado, Prioridad prioridad, String tipo, String paciente, String eps);

    // Historial de cambios
    List<HistorialTareaResponseDTO> obtenerHistorialPorTarea(Long tareaId);
}