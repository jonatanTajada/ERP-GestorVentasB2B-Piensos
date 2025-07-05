package com.gestorventasapp.exceptions;

/**
 * Excepción personalizada para la capa DAO. Indica errores de acceso o
 * manipulación de datos en la base de datos.
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor con mensaje descriptivo.
	 * 
	 * @param message Mensaje de error.
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructor con mensaje y causa original.
	 * 
	 * @param message Mensaje de error.
	 * @param cause   Causa original del error.
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
