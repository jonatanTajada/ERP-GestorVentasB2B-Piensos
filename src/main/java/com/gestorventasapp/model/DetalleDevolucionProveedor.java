package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado; // Importa tu Enum Estado

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_devoluciones_proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleDevolucionProveedor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle_devolucion_proveedor")
	private Integer idDetalleDevolucionProveedor;

	@NotNull(message = "La devolución a proveedor es obligatoria")
	@ManyToOne
	@JoinColumn(name = "id_devolucion_proveedor", nullable = false, foreignKey = @ForeignKey(name = "fk_detalledevolucionproveedor_devolucion"))
	private DevolucionProveedor devolucionProveedor;

	@NotNull(message = "El producto es obligatorio")
	@ManyToOne
	@JoinColumn(name = "id_producto", nullable = false, foreignKey = @ForeignKey(name = "fk_detalledevolucionproveedor_producto"))
	private Producto producto;

	@NotNull(message = "La cantidad es obligatoria")
	@Min(value = 1, message = "La cantidad debe ser mayor que 0")
	@Column(name = "cantidad", nullable = false)
	private Integer cantidad;

	@NotNull(message = "El porcentaje de IVA es obligatorio")
	@DecimalMin(value = "0.0", inclusive = true, message = "El porcentaje de IVA no puede ser negativo")
	@Digits(integer = 5, fraction = 2, message = "El porcentaje de IVA no puede tener más de 5 dígitos y 2 decimales")
	@Column(name = "porcentaje_iva", nullable = false, precision = 5, scale = 2)
	private BigDecimal porcentajeIva;

	@NotNull(message = "El subtotal sin IVA es obligatorio")
	@DecimalMin(value = "0.0", inclusive = true, message = "El subtotal sin IVA no puede ser negativo")
	@Digits(integer = 12, fraction = 2, message = "El subtotal sin IVA no puede tener más de 12 dígitos y 2 decimales")
	@Column(name = "subtotal_sin_iva", nullable = false, precision = 12, scale = 2)
	private BigDecimal subtotalSinIva;

	@NotNull(message = "El subtotal con IVA es obligatorio")
	@DecimalMin(value = "0.0", inclusive = true, message = "El subtotal con IVA no puede ser negativo")
	@Digits(integer = 12, fraction = 2, message = "El subtotal con IVA no puede tener más de 12 dígitos y 2 decimales")
	@Column(name = "subtotal_con_iva", nullable = false, precision = 12, scale = 2)
	private BigDecimal subtotalConIva;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo','inactivo') default 'activo'")
	@Builder.Default
	private Estado estado = Estado.activo;
}
