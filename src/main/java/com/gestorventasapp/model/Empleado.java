package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_empleado")
	private Integer idEmpleado;

	@NotBlank(message = "El DNI es obligatorio")
	@Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "El DNI debe tener 8 dígitos y una letra mayúscula (ej: 12345678A)")
	@Size(max = 9, message = "El DNI no puede tener más de 9 caracteres")
	@Column(name = "dni", nullable = false, length = 9, unique = true)
	private String dni;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@NotBlank(message = "El primer apellido es obligatorio")
	@Size(max = 50, message = "El primer apellido no puede tener más de 50 caracteres")
	@Column(name = "apellido1", nullable = false, length = 50)
	private String apellido1;

	@Size(max = 50, message = "El segundo apellido no puede tener más de 50 caracteres")
	@Column(name = "apellido2", length = 50)
	private String apellido2;

	@Size(max = 150, message = "La dirección no puede tener más de 150 caracteres")
	@Column(name = "direccion", length = 150)
	private String direccion;

	@Size(max = 80, message = "La localidad no puede tener más de 80 caracteres")
	@Column(name = "localidad", length = 80)
	private String localidad;

	@NotBlank(message = "El código postal es obligatorio")
	@Size(min = 5, max = 5, message = "El código postal debe tener exactamente 5 dígitos")
	@Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe contener solo dígitos")
	@Column(name = "codigo_postal", length = 5)
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
}
