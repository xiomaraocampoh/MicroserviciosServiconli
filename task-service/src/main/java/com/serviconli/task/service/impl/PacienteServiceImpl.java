package com.serviconli.task.service.impl;

import com.serviconli.task.dto.BeneficiarioDTO;
import com.serviconli.task.dto.CotizanteDTO;
import com.serviconli.task.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    @Qualifier("serviconliJdbcTemplate")
    private JdbcTemplate jdbc;

    @Override
    public CotizanteDTO buscarCotizantePorNombre(String nombre) {
        String sql = "SELECT * FROM cotizantes WHERE LOWER(nombre_completo) = ?";
        List<CotizanteDTO> result = jdbc.query(sql, new Object[]{nombre.toLowerCase()}, (rs, rowNum) ->
                new CotizanteDTO(
                        rs.getString("nombre_completo"),
                        rs.getString("tipo_identificacion"),
                        rs.getString("numero_identificacion"),
                        rs.getDate("fecha_expedicion").toLocalDate(),
                        rs.getString("celular"),
                        rs.getString("eps")
                )
        );
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void guardarCotizante(CotizanteDTO dto) {
        String sql = "INSERT INTO cotizantes (nombre_completo, tipo_identificacion, numero_identificacion, fecha_expedicion, celular, eps) VALUES (?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                dto.getNombreCompleto(),
                dto.getTipoIdentificacion(),
                dto.getNumeroIdentificacion(),
                Date.valueOf(dto.getFechaExpedicion()),
                dto.getCelular(),
                dto.getEps()
        );
    }

    @Override
    public BeneficiarioDTO buscarBeneficiarioPorNombre(String nombre) {
        String sql = "SELECT * FROM beneficiarios WHERE LOWER(nombre_completo) = ?";
        List<BeneficiarioDTO> result = jdbc.query(sql, new Object[]{nombre.toLowerCase()}, (rs, rowNum) ->
                new BeneficiarioDTO(
                        rs.getString("nombre_completo"),
                        rs.getString("tipo_identificacion"),
                        rs.getString("numero_identificacion"),
                        rs.getDate("fecha_expedicion").toLocalDate(),
                        rs.getString("celular"),
                        rs.getString("parentesco"),
                        rs.getString("eps"),
                        rs.getString("nombre_cotizante"),
                        rs.getString("tipo_identificacion_cotizante"),
                        rs.getString("numero_identificacion_cotizante")
                )
        );
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void guardarBeneficiario(BeneficiarioDTO dto) {
        String sql = "INSERT INTO beneficiarios (nombre_completo, tipo_identificacion, numero_identificacion, fecha_expedicion, celular, parentesco, eps, nombre_cotizante, tipo_identificacion_cotizante, numero_identificacion_cotizante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                dto.getNombreCompleto(),
                dto.getTipoIdentificacion(),
                dto.getNumeroIdentificacion(),
                Date.valueOf(dto.getFechaExpedicion()),
                dto.getCelular(),
                dto.getParentesco(),
                dto.getEps(),
                dto.getNombreCotizante(),
                dto.getTipoIdentificacionCotizante(),
                dto.getNumeroIdentificacionCotizante()
        );
    }
}
