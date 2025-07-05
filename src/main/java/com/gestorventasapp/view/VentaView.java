package com.gestorventasapp.view;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.gestorventasapp.controller.VentaController;
import com.gestorventasapp.controller.ClienteController;
import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.util.EstiloUI;

/**
 * Vista principal para la gestión de ventas (buscador, filtro, solo alta y baja
 * lógica).
 */
public class VentaView extends ModuloBaseView {

	private final VentaController ventaController;
	private final ClienteController clienteController;
	private final IvaController ivaController;
	private final ProductoController productoController;
	private JTextField campoBuscador;
	private JComboBox<String> comboEstado;

	private List<Venta> ventasOriginales;

	private static final String[] COLUMNAS = { "ID", "Fecha", "Cliente", "Empleado", "Total Sin IVA", "Total Con IVA",
			"Estado" };

	public VentaView(Usuario usuarioEnSesion, VistaPrincipal vistaPrincipal, VentaController ventaController,
			ClienteController clienteController, IvaController ivaController, ProductoController productoController) {
		super(usuarioEnSesion, vistaPrincipal, "Gestión de Ventas", COLUMNAS);
		this.ventaController = ventaController;
		this.clienteController = clienteController;
		this.ivaController = ivaController;
		this.productoController = productoController;
		inicializarFiltros();
		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}

	/**
	 * Inicializa el panel de filtros: buscador dinámico y filtro por estado.
	 */
	private void inicializarFiltros() {
		campoBuscador = new JTextField(18);
		EstiloUI.aplicarEstiloCampoTexto(campoBuscador);
		panelFiltros.add(new JLabel("Buscar:"));
		panelFiltros.add(campoBuscador);

		comboEstado = new JComboBox<>(new String[] { "Todos", "Activo", "Inactivo" });
		panelFiltros.add(new JLabel("Estado:"));
		panelFiltros.add(comboEstado);

		campoBuscador.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				filtrarYActualizarTabla();
			}

			public void removeUpdate(DocumentEvent e) {
				filtrarYActualizarTabla();
			}

			public void changedUpdate(DocumentEvent e) {
				filtrarYActualizarTabla();
			}
		});
		comboEstado.addActionListener(e -> filtrarYActualizarTabla());
	}

	private void filtrarYActualizarTabla() {
		String texto = campoBuscador.getText().trim().toLowerCase();
		String estadoSeleccionado = (String) comboEstado.getSelectedItem();

		List<Venta> filtradas = ventasOriginales.stream()
				.filter(v -> (texto.isEmpty() || v.getCliente().getRazonSocial().toLowerCase().contains(texto)
						|| v.getEmpleado().getNombre().toLowerCase().contains(texto))
						&& (estadoSeleccionado.equals("Todos")
								|| (estadoSeleccionado.equals("Activo") && v.getEstado() == Estado.activo)
								|| (estadoSeleccionado.equals("Inactivo") && v.getEstado() == Estado.inactivo)))
				.toList();

		Object[][] datos = filtradas.stream().map(this::ventaToRow).toArray(Object[][]::new);
		actualizarTabla(datos);
	}

	@Override
	protected void inicializarBotones() {
		JButton btnAgregar = new JButton("Agregar Venta");
		JButton btnEliminar = new JButton("Eliminar (baja lógica)");
		JButton btnActualizar = new JButton("Actualizar");

		EstiloUI.aplicarEstiloBoton(btnAgregar);
		EstiloUI.aplicarEstiloBoton(btnEliminar);
		EstiloUI.aplicarEstiloBoton(btnActualizar);

		btnAgregar.addActionListener(e -> abrirFormularioAgregar());
		btnEliminar.addActionListener(e -> accionBajaLogica());
		btnActualizar.addActionListener(e -> ejecutarSwingWorker(this::cargarDatosOriginales, null));

		panelBotones.add(btnAgregar);
		panelBotones.add(btnEliminar);
		panelBotones.add(btnActualizar);
	}

	@Override
	protected void cargarDatosOriginales() {
		ventasOriginales = ventaController.listarTodas();
		filtrarYActualizarTabla();
	}

	@Override
	protected Object[][] obtenerDatosFiltrados(String textoFiltro) {
		return new Object[0][];
	}

	/**
	 * Convierte una Venta a una fila para la tabla.
	 */
	private Object[] ventaToRow(Venta v) {
		return new Object[] { v.getIdVenta(), v.getFecha(), v.getCliente().getRazonSocial(),
				v.getEmpleado().getNombre(), v.getTotalSinIva(), v.getTotalConIva(), v.getEstado() };
	}

	/**
	 * Lanza el formulario de alta de venta.
	 */
	private void abrirFormularioAgregar() {
		VentaFormularioAgregar formulario = new VentaFormularioAgregar(ventana, "Agregar Venta", ventaController,
				clienteController, ivaController, productoController, this::recargarTabla, usuarioEnSesion);
		formulario.setVisible(true);
	}

	/**
	 * Elimina (baja lógica) la venta seleccionada.
	 */
	private void accionBajaLogica() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una venta para dar de baja.");
			return;
		}
		int idVenta = (int) modeloTabla.getValueAt(fila, 0);
		String[] opciones = { "Sí", "No" };
		int confirm = JOptionPane.showOptionDialog(ventana, "¿Seguro que deseas dar de baja esta venta?", "Confirmar",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				ventaController.darBajaLogicaVenta(idVenta);
				JOptionPane.showMessageDialog(ventana, "Venta dada de baja correctamente.");
				recargarTabla();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error al dar de baja",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void recargarTabla() {
		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}

	public ProductoController getProductoController() {
		return productoController;
	}
}
