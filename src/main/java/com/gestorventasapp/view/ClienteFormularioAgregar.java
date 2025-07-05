package com.gestorventasapp.view;

import com.gestorventasapp.controller.ClienteController;
import com.gestorventasapp.enums.FormaJuridica;
import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.exceptions.ControllerException;
import com.gestorventasapp.exceptions.ServiceException;
import com.gestorventasapp.exceptions.DAOException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ClienteFormularioAgregar extends JDialog {

	// Campos de formulario
	private JTextField campoRazonSocial;
	private JComboBox<FormaJuridica> comboFormaJuridica;
	private JTextField campoCifNif;
	private JTextField campoDireccion;
	private JTextField campoLocalidad;
	private JTextField campoCodigoPostal;
	private JTextField campoPais;
	private JTextField campoTelefono;
	private JTextField campoEmail;
	private JTextField campoTipoCliente;
	private JButton btnGuardar;
	private JButton btnCancelar;

	private final ClienteController clienteController;
	private final Runnable onClienteGuardado; // Callback para refrescar tabla

	// Nuevo: Cliente que se está editando (si es nulo, modo alta)
	private final Cliente clienteEditando;

	// --- Constructor para ALTA (crear cliente) ---
	public ClienteFormularioAgregar(Window parent, String titulo, ClienteController clienteController,
			Runnable onClienteGuardado) {
		this(parent, titulo, clienteController, onClienteGuardado, null);
	}

	// --- Constructor para MODIFICAR (editar cliente) ---
	public ClienteFormularioAgregar(Window parent, String titulo, ClienteController clienteController,
			Runnable onClienteGuardado, Cliente clienteEditar) {
		super(parent, titulo, ModalityType.APPLICATION_MODAL);
		this.clienteController = clienteController;
		this.onClienteGuardado = onClienteGuardado;
		this.clienteEditando = clienteEditar;

		setSize(450, 500);
		setResizable(false);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		add(crearPanelCentral(), BorderLayout.CENTER);
		add(crearPanelInferior(), BorderLayout.SOUTH);

		if (clienteEditando != null) {
			// Si estamos editando, rellenar los campos
			cargarDatosCliente(clienteEditando);
		}
	}

	/**
	 * Rellena los campos del formulario con los datos del cliente a editar.
	 */
	private void cargarDatosCliente(Cliente cliente) {
		campoRazonSocial.setText(cliente.getRazonSocial());
		comboFormaJuridica.setSelectedItem(cliente.getFormaJuridica());
		campoCifNif.setText(cliente.getCifNif());
		campoDireccion.setText(cliente.getDireccion());
		campoLocalidad.setText(cliente.getLocalidad());
		campoCodigoPostal.setText(cliente.getCodigoPostal());
		campoPais.setText(cliente.getPais());
		campoTelefono.setText(cliente.getTelefono());
		campoEmail.setText(cliente.getEmail());
		campoTipoCliente.setText(cliente.getTipoCliente());

	}

	private JPanel crearPanelCentral() {
		JPanel panel = new JPanel(new GridLayout(10, 2, 10, 8));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		panel.setBackground(EstiloUI.getColor("secundario"));

		JLabel lblRazon = new JLabel("Razón Social*:");
		campoRazonSocial = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblRazon);
		EstiloUI.aplicarEstiloCampoTexto(campoRazonSocial);

		JLabel lblForma = new JLabel("Forma Jurídica*:");
		comboFormaJuridica = new JComboBox<>(FormaJuridica.values());
		EstiloUI.aplicarEstiloEtiqueta(lblForma);

		JLabel lblCif = new JLabel("CIF/NIF*:");
		campoCifNif = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblCif);
		EstiloUI.aplicarEstiloCampoTexto(campoCifNif);

		JLabel lblDir = new JLabel("Dirección:");
		campoDireccion = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblDir);
		EstiloUI.aplicarEstiloCampoTexto(campoDireccion);

		JLabel lblLoc = new JLabel("Localidad:");
		campoLocalidad = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblLoc);
		EstiloUI.aplicarEstiloCampoTexto(campoLocalidad);

		JLabel lblCP = new JLabel("Código Postal:");
		campoCodigoPostal = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblCP);
		EstiloUI.aplicarEstiloCampoTexto(campoCodigoPostal);

		JLabel lblPais = new JLabel("País:");
		campoPais = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblPais);
		EstiloUI.aplicarEstiloCampoTexto(campoPais);

		JLabel lblTel = new JLabel("Teléfono*:");
		campoTelefono = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblTel);
		EstiloUI.aplicarEstiloCampoTexto(campoTelefono);

		JLabel lblEmail = new JLabel("Email*:");
		campoEmail = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblEmail);
		EstiloUI.aplicarEstiloCampoTexto(campoEmail);

		JLabel lblTipo = new JLabel("Tipo Cliente:");
		campoTipoCliente = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblTipo);
		EstiloUI.aplicarEstiloCampoTexto(campoTipoCliente);

		panel.add(lblRazon);
		panel.add(campoRazonSocial);
		panel.add(lblForma);
		panel.add(comboFormaJuridica);
		panel.add(lblCif);
		panel.add(campoCifNif);
		panel.add(lblDir);
		panel.add(campoDireccion);
		panel.add(lblLoc);
		panel.add(campoLocalidad);
		panel.add(lblCP);
		panel.add(campoCodigoPostal);
		panel.add(lblPais);
		panel.add(campoPais);
		panel.add(lblTel);
		panel.add(campoTelefono);
		panel.add(lblEmail);
		panel.add(campoEmail);
		panel.add(lblTipo);
		panel.add(campoTipoCliente);

		return panel;
	}

	private JPanel crearPanelInferior() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(EstiloUI.getColor("secundario"));

		btnGuardar = new JButton(clienteEditando == null ? "Guardar" : "Actualizar");
		btnCancelar = new JButton("Cancelar");
		EstiloUI.aplicarEstiloBoton(btnGuardar);
		EstiloUI.aplicarEstiloBoton(btnCancelar);

		btnGuardar.addActionListener(e -> guardarOActualizarCliente());
		btnCancelar.addActionListener(e -> dispose());

		panel.add(btnGuardar);
		panel.add(btnCancelar);
		return panel;
	}

	/**
	 * Guarda un nuevo cliente o actualiza uno existente, según el modo.
	 */
	private void guardarOActualizarCliente() {
		try {
			// Validaciones
			if (campoRazonSocial.getText().trim().isEmpty() || comboFormaJuridica.getSelectedItem() == null
					|| campoCifNif.getText().trim().isEmpty() || campoTelefono.getText().trim().isEmpty()
					|| campoEmail.getText().trim().isEmpty()) {
				throw new IllegalArgumentException("Completa todos los campos obligatorios (*)");
			}
			if (!campoEmail.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
				throw new IllegalArgumentException("El email no tiene un formato válido.");
			}
			if (!campoTelefono.getText().matches("^[6789][0-9]{8}$")) {
				throw new IllegalArgumentException("El teléfono debe tener 9 dígitos y empezar por 6, 7, 8 o 9.");
			}

			if (clienteEditando == null) {
				// --- Alta de cliente ---
				Cliente nuevo = new Cliente();
				nuevo.setRazonSocial(campoRazonSocial.getText().trim());
				nuevo.setFormaJuridica((FormaJuridica) comboFormaJuridica.getSelectedItem());
				nuevo.setCifNif(campoCifNif.getText().trim());
				nuevo.setDireccion(campoDireccion.getText().trim());
				nuevo.setLocalidad(campoLocalidad.getText().trim());
				nuevo.setCodigoPostal(campoCodigoPostal.getText().trim());
				nuevo.setPais(campoPais.getText().trim());
				nuevo.setTelefono(campoTelefono.getText().trim());
				nuevo.setEmail(campoEmail.getText().trim());
				nuevo.setTipoCliente(campoTipoCliente.getText().trim());
				nuevo.setFechaAlta(LocalDateTime.now());

				clienteController.crearCliente(nuevo);
				JOptionPane.showMessageDialog(this, "Cliente creado correctamente.");
			} else {
				// --- Modificación de cliente ---
				clienteEditando.setRazonSocial(campoRazonSocial.getText().trim());
				clienteEditando.setFormaJuridica((FormaJuridica) comboFormaJuridica.getSelectedItem());
				clienteEditando.setCifNif(campoCifNif.getText().trim());
				clienteEditando.setDireccion(campoDireccion.getText().trim());
				clienteEditando.setLocalidad(campoLocalidad.getText().trim());
				clienteEditando.setCodigoPostal(campoCodigoPostal.getText().trim());
				clienteEditando.setPais(campoPais.getText().trim());
				clienteEditando.setTelefono(campoTelefono.getText().trim());
				clienteEditando.setEmail(campoEmail.getText().trim());
				clienteEditando.setTipoCliente(campoTipoCliente.getText().trim());
				clienteController.modificarCliente(clienteEditando);
				JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
			}

			if (onClienteGuardado != null)
				onClienteGuardado.run();
			dispose();

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Datos inválidos",
					JOptionPane.ERROR_MESSAGE);
		} catch (ControllerException | ServiceException | DAOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en la operación", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error inesperado",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
