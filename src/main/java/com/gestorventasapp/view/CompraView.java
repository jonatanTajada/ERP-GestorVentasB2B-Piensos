package com.gestorventasapp.view;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.gestorventasapp.controller.CompraController;
import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.util.EstiloUI;

/**
 * Vista principal para la gestión de compras (buscador, filtro, solo alta y
 * baja lógica).
 */
public class CompraView extends ModuloBaseView {

	private final CompraController compraController;
	private final ProveedorController proveedorController;
	private final IvaController ivaController;
	private final ProductoController productoController;
	private JTextField campoBuscador;
	private JComboBox<String> comboEstado;

	private List<Compra> comprasOriginales;

	private static final String[] COLUMNAS = { "ID", "Fecha", "Proveedor", "Empleado", "Total Sin IVA", "Total Con IVA",
			"Estado" };

	public CompraView(Usuario usuarioEnSesion, VistaPrincipal vistaPrincipal, CompraController compraController,
			ProveedorController proveedorController, IvaController ivaController,
			ProductoController productoController) {
		super(usuarioEnSesion, vistaPrincipal, "Gestión de Compras", COLUMNAS);
		this.compraController = compraController;
		this.proveedorController = proveedorController;
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

		List<Compra> filtradas = comprasOriginales.stream()
				.filter(c -> (texto.isEmpty() || c.getProveedor().getRazonSocial().toLowerCase().contains(texto)
						|| c.getEmpleado().getNombre().toLowerCase().contains(texto))
						&& (estadoSeleccionado.equals("Todos")
								|| (estadoSeleccionado.equals("Activo") && c.getEstado() == Estado.activo)
								|| (estadoSeleccionado.equals("Inactivo") && c.getEstado() == Estado.inactivo)))
				.toList();

		Object[][] datos = filtradas.stream().map(this::compraToRow).toArray(Object[][]::new);
		actualizarTabla(datos);
	}

	/**
	 * Inicializa los botones principales de la vista (Agregar, Eliminar,
	 * Actualizar) y les asigna sus acciones.
	 */
	@Override
	protected void inicializarBotones() {
		JButton btnAgregar = new JButton("Agregar Compra");
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

	/**
	 * Carga la lista original de compras desde el controlador y aplica el filtro
	 * actual. Actualiza la tabla en pantalla con los datos filtrados.
	 */
	@Override
	protected void cargarDatosOriginales() {
		comprasOriginales = compraController.listarTodas();
		filtrarYActualizarTabla();
	}

	/**
	 * Obtiene los datos de compras filtrados por el texto del buscador y el estado
	 * seleccionado. Ya no se usa directamente porque ahora el filtrado se realiza
	 * en filtrarYActualizarTabla().
	 */

	@Override
	protected Object[][] obtenerDatosFiltrados(String textoFiltro) {
		JComboBox<String> comboEstado = (JComboBox<String>) panelFiltros.getClientProperty("comboEstado");
		String estadoSeleccionado = comboEstado != null ? (String) comboEstado.getSelectedItem() : "Todos";

		final String texto = textoFiltro.toLowerCase();

		return comprasOriginales.stream().filter(c -> {
			boolean filtroTexto = texto.isEmpty() || c.getProveedor().getRazonSocial().toLowerCase().contains(texto)
					|| c.getEmpleado().getNombre().toLowerCase().contains(texto);
			boolean filtroEstado = estadoSeleccionado.equals("Todos")
					|| (estadoSeleccionado.equals("Activo") && c.getEstado() == Estado.activo)
					|| (estadoSeleccionado.equals("Inactivo") && c.getEstado() == Estado.inactivo);
			return filtroTexto && filtroEstado;
		}).map(this::compraToRow).toArray(Object[][]::new);
	}

	/**
	 * Convierte una Compra a una fila para la tabla.
	 */
	private Object[] compraToRow(Compra c) {
		return new Object[] { c.getIdCompra(), c.getFecha(), c.getProveedor().getRazonSocial(),
				c.getEmpleado().getNombre(), c.getTotalSinIva(), c.getTotalConIva(), c.getEstado() };
	}

	// ---- Acciones de los botones ----

	/**
	 * Lanza el formulario de alta de compra.
	 */
	private void abrirFormularioAgregar() {
		CompraFormularioAgregar formulario = new CompraFormularioAgregar(ventana, "Agregar Compra", compraController,
				proveedorController, ivaController, productoController, 
				this::recargarTabla, usuarioEnSesion);
		formulario.setVisible(true);
	}

	/**
	 * Elimina (baja lógica) la compra seleccionada.
	 */
	private void accionBajaLogica() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una compra para dar de baja.");
			return;
		}
		int idCompra = (int) modeloTabla.getValueAt(fila, 0);
		String[] opciones = {"Sí", "No"};
		int confirm = JOptionPane.showOptionDialog(
		    ventana,
		    "¿Seguro que deseas dar de baja esta compra?",
		    "Confirmar",
		    JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    opciones,
		    opciones[0]
		);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				compraController.darBajaLogicaCompra(idCompra);
				JOptionPane.showMessageDialog(ventana, "Compra dada de baja correctamente.");
				recargarTabla();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error al dar de baja",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Refresca la tabla tras cambios.
	 */
	private void recargarTabla() {
		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}

	public ProductoController getProductoController() {
		return productoController;
	}

}
