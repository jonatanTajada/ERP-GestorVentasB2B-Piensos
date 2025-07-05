package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoAnimal;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_producto")
	private Integer idProducto;

	@NotBlank(message = "El nombre del producto es obligatorio")
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;

	@Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
	@Column(name = "descripcion", length = 255)
	private String descripcion;

	@NotNull(message = "El tipo de animal es obligatorio")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_animal", nullable = false)
	private TipoAnimal tipoAnimal;

	@NotBlank(message = "La marca es obligatoria")
	@Size(max = 50, message = "La marca no puede tener más de 50 caracteres")
	@Column(name = "marca", nullable = false, length = 50)
	private String marca;

	@NotBlank(message = "El formato es obligatorio")
	@Size(max = 50, message = "El formato no puede tener más de 50 caracteres")
	@Column(name = "formato", nullable = false, length = 50)
	private String formato;

	@NotNull(message = "El precio de venta es obligatorio")
	@DecimalMin(value = "0.0", inclusive = true, message = "El precio de venta no puede ser negativo")
	@Digits(integer = 10, fraction = 2, message = "El precio de venta no puede tener más de 10 dígitos y 2 decimales")
	@Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioVenta;

	@NotNull(message = "El precio de compra es obligatorio")
	@DecimalMin(value = "0.0", inclusive = true, message = "El precio de compra no puede ser negativo")
	@Digits(integer = 10, fraction = 2, message = "El precio de compra no puede tener más de 10 dígitos y 2 decimales")
	@Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioCompra;

	@NotNull(message = "El proveedor es obligatorio")
	@ManyToOne
	@JoinColumn(name = "id_proveedor", nullable = false, foreignKey = @ForeignKey(name = "fk_producto_proveedor"))
	private Proveedor proveedor;

	@NotNull(message = "El tipo de IVA es obligatorio")
	@ManyToOne
	@JoinColumn(name = "id_iva", nullable = false, foreignKey = @ForeignKey(name = "fk_producto_iva"))
	private Iva iva;

	@NotNull(message = "El stock es obligatorio")
	@Min(value = 0, message = "El stock no puede ser negativo")
	@Column(name = "stock", nullable = false)
	private Integer stock;

	@Min(value = 0, message = "El stock mínimo no puede ser negativo")
	@Column(name = "stock_minimo")
	private Integer stockMinimo;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo','inactivo') default 'activo'")
	@Builder.Default
	private Estado estado = Estado.activo;
}
