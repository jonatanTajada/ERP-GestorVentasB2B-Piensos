package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones_proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevolucionProveedor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_devolucion_proveedor")
	private Integer idDevolucionProveedor;

	@NotNull(message = "La compra asociada es obligatoria")
	@ManyToOne
	@JoinColumn(name = "id_compra", nullable = false, foreignKey = @ForeignKey(name = "fk_devolucionproveedor_compra"))
	private Compra compra;

	@NotNull(message = "El empleado es obligatorio")
	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false, foreignKey = @ForeignKey(name = "fk_devolucionproveedor_empleado"))
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
