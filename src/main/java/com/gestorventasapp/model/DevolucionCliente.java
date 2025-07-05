package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones_clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevolucionCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_devolucion_cliente")
	private Integer idDevolucionCliente;

	@NotNull(message = "La venta asociada es obligatoria")
	@ManyToOne
	@JoinColumn(name = "id_venta", nullable = false, foreignKey = @ForeignKey(name = "fk_devolucioncliente_venta"))
	private Venta venta;

	@NotNull(message = "El empleado es obligatorio")
	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false, foreignKey = @ForeignKey(name = "fk_devolucioncliente_empleado"))
	private Empleado empleado;

	@Column(name = "fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime fecha;

	@Size(max = 255, message = "El motivo no puede tener más de 255 caracteres")
	@Column(name = "motivo", length = 255)
	private String motivo;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo','inactivo') default 'activo'")
	@Builder.Default
	private Estado estado = Estado.activo;
}
