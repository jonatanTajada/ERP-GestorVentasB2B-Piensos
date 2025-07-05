package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_auditoria")
	private Integer idAuditoria;

	@Size(max = 50, message = "El nombre de la tabla modificada no puede tener más de 50 caracteres")
	@Column(name = "tabla_modificada", length = 50)
	private String tablaModificada;

	@Size(max = 50, message = "El nombre de la acción no puede tener más de 50 caracteres")
	@Column(name = "accion", length = 50)
	private String accion;

	@Column(name = "fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime fecha;

	@Column(name = "descripcion", columnDefinition = "TEXT")
	private String descripcion;
}
