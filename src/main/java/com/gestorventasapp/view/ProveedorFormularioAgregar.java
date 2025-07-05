package com.gestorventasapp.view;

import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.FormaJuridica;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.exceptions.ControllerException;
import com.gestorventasapp.exceptions.ServiceException;
import com.gestorventasapp.exceptions.DAOException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ProveedorFormularioAgregar extends JDialog {

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
	private JButton btnGuardar;
	private JButton btnCancelar;

	private final ProveedorController proveedorController;
	private final Runnable onProveedorGuardado; // Callback para refrescar tabla
	private final Proveedor proveedorEditando; // Si es nulo, es alta

	// Constructor para ALTA (crear proveedor)
	public ProveedorFormularioAgregar(Window parent, String titulo, ProveedorController proveedorController,
			Runnable onProveedorGuardado) {
		this(parent, titulo, proveedorController, onProveedorGuardado, null);
	}

	// Constructor para MODIFICAR (editar proveedor)
	public ProveedorFormularioAgregar(Window parent, String titulo, ProveedorController proveedorController,
			Runnable onProveedorGuardado, Proveedor proveedorEditar) {
		super(parent, titulo, ModalityType.APPLICATION_MODAL);
		this.proveedorController = proveedorController;
		this.onProveedorGuardado = onProveedorGuardado;
		this.proveedorEditando = proveedorEditar;

		setSize(450, 500);
		setResizable(false);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		add(crearPanelCentral(), BorderLayout.CENTER);
		add(crearPanelInferior(), BorderLayout.SOUTH);

		if (proveedorEditando != null) {
			cargarDatosProveedor(proveedorEditando);
		}
	}

	/**
	 * Rellena los campos del formulario con los datos del proveedor a editar.
	 */
	private void cargarDatosProveedor(Proveedor proveedor) {
		campoRazonSocial.setText(proveedor.getRazonSocial());
		comboFormaJuridica.setSelectedItem(proveedor.getFormaJuridica());
		campoCifNif.setText(proveedor.getCifNif());
		campoDireccion.setText(proveedor.getDireccion());
		campoLocalidad.setText(proveedor.getLocalidad());
		campoCodigoPostal.setText(proveedor.getCodigoPostal());
		campoPais.setText(proveedor.getPais());
		campoTelefono.setText(proveedor.getTelefono());
		campoEmail.setText(proveedor.getEmail());

	}

	private JPanel crearPanelCentral() {
		JPanel panel = new JPanel(new GridLayout(9, 2, 10, 8)); // 9 porque no hay "estado"
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

		return panel;
	}

	private JPanel crearPanelInferior() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(EstiloUI.getColor("secundario"));

		btnGuardar = new JButton(proveedorEditando == null ? "Guardar" : "Actualizar");
		btnCancelar = new JButton("Cancelar");
		EstiloUI.aplicarEstiloBoton(btnGuardar);
		EstiloUI.aplicarEstiloBoton(btnCancelar);

		btnGuardar.addActionListener(e -> guardarOActualizarProveedor());
		btnCancelar.addActionListener(e -> dispose());

		panel.add(btnGuardar);
		panel.add(btnCancelar);
		return panel;
	}

	/**
	 * Guarda un nuevo proveedor o actualiza uno existente, según el modo.
	 */
	private void guardarOActualizarProveedor() {
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

			if (proveedorEditando == null) {
				// --- Alta de proveedor ---
				Proveedor nuevo = new Proveedor();
				nuevo.setRazonSocial(campoRazonSocial.getText().trim());
				nuevo.setFormaJuridica((FormaJuridica) comboFormaJuridica.getSelectedItem());
				nuevo.setCifNif(campoCifNif.getText().trim());
				nuevo.setDireccion(campoDireccion.getText().trim());
				nuevo.setLocalidad(campoLocalidad.getText().trim());
				nuevo.setCodigoPostal(campoCodigoPostal.getText().trim());
				nuevo.setPais(campoPais.getText().trim());
				nuevo.setTelefono(campoTelefono.getText().trim());
				nuevo.setEmail(campoEmail.getText().trim());
				nuevo.setEstado(Estado.activo); // SIEMPRE ACTIVO EN ALTA
				nuevo.setFechaAlta(LocalDateTime.now());

				proveedorController.crearProveedor(nuevo);
				JOptionPane.showMessageDialog(this, "Proveedor creado correctamente.");
			} else {
				// --- Modificación de proveedor ---
				proveedorEditando.setRazonSocial(campoRazonSocial.getText().trim());
				proveedorEditando.setFormaJuridica((FormaJuridica) comboFormaJuridica.getSelectedItem());
				proveedorEditando.setCifNif(campoCifNif.getText().trim());
				proveedorEditando.setDireccion(campoDireccion.getText().trim());
				proveedorEditando.setLocalidad(campoLocalidad.getText().trim());
				proveedorEditando.setCodigoPostal(campoCodigoPostal.getText().trim());
				proveedorEditando.setPais(campoPais.getText().trim());
				proveedorEditando.setTelefono(campoTelefono.getText().trim());
				proveedorEditando.setEmail(campoEmail.getText().trim());

				proveedorController.modificarProveedor(proveedorEditando);
				JOptionPane.showMessageDialog(this, "Proveedor actualizado correctamente.");
			}

			if (onProveedorGuardado != null)
				onProveedorGuardado.run();
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
