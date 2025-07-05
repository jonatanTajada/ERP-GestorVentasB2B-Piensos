package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.FormaJuridica;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_proveedor")
	private Integer idProveedor;

	@NotBlank(message = "La razón social no puede estar vacía")
	@Size(max = 100, message = "La razón social no puede tener más de 100 caracteres")
	@Column(name = "razon_social", nullable = false, length = 100)
	private String razonSocial;

	@NotNull(message = "La forma jurídica es obligatoria")
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_juridica", nullable = false)
	private FormaJuridica formaJuridica;

	@NotBlank(message = "El CIF/NIF es obligatorio")
	@Size(max = 20, message = "El CIF/NIF no puede tener más de 20 caracteres")
	@Column(name = "cif_nif", nullable = false, length = 20, unique = true)
	private String cifNif;

	@Size(max = 150, message = "La dirección no puede tener más de 150 caracteres")
	@Column(name = "direccion", length = 150)
	private String direccion;

	@Size(max = 80, message = "La localidad no puede tener más de 80 caracteres")
	@Column(name = "localidad", length = 80)
	private String localidad;

	@Size(max = 10, message = "El código postal no puede tener más de 10 caracteres")
	@Column(name = "codigo_postal", length = 10)
	private String codigoPostal;

	@Size(max = 50, message = "El país no puede tener más de 50 caracteres")
	@Column(name = "pais", length = 50)
	private String pais;

	@NotBlank(message = "El teléfono es obligatorio")
	@Pattern(regexp = "^[6789][0-9]{8}$", message = "El teléfono debe tener 9 dígitos y empezar por 6, 7, 8 o 9")
	@Column(name = "telefono", nullable = false, length = 9)
	private String telefono;

	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El formato del email no es válido")
	@Size(max = 100, message = "El email no puede tener más de 100 caracteres")
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "fecha_alta", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime fechaAlta;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo','inactivo') default 'activo'")
	@Builder.Default
	private Estado estado = Estado.activo;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Proveedor proveedor = (Proveedor) o;
		return Objects.equals(idProveedor, proveedor.idProveedor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idProveedor);
	}

}
