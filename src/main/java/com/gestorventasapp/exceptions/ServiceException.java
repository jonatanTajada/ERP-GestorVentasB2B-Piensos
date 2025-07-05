package com.gestorventasapp.exceptions;

/**
 * Excepción personalizada para la capa service. Indica errores de negocio o
 * validaciones en los servicios de la aplicación.
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor con mensaje descriptivo.
	 * 
	 * @param message Mensaje de error.
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * Constructor con mensaje y causa original.
	 * 
	 * @param message Mensaje de error.
	 * @param cause   Causa original del error.
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
