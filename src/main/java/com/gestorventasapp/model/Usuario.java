package com.gestorventasapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoUsuario;

import java.io.Serializable;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@NotNull(message = "El empleado es obligatorio")
	@OneToOne
	@JoinColumn(name = "id_empleado", unique = true, nullable = false, foreignKey = @ForeignKey(name = "fk_usuario_empleado"))
	private Empleado empleado;

	@NotBlank(message = "El nombre de usuario es obligatorio")
	@Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres")
	@Column(name = "nombre_usuario", nullable = false, length = 50, unique = true)
	private String nombreUsuario;

	@NotBlank(message = "La contraseña es obligatoria")
	@Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,100}$", message = "La contraseña debe ser alfanumérica, tener al menos una mayúscula y un número")
	@Column(name = "contrasena", nullable = false, length = 100)
	private String contrasena;

	@NotNull(message = "El tipo de usuario es obligatorio")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false, columnDefinition = "ENUM('admin','usuario') default 'usuario'")
	@Builder.Default
	private TipoUsuario tipo = TipoUsuario.usuario;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo','inactivo') default 'activo'")
	@Builder.Default
	private Estado estado = Estado.activo;
}
