package com.gestorventasapp.exceptions;

/**
 * Excepción personalizada para la capa controller. Utilizada para indicar
 * errores de validación, entrada de usuario, o problemas específicos de la
 * interacción entre la vista y el controlador.
 */
public class ControllerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor simple con mensaje de error.
	 * 
	 * @param message Mensaje descriptivo del error.
	 */
	public ControllerException(String message) {
		super(message);
	}

	/**
	 * Constructor con mensaje y causa.
	 * 
	 * @param message Mensaje descriptivo del error.
	 * @param cause   Excepción original que causó este error.
	 */
	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}
}
