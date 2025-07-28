package com.serviconli.task.repository;

import com.serviconli.task.model.EstadoTarea;
import com.serviconli.task.model.Prioridad;
import com.serviconli.task.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    // Métodos de consulta personalizados
    List<Tarea> findByEstado(EstadoTarea estado);
    List<Tarea> findByPrioridad(Prioridad prioridad);
    List<Tarea> findByTipo(String tipo);
    List<Tarea> findByPacienteContainingIgnoreCase(String paciente);
    List<Tarea> findByEpsContainingIgnoreCase(String eps);

    // Combinación de filtros
    List<Tarea> findByEstadoAndPrioridad(EstadoTarea estado, Prioridad prioridad);
    List<Tarea> findByEstadoAndTipo(EstadoTarea estado, String tipo);

}