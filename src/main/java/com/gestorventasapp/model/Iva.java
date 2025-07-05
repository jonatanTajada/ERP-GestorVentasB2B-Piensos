package com.gestorventasapp.model;

import jakarta.persistence.*;
import lombok.*;
import com.gestorventasapp.enums.Estado;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "ivas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Iva implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_iva")
	private Integer idIva;

	@Column(name = "descripcion", nullable = false, length = 50)
	private String descripcion;

	@Column(name = "porcentaje", nullable = false, precision = 5, scale = 2)
	private BigDecimal porcentaje;

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
		Iva iva = (Iva) o;
		return Objects.equals(idIva, iva.idIva);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idIva);
	}
}
