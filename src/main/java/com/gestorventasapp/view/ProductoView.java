package com.gestorventasapp.view;

import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.model.Iva;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.exceptions.ControllerException;
import com.gestorventasapp.exceptions.ServiceException;
import com.gestorventasapp.exceptions.DAOException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista principal para la gestión de productos (CRUD + buscador + filtro por
 * estado). Accede únicamente a métodos públicos del ProductoController.
 */
public class ProductoView extends ModuloBaseView {

	private final ProductoController productoController;
	private List<Producto> productosOriginales;
	private final ProveedorController proveedorController;
	private final IvaController ivaController;

	// --- NUEVO: elementos de filtrado
	private JTextField campoBuscador; // Campo de búsqueda
	private JComboBox<String> comboEstado; // Combo para filtrar por estado

	private static final String[] COLUMNAS = { "ID", "Nombre", "Tipo Animal", "Marca", "Formato", "Precio Venta",
			"Precio Compra", "Proveedor", "IVA", "Stock", "Stock Mínimo", "Estado" };

	public ProductoView(Usuario usuarioEnSesion, VistaPrincipal vistaPrincipal, ProductoController productoController,
			ProveedorController proveedorController, IvaController ivaController) {
		super(usuarioEnSesion, vistaPrincipal, "Gestión de Productos", COLUMNAS);
		this.productoController = productoController;
		this.proveedorController = proveedorController;
		this.ivaController = ivaController;

		// --- INICIO: Añadir buscador y combo de estado al panel de filtros ---
		campoBuscador = new JTextField(18);
		EstiloUI.aplicarEstiloCampoTexto(campoBuscador);
		panelFiltros.add(new JLabel("Buscar:"));
		panelFiltros.add(campoBuscador);

		comboEstado = new JComboBox<>(new String[] { "Todos", "Activos", "Inactivos" });
		panelFiltros.add(new JLabel("Estado:"));
		panelFiltros.add(comboEstado);

		// Listeners para filtrar la tabla
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

		ejecutarSwingWorker(this::cargarDatosOriginales, null);
	}

	@Override
	protected void inicializarBotones() {
		JButton btnAgregar = new JButton("Agregar Producto");
		JButton btnModificar = new JButton("Modificar Producto");
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

	@Override
	protected void cargarDatosOriginales() {
		try {
			productosOriginales = productoController.listarTodos();
			filtrarYActualizarTabla(); // Actualiza tabla con filtro aplicado
		} catch (ControllerException | ServiceException | DAOException ex) {
			JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error al cargar productos",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	protected Object[][] obtenerDatosFiltrados(String textoFiltro) {
		return new Object[0][];
	}

	/**
	 * Método para filtrar por buscador y estado y refrescar la tabla. --- AÑADIDO
	 * PARA MEJOR USABILIDAD ---
	 */
	private void filtrarYActualizarTabla() {
		String texto = campoBuscador.getText().trim().toLowerCase();
		String estadoSeleccionado = (String) comboEstado.getSelectedItem();

		List<Producto> filtrados = productosOriginales.stream()
				.filter(p -> (texto.isEmpty() || p.getNombre().toLowerCase().contains(texto)
						|| p.getMarca().toLowerCase().contains(texto) || p.getFormato().toLowerCase().contains(texto))
						&& (estadoSeleccionado.equals("Todos")
								|| (estadoSeleccionado.equals("Activos") && p.getEstado() == Estado.activo)
								|| (estadoSeleccionado.equals("Inactivos") && p.getEstado() == Estado.inactivo)))
				.collect(Collectors.toList());

		Object[][] datos = filtrados.stream().map(this::productoToRow).toArray(Object[][]::new);
		actualizarTabla(datos);
	}

	private Object[] productoToRow(Producto p) {
		// Mostramos solo el porcentaje de IVA
		String ivaStr = "";
		if (p.getIva() != null) {
			ivaStr = p.getIva().getPorcentaje().stripTrailingZeros().toPlainString() + "%";
		}

		return new Object[] { p.getIdProducto(), p.getNombre(),
				p.getTipoAnimal() != null ? p.getTipoAnimal().getLabel() : "", p.getMarca(), p.getFormato(),
				p.getPrecioVenta(), p.getPrecioCompra(),
				p.getProveedor() != null ? p.getProveedor().getRazonSocial() : "", ivaStr, p.getStock(),
				p.getStockMinimo(), p.getEstado() != null ? p.getEstado().name() : "" };
	}

	private void abrirFormularioAgregar() {
		ProductoFormularioAgregar formulario = new ProductoFormularioAgregar(ventana, "Agregar Producto",
				productoController, proveedorController, ivaController, this::recargarTabla);
		formulario.setVisible(true);
	}

	private void abrirFormularioModificar() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un producto para modificar.");
			return;
		}
		int idProducto = (int) modeloTabla.getValueAt(fila, 0);
		Producto producto = productosOriginales.stream().filter(p -> p.getIdProducto() == idProducto).findFirst()
				.orElse(null);
		if (producto == null) {
			JOptionPane.showMessageDialog(ventana, "No se encontró el producto seleccionado.");
			return;
		}

		ProductoFormularioAgregar formulario = new ProductoFormularioAgregar(ventana, "Modificar Producto",
				productoController, proveedorController, ivaController, this::recargarTabla, producto);
		formulario.setVisible(true);
	}

	private void accionBajaLogica() {
		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un producto para dar de baja.");
			return;
		}
		int idProducto = (int) modeloTabla.getValueAt(fila, 0);
		String[] opciones = { "Sí", "No" };
		int confirm = JOptionPane.showOptionDialog(ventana, "¿Seguro que deseas dar de baja este producto?",
				"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				productoController.darBajaLogicaProducto(idProducto);
				JOptionPane.showMessageDialog(ventana, "Producto dado de baja correctamente.");
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
}
