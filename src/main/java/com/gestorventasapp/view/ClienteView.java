package com.gestorventasapp.view;

import com.gestorventasapp.controller.ClienteController;
import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.exceptions.ControllerException;
import com.gestorventasapp.exceptions.ServiceException;
import com.gestorventasapp.exceptions.DAOException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista principal para la gestión de clientes (CRUD + buscador + filtro por
 * estado). Accede únicamente a métodos públicos del ClienteController.
 */
public class ClienteView extends ModuloBaseView {

	private final ClienteController clienteController;
	private List<Cliente> listaClientesOriginales;

	private static final String[] COLUMNAS = { "ID", "Razón Social", "Forma Jurídica", "CIF/NIF", "Localidad",
			"Teléfono", "Email", "Tipo Cliente", "Fecha Alta", "Estado" };

	// Filtro por estado
	private JComboBox<String> comboEstado;

	public ClienteView(Usuario usuarioEnSesion, VistaPrincipal vistaPrincipal, ClienteController clienteController) {
		super(usuarioEnSesion, vistaPrincipal, "Gestión de Clientes", COLUMNAS);
		this.clienteController = clienteController;
		inicializarPanelFiltros();
		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}

	/**
	 * Inicializa el panel de filtros con el buscador y el combo de estado.
	 */
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

			// Eventos para actualizar la tabla al cambiar filtros
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

	/**
	 * Aplica los filtros y actualiza la tabla.
	 */
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

	/**
	 * Filtra clientes por texto y estado.
	 */
	private Object[][] obtenerDatosFiltradosAvanzado(String textoFiltro, String estadoFiltro) {
		return listaClientesOriginales.stream()
				.filter(c -> (c.getRazonSocial().toLowerCase().contains(textoFiltro)
						|| c.getCifNif().toLowerCase().contains(textoFiltro)
						|| c.getEmail().toLowerCase().contains(textoFiltro)
						|| (c.getTelefono() != null && c.getTelefono().contains(textoFiltro))
						|| (c.getLocalidad() != null && c.getLocalidad().toLowerCase().contains(textoFiltro))
						|| (c.getTipoCliente() != null && c.getTipoCliente().toLowerCase().contains(textoFiltro)))
						&& (estadoFiltro.equals("Todos")
								|| (estadoFiltro.equalsIgnoreCase("Activo") && c.getEstado() != null
										&& c.getEstado().name().equalsIgnoreCase("activo"))
								|| (estadoFiltro.equalsIgnoreCase("Inactivo") && c.getEstado() != null
										&& c.getEstado().name().equalsIgnoreCase("inactivo"))))
				.map(this::clienteToRow).toArray(Object[][]::new);
	}

	/**
	 * Carga todos los clientes y refresca la tabla.
	 */
	@Override
	protected void cargarDatosOriginales() {
		try {
			listaClientesOriginales = clienteController.listarTodos();
			filtrarYActualizar();
		} catch (ControllerException | ServiceException | DAOException ex) {
			JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error al cargar clientes",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Implementación por herencia, ya no se usa, pero OBLIGATORIA
	@Override
	protected Object[][] obtenerDatosFiltrados(String textoFiltro) {
		// usa solo el filtro por texto (sin estado)
		return listaClientesOriginales.stream().filter(c -> c.getRazonSocial().toLowerCase().contains(textoFiltro)
				|| c.getCifNif().toLowerCase().contains(textoFiltro) || c.getEmail().toLowerCase().contains(textoFiltro)
				|| (c.getTelefono() != null && c.getTelefono().contains(textoFiltro))
				|| (c.getLocalidad() != null && c.getLocalidad().toLowerCase().contains(textoFiltro))
				|| (c.getTipoCliente() != null && c.getTipoCliente().toLowerCase().contains(textoFiltro)))
				.map(this::clienteToRow).toArray(Object[][]::new);
	}

	private Object[] clienteToRow(Cliente c) {
		return new Object[] { c.getIdCliente(), c.getRazonSocial(),
				c.getFormaJuridica() != null ? c.getFormaJuridica().name() : "", c.getCifNif(), c.getLocalidad(),
				c.getTelefono(), c.getEmail(), c.getTipoCliente(),
				c.getFechaAlta() != null ? c.getFechaAlta().toLocalDate().toString() : "",
				c.getEstado() != null ? c.getEstado().name() : "" };
	}

	private void abrirFormularioAgregar() {
		ClienteFormularioAgregar formulario = new ClienteFormularioAgregar(ventana, "Agregar Cliente",
				clienteController, this::recargarTabla);
		formulario.setVisible(true);
	}

	/**
	 * Lógica para dar de baja lógica (estado inactivo) a un cliente seleccionado.
	 */
	private void accionBajaLogica() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un cliente para dar de baja.");
			return;
		}
		int idCliente = (int) modeloTabla.getValueAt(fila, 0);
		String[] opciones = { "Sí", "No" };
		int confirm = JOptionPane.showOptionDialog(ventana, "¿Seguro que deseas dar de baja este cliente?", "Confirmar",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				clienteController.darBajaLogicaCliente(idCliente);
				JOptionPane.showMessageDialog(ventana, "Cliente dado de baja correctamente.");
				recargarTabla();
			} catch (ControllerException | ServiceException | DAOException ex) {
				JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error al dar de baja",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * NUEVO: Botón Modificar Cliente (abre formulario de modificación).
	 */
	private void abrirFormularioModificar() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un cliente para modificar.");
			return;
		}
		int idCliente = (int) modeloTabla.getValueAt(fila, 0);
		Cliente cliente = listaClientesOriginales.stream().filter(c -> c.getIdCliente() == idCliente).findFirst()
				.orElse(null);
		if (cliente == null) {
			JOptionPane.showMessageDialog(ventana, "No se encontró el cliente seleccionado.");
			return;
		}
		ClienteFormularioAgregar formulario = new ClienteFormularioAgregar(ventana, "Modificar Cliente",
				clienteController, this::recargarTabla, cliente);
		formulario.setVisible(true);
	}

	/**
	 * Inicializa los botones de la vista y les asigna el estilo y las acciones.
	 */
	@Override
	protected void inicializarBotones() {
		JButton btnAgregar = new JButton("Agregar Cliente");
		JButton btnModificar = new JButton("Modificar Cliente");
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

	private void recargarTabla() {
		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}
}
