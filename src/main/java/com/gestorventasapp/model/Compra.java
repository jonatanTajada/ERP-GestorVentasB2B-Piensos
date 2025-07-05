package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_compra")
	private Integer idCompra;

	@Column(name = "fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime fecha;

	@NotNull(message = "El proveedor es obligatorio")
	@ManyToOne
	@JoinColumn(name = "id_proveedor", nullable = false, foreignKey = @ForeignKey(name = "fk_compra_proveedor"))
	private Proveedor proveedor;

	@NotNull(message = "El empleado es obligatorio")
	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false, foreignKey = @ForeignKey(name = "fk_compra_empleado"))
	private Empleado empleado;

	@Digits(integer = 12, fraction = 2, message = "El total sin IVA no puede tener más de 12 dígitos y 2 decimales")
	@DecimalMin(value = "0.0", inclusive = true, message = "El total sin IVA no puede ser negativo")
	@Column(name = "total_sin_iva", precision = 12, scale = 2)
	private BigDecimal totalSinIva;

	@Digits(integer = 12, fraction = 2, message = "El total con IVA no puede tener más de 12 dígitos y 2 decimales")
	@DecimalMin(value = "0.0", inclusive = true, message = "El total con IVA no puede ser negativo")
	@Column(name = "total_con_iva", precision = 12, scale = 2)
	private BigDecimal totalConIva;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo','inactivo') default 'activo'")
	@Builder.Default
	private Estado estado = Estado.activo;
}
