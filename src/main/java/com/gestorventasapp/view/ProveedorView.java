package com.gestorventasapp.view;

import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.exceptions.ControllerException;
import com.gestorventasapp.exceptions.ServiceException;
import com.gestorventasapp.exceptions.DAOException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista principal para la gestión de proveedores (CRUD + buscador + filtro por
 * estado). Igual que ClienteView, pero para Proveedores.
 */
public class ProveedorView extends ModuloBaseView {

	private final ProveedorController proveedorController;
	private List<Proveedor> listaProveedoresOriginales;

	private static final String[] COLUMNAS = { "ID", "Razón Social", "Forma Jurídica", "CIF/NIF", "Localidad",
			"Teléfono", "Email", "Fecha Alta", "Estado" };

	private JComboBox<String> comboEstado;

	public ProveedorView(Usuario usuarioEnSesion, VistaPrincipal vistaPrincipal,
			ProveedorController proveedorController) {
		super(usuarioEnSesion, vistaPrincipal, "Gestión de Proveedores", COLUMNAS);
		this.proveedorController = proveedorController;
		inicializarPanelFiltros();
		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}

	// Panel con buscador y filtro por estado
	private void inicializarPanelFiltros() {
		if (panelFiltros != null) {
			panelFiltros.removeAll();
			JTextField campoBuscador = new JTextField(20);
			JPanel panelBuscador = EstiloUI.crearPanelBuscadorDinamico("Buscar:", campoBuscador);

			comboEstado = new JComboBox<>(new String[] { "Todos", "Activo", "Inactivo" });
			comboEstado.setFont(panelBuscador.getFont());
			JPanel panelEstado = new JPanel(new BorderLayout(10, 0));
			JLabel lblEstado = new JLabel("Estado:");
			EstiloUI.aplicarEstiloEtiqueta(lblEstado);
			panelEstado.add(lblEstado, BorderLayout.WEST);
			panelEstado.add(comboEstado, BorderLayout.CENTER);
			panelEstado.setBackground(EstiloUI.getColor("secundario"));

			panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 4));
			panelFiltros.add(panelBuscador);
			panelFiltros.add(panelEstado);

			// Listeners
			campoBuscador.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					filtrarYActualizar();
				}

				public void removeUpdate(DocumentEvent e) {
					filtrarYActualizar();
				}

				public void changedUpdate(DocumentEvent e) {
					filtrarYActualizar();
				}
			});
			comboEstado.addActionListener(e -> filtrarYActualizar());
		}
	}

	private void filtrarYActualizar() {
		String texto = "";
		if (panelFiltros != null && panelFiltros.getComponentCount() > 0) {
			JPanel buscador = (JPanel) panelFiltros.getComponent(0);
			JTextField campo = (JTextField) buscador.getComponent(1);
			texto = campo.getText().toLowerCase();
		}
		String estado = comboEstado != null ? (String) comboEstado.getSelectedItem() : "Todos";
		Object[][] datos = obtenerDatosFiltradosAvanzado(texto, estado);
		actualizarTabla(datos);
	}

	private Object[][] obtenerDatosFiltradosAvanzado(String textoFiltro, String estadoFiltro) {
		return listaProveedoresOriginales.stream()
				.filter(p -> (p.getRazonSocial().toLowerCase().contains(textoFiltro)
						|| p.getCifNif().toLowerCase().contains(textoFiltro)
						|| p.getEmail().toLowerCase().contains(textoFiltro)
						|| (p.getTelefono() != null && p.getTelefono().contains(textoFiltro))
						|| (p.getLocalidad() != null && p.getLocalidad().toLowerCase().contains(textoFiltro)))
						&& (estadoFiltro.equals("Todos")
								|| (estadoFiltro.equalsIgnoreCase("Activo") && p.getEstado() != null
										&& p.getEstado().name().equalsIgnoreCase("activo"))
								|| (estadoFiltro.equalsIgnoreCase("Inactivo") && p.getEstado() != null
										&& p.getEstado().name().equalsIgnoreCase("inactivo"))))
				.map(this::proveedorToRow).toArray(Object[][]::new);
	}

	@Override
	protected void cargarDatosOriginales() {
		try {
			listaProveedoresOriginales = proveedorController.listarTodos();
			filtrarYActualizar();
		} catch (ControllerException | ServiceException | DAOException ex) {
			JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error al cargar proveedores",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Object[] proveedorToRow(Proveedor p) {
		return new Object[] { p.getIdProveedor(), p.getRazonSocial(),
				p.getFormaJuridica() != null ? p.getFormaJuridica().name() : "", p.getCifNif(), p.getLocalidad(),
				p.getTelefono(), p.getEmail(),
				p.getFechaAlta() != null ? p.getFechaAlta().toLocalDate().toString() : "",
				p.getEstado() != null ? p.getEstado().name() : "" };
	}

	// --- Botones CRUD ---
	@Override
	protected void inicializarBotones() {
		JButton btnAgregar = new JButton("Agregar Proveedor");
		JButton btnModificar = new JButton("Modificar Proveedor");
		JButton btnEliminar = new JButton("Eliminar (baja lógica)");
		JButton btnActualizar = new JButton("Actualizar");

		EstiloUI.aplicarEstiloBoton(btnAgregar);
		EstiloUI.aplicarEstiloBoton(btnModificar);
		EstiloUI.aplicarEstiloBoton(btnEliminar);
		EstiloUI.aplicarEstiloBoton(btnActualizar);

		btnAgregar.addActionListener(e -> abrirFormularioAgregar());
		btnModificar.addActionListener(e -> abrirFormularioModificar());
		btnEliminar.addActionListener(e -> accionBajaLogica());
		btnActualizar.addActionListener(e -> ejecutarSwingWorker(this::cargarDatosOriginales, null));

		panelBotones.add(btnAgregar);
		panelBotones.add(btnModificar);
		panelBotones.add(btnEliminar);
		panelBotones.add(btnActualizar);
	}

	private void abrirFormularioAgregar() {
		ProveedorFormularioAgregar formulario = new ProveedorFormularioAgregar(ventana, "Agregar Proveedor",
				proveedorController, this::recargarTabla);
		formulario.setVisible(true);
	}

	private void abrirFormularioModificar() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un proveedor para modificar.");
			return;
		}
		int idProveedor = (int) modeloTabla.getValueAt(fila, 0);
		Proveedor proveedor = listaProveedoresOriginales.stream().filter(p -> p.getIdProveedor() == idProveedor)
				.findFirst().orElse(null);
		if (proveedor == null) {
			JOptionPane.showMessageDialog(ventana, "No se encontró el proveedor seleccionado.");
			return;
		}
		// Aquí reutilizas el formulario para modificar si lo amplías, de momento solo
		// para agregar
		ProveedorFormularioAgregar formulario = new ProveedorFormularioAgregar(ventana, "Modificar Proveedor",
				proveedorController, this::recargarTabla, proveedor 
		);

		formulario.setVisible(true);
	}

	private void accionBajaLogica() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un proveedor para dar de baja.");
			return;
		}
		int idProveedor = (int) modeloTabla.getValueAt(fila, 0);
		String[] opciones = { "Sí", "No" };
		int confirm = JOptionPane.showOptionDialog(ventana, "¿Seguro que deseas dar de baja este proveedor?",
				"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				proveedorController.darBajaLogicaProveedor(idProveedor);
				JOptionPane.showMessageDialog(ventana, "Proveedor dado de baja correctamente.");
				recargarTabla();
			} catch (ControllerException | ServiceException | DAOException ex) {
				JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error al dar de baja",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void recargarTabla() {
		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}

	@Override
	protected Object[][] obtenerDatosFiltrados(String textoFiltro) {
		return null;
	}
}
