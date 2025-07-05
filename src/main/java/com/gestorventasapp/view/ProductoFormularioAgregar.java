package com.gestorventasapp.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.enums.TipoAnimal;
import com.gestorventasapp.model.Iva;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.util.EstiloUI;

public class ProductoFormularioAgregar extends JDialog {

	// Campos del formulario
	private JTextField campoNombre;
	private JComboBox<TipoAnimal> comboTipoAnimal;
	private JTextField campoMarca;
	private JTextField campoFormato;
	private JTextField campoPrecioVenta;
	private JTextField campoPrecioCompra;
	private JComboBox<Proveedor> comboProveedor;
	private JComboBox<Iva> comboIva;
	private JTextField campoStock;
	private JTextField campoStockMinimo;

	private JButton btnGuardar;
	private JButton btnCancelar;

	private final ProductoController productoController;
	private final Runnable onProductoGuardado; // Callback para refrescar tabla
	private final Producto productoEditando; 
	private final ProveedorController proveedorController;
	private final IvaController ivaController;

	// Listas auxiliares: se rellenan desde el Controller 
	private final List<Proveedor> proveedores;
	private final List<Iva> ivas;

	// --- Constructor para ALTA ---
	public ProductoFormularioAgregar(Window parent, String titulo, ProductoController productoController,
			ProveedorController proveedorController, IvaController ivaController, Runnable onProductoGuardado) {
		this(parent, titulo, productoController, proveedorController, ivaController, onProductoGuardado, null);
	}

	// --- Constructor para MODIFICAR ---
	public ProductoFormularioAgregar(Window parent, String titulo, ProductoController productoController,
			ProveedorController proveedorController, IvaController ivaController, Runnable onProductoGuardado,
			Producto productoEditando) {
		super(parent, titulo, ModalityType.APPLICATION_MODAL);
		this.productoController = productoController;
		this.proveedorController = proveedorController;
		this.ivaController = ivaController;
		this.onProductoGuardado = onProductoGuardado;
		this.productoEditando = productoEditando;

		// Inicialización profesional de las listas auxiliares
		this.proveedores = proveedorController.listarActivos();
		this.ivas = ivaController.listarActivos();

		setSize(550, 550);
		setResizable(false);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		add(crearPanelCentral(), BorderLayout.CENTER);
		add(crearPanelInferior(), BorderLayout.SOUTH);

		if (productoEditando != null) {
			cargarDatosProducto(productoEditando);
		}
	}

	private JPanel crearPanelCentral() {
		JPanel panel = new JPanel(new GridLayout(10, 2, 10, 8));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		panel.setBackground(EstiloUI.getColor("secundario"));

		JLabel lblNombre = new JLabel("Nombre*:");
		campoNombre = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblNombre);
		EstiloUI.aplicarEstiloCampoTexto(campoNombre);

		JLabel lblTipoAnimal = new JLabel("Tipo Animal*:");
		comboTipoAnimal = new JComboBox<>(TipoAnimal.values());
		EstiloUI.aplicarEstiloEtiqueta(lblTipoAnimal);

		JLabel lblMarca = new JLabel("Marca*:");
		campoMarca = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblMarca);
		EstiloUI.aplicarEstiloCampoTexto(campoMarca);

		JLabel lblFormato = new JLabel("Formato*:");
		campoFormato = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblFormato);
		EstiloUI.aplicarEstiloCampoTexto(campoFormato);

		JLabel lblPrecioVenta = new JLabel("Precio Venta*:");
		campoPrecioVenta = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblPrecioVenta);
		EstiloUI.aplicarEstiloCampoTexto(campoPrecioVenta);

		JLabel lblPrecioCompra = new JLabel("Precio Compra*:");
		campoPrecioCompra = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblPrecioCompra);
		EstiloUI.aplicarEstiloCampoTexto(campoPrecioCompra);

		JLabel lblProveedor = new JLabel("Proveedor*:");
		comboProveedor = new JComboBox<>(proveedores.toArray(new Proveedor[0]));
		EstiloUI.aplicarEstiloEtiqueta(lblProveedor);

		JLabel lblIva = new JLabel("IVA*:");
		comboIva = new JComboBox<>(ivas.toArray(new Iva[0]));
		EstiloUI.aplicarEstiloEtiqueta(lblIva);

		JLabel lblStock = new JLabel("Stock*:");
		campoStock = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblStock);
		EstiloUI.aplicarEstiloCampoTexto(campoStock);

		JLabel lblStockMinimo = new JLabel("Stock Mínimo:");
		campoStockMinimo = new JTextField();
		EstiloUI.aplicarEstiloEtiqueta(lblStockMinimo);
		EstiloUI.aplicarEstiloCampoTexto(campoStockMinimo);

		panel.add(lblNombre);
		panel.add(campoNombre);
		panel.add(lblTipoAnimal);
		panel.add(comboTipoAnimal);
		panel.add(lblMarca);
		panel.add(campoMarca);
		panel.add(lblFormato);
		panel.add(campoFormato);
		panel.add(lblPrecioVenta);
		panel.add(campoPrecioVenta);
		panel.add(lblPrecioCompra);
		panel.add(campoPrecioCompra);
		panel.add(lblProveedor);
		panel.add(comboProveedor);
		panel.add(lblIva);
		panel.add(comboIva);
		panel.add(lblStock);
		panel.add(campoStock);
		panel.add(lblStockMinimo);
		panel.add(campoStockMinimo);

		// === CAMBIO REALIZADO: renderers personalizados para los combos ===
		// Combo de proveedor: solo el nombre
		comboProveedor.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Proveedor) {
					setText(((Proveedor) value).getRazonSocial());
				} else {
					setText("");
				}
				return this;
			}
		});
		// Combo de IVA: solo el porcentaje (por ejemplo: 21%)
		comboIva.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Iva) {
					setText(((Iva) value).getPorcentaje().stripTrailingZeros().toPlainString() + "%");
				} else {
					setText("");
				}
				return this;
			}
		});

		return panel;
	}

	private JPanel crearPanelInferior() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(EstiloUI.getColor("secundario"));

		btnGuardar = new JButton(productoEditando == null ? "Guardar" : "Actualizar");
		btnCancelar = new JButton("Cancelar");
		EstiloUI.aplicarEstiloBoton(btnGuardar);
		EstiloUI.aplicarEstiloBoton(btnCancelar);

		btnGuardar.addActionListener(e -> guardarOActualizarProducto());
		btnCancelar.addActionListener(e -> dispose());

		panel.add(btnGuardar);
		panel.add(btnCancelar);
		return panel;
	}

	private void cargarDatosProducto(Producto p) {
		campoNombre.setText(p.getNombre());
		comboTipoAnimal.setSelectedItem(p.getTipoAnimal());
		campoMarca.setText(p.getMarca());
		campoFormato.setText(p.getFormato());
		campoPrecioVenta.setText(p.getPrecioVenta().toPlainString());
		campoPrecioCompra.setText(p.getPrecioCompra().toPlainString());
		comboProveedor.setSelectedItem(p.getProveedor());
		comboIva.setSelectedItem(p.getIva());
		campoStock.setText(p.getStock() != null ? String.valueOf(p.getStock()) : "");
		campoStockMinimo.setText(p.getStockMinimo() != null ? String.valueOf(p.getStockMinimo()) : "");
	}

	private void guardarOActualizarProducto() {
		try {
			// Validaciones
			if (campoNombre.getText().trim().isEmpty() || comboTipoAnimal.getSelectedItem() == null
					|| campoMarca.getText().trim().isEmpty() || campoFormato.getText().trim().isEmpty()
					|| campoPrecioVenta.getText().trim().isEmpty() || campoPrecioCompra.getText().trim().isEmpty()
					|| comboProveedor.getSelectedItem() == null || comboIva.getSelectedItem() == null
					|| campoStock.getText().trim().isEmpty()) {
				throw new IllegalArgumentException("Completa todos los campos obligatorios (*)");
			}

			BigDecimal precioVenta = new BigDecimal(campoPrecioVenta.getText().trim());
			BigDecimal precioCompra = new BigDecimal(campoPrecioCompra.getText().trim());
			int stock = Integer.parseInt(campoStock.getText().trim());
			int stockMinimo = campoStockMinimo.getText().trim().isEmpty() ? 0
					: Integer.parseInt(campoStockMinimo.getText().trim());

			// --- Validación especial: advertencia si venta < compra ---
			if (precioVenta.compareTo(precioCompra) < 0) {
				int opcion = JOptionPane.showConfirmDialog(this,
						"El precio de VENTA es MENOR que el de COMPRA.\n¿Seguro que quieres guardar este producto?",
						"Advertencia: Venta a pérdida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (opcion != JOptionPane.YES_OPTION) {
					return; // Cancela la operación si el usuario NO confirma
				}
			}

			if (productoEditando == null) {
				// --- Alta producto ---
				Producto nuevo = new Producto();
				nuevo.setNombre(campoNombre.getText().trim());
				nuevo.setTipoAnimal((TipoAnimal) comboTipoAnimal.getSelectedItem());
				nuevo.setMarca(campoMarca.getText().trim());
				nuevo.setFormato(campoFormato.getText().trim());
				nuevo.setPrecioVenta(precioVenta);
				nuevo.setPrecioCompra(precioCompra);
				nuevo.setProveedor((Proveedor) comboProveedor.getSelectedItem());
				nuevo.setIva((Iva) comboIva.getSelectedItem());
				nuevo.setStock(stock);
				nuevo.setStockMinimo(stockMinimo);

				productoController.crearProducto(nuevo);
				JOptionPane.showMessageDialog(this, "Producto creado correctamente.");
			} else {
				// --- Modificación producto ---
				productoEditando.setNombre(campoNombre.getText().trim());
				productoEditando.setTipoAnimal((TipoAnimal) comboTipoAnimal.getSelectedItem());
				productoEditando.setMarca(campoMarca.getText().trim());
				productoEditando.setFormato(campoFormato.getText().trim());
				productoEditando.setPrecioVenta(precioVenta);
				productoEditando.setPrecioCompra(precioCompra);
				productoEditando.setProveedor((Proveedor) comboProveedor.getSelectedItem());
				productoEditando.setIva((Iva) comboIva.getSelectedItem());
				productoEditando.setStock(stock);
				productoEditando.setStockMinimo(stockMinimo);

				productoController.modificarProducto(productoEditando);
				JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
			}

			if (onProductoGuardado != null)
				onProductoGuardado.run();
			dispose();

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Los campos de precios y stock deben ser numéricos.", "Datos inválidos",
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Datos inválidos",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en la operación", JOptionPane.ERROR_MESSAGE);
		}
	}

}
