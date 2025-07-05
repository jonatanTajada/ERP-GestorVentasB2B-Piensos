package com.gestorventasapp.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import com.gestorventasapp.controller.VentaController;
import com.gestorventasapp.controller.ClienteController;
import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.util.EstiloUI;

/**
 * Formulario profesional para alta de ventas con detalle de líneas.
 */
public class VentaFormularioAgregar extends JDialog {

	private final VentaController ventaController;
	private final ClienteController clienteController;
	private final IvaController ivaController;
	private final ProductoController productoController;
	private final Runnable onVentaGuardada;
	private final Usuario usuarioEnSesion;

	private JComboBox<Cliente> comboCliente;
	private JTextField campoFecha;
	private JTextField campoEmpleado;
	private JTextField campoTotalSinIva;
	private JTextField campoTotalConIva;
	private JTable tablaDetalles;
	private DefaultTableModel modeloTabla;
	private List<DetalleVenta> detallesVenta = new ArrayList<>();

	private JComboBox<Producto> comboProducto;
	private JTextField campoCantidad;
	private JTextField campoPrecioUnitario;
	private JComboBox<String> comboIva;

	public VentaFormularioAgregar(Window parent, String titulo, VentaController ventaController,
			ClienteController clienteController, IvaController ivaController, ProductoController productoController,
			Runnable onVentaGuardada, Usuario usuarioEnSesion) {
		super(parent, titulo, ModalityType.APPLICATION_MODAL);
		this.ventaController = ventaController;
		this.clienteController = clienteController;
		this.ivaController = ivaController;
		this.productoController = productoController;
		this.onVentaGuardada = onVentaGuardada;
		this.usuarioEnSesion = usuarioEnSesion;

		setSize(950, 700);
		setResizable(false);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		add(crearPanelCabecera(), BorderLayout.NORTH);
		add(crearPanelDetalles(), BorderLayout.CENTER);
		add(crearPanelInferior(), BorderLayout.SOUTH);

		recalcularTotales();
	}

	/**
	 * Cabecera con cliente, fecha, empleado y totales.
	 */
	private JPanel crearPanelCabecera() {
		JPanel panel = new JPanel(new GridLayout(3, 4, 16, 12));
		panel.setBorder(BorderFactory.createEmptyBorder(18, 26, 8, 26));
		panel.setBackground(EstiloUI.getColor("secundario"));

		// --- Combo de Clientes ---
		comboCliente = new JComboBox<>(clienteController.listarActivos().toArray(new Cliente[0]));
		comboCliente.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Cliente) {
					setText(((Cliente) value).getRazonSocial());
				} else {
					setText("");
				}
				return this;
			}
		});

		// Fecha (auto)
		campoFecha = new JTextField(LocalDateTime.now().toString().substring(0, 16));
		campoFecha.setEditable(false);

		// Empleado (usuario en sesión)
		campoEmpleado = new JTextField(usuarioEnSesion.getNombreUsuario());
		campoEmpleado.setEditable(false);

		// Totales
		campoTotalSinIva = new JTextField("0.00");
		campoTotalSinIva.setEditable(false);
		campoTotalConIva = new JTextField("0.00");
		campoTotalConIva.setEditable(false);

		panel.add(new JLabel("Cliente*:"));
		panel.add(comboCliente);
		panel.add(new JLabel("Fecha:"));
		panel.add(campoFecha);
		panel.add(new JLabel("Empleado:"));
		panel.add(campoEmpleado);
		panel.add(new JLabel("Total sin IVA:"));
		panel.add(campoTotalSinIva);
		panel.add(new JLabel("Total con IVA:"));
		panel.add(campoTotalConIva);

		// Relleno para ocupar las 3 filas
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));

		return panel;
	}

	/**
	 * Panel central para gestión de detalles de la venta (líneas de producto).
	 */
	private JPanel crearPanelDetalles() {
		JPanel panel = new JPanel(new BorderLayout(10, 6));
		panel.setBackground(EstiloUI.getColor("secundario"));

		// Tabla de detalles de venta
		String[] cols = { "Producto", "Formato", "Cantidad", "Precio Unit.", "IVA (%)", "Subtotal", "Total IVA" };
		modeloTabla = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaDetalles = new JTable(modeloTabla);
		tablaDetalles.setRowHeight(28);
		JScrollPane scroll = new JScrollPane(tablaDetalles);

		// Panel superior para añadir línea
		JPanel panelLinea = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));

		comboProducto = new JComboBox<>(productoController.listarActivos().toArray(new Producto[0]));
		comboProducto.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Producto) {
					setText(((Producto) value).getNombre());
				} else {
					setText("");
				}
				return this;
			}
		});

		// Campos de cantidad, precio e IVA
		campoCantidad = new JTextField(5);
		campoPrecioUnitario = new JTextField(8);
		comboIva = new JComboBox<>(new String[] { "21", "10" });

		// Cuando cambias el producto, autocompleta el precio unitario desde la BD
		comboProducto.addActionListener(e -> {
			Producto prod = (Producto) comboProducto.getSelectedItem();
			if (prod != null) {
				campoPrecioUnitario.setText(prod.getPrecioVenta() != null ? prod.getPrecioVenta().toPlainString() : "");
				// IVA autocompletado si el producto tiene iva asociado
				if (prod.getIva() != null && prod.getIva().getPorcentaje() != null) {
					comboIva.setSelectedItem(prod.getIva().getPorcentaje().stripTrailingZeros().toPlainString());
				}
			} else {
				campoPrecioUnitario.setText("");
				comboIva.setSelectedIndex(0);
			}
		});

		// --- Botón añadir línea ---
		JButton btnAgregarLinea = new JButton("Añadir línea");
		EstiloUI.aplicarEstiloBoton(btnAgregarLinea);

		btnAgregarLinea.addActionListener(e -> {
			Producto prod = (Producto) comboProducto.getSelectedItem();
			int cantidad;
			BigDecimal precioUnit;
			try {
				cantidad = Integer.parseInt(campoCantidad.getText().trim());
				precioUnit = new BigDecimal(campoPrecioUnitario.getText().trim());
				if (prod == null || cantidad <= 0 || precioUnit.compareTo(BigDecimal.ZERO) < 0)
					throw new Exception();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Introduce producto, cantidad y precio válidos.");
				return;
			}
			// IVA
			BigDecimal ivaPct = new BigDecimal(comboIva.getSelectedItem().toString());
			BigDecimal subtotal = precioUnit.multiply(BigDecimal.valueOf(cantidad));
			BigDecimal totalIva = subtotal.multiply(ivaPct).divide(BigDecimal.valueOf(100)).add(subtotal);

			modeloTabla.addRow(new Object[] { prod.getNombre(), prod.getFormato(), cantidad, precioUnit, ivaPct + "%",
					subtotal, totalIva });

			DetalleVenta det = DetalleVenta.builder().producto(prod).cantidad(cantidad).precioUnitario(precioUnit)
					.porcentajeIva(ivaPct).subtotalSinIva(subtotal).subtotalConIva(totalIva).estado(Estado.activo)
					.build();
			detallesVenta.add(det);

			campoCantidad.setText("");
			campoPrecioUnitario.setText(prod.getPrecioVenta() != null ? prod.getPrecioVenta().toPlainString() : "");
			if (prod.getIva() != null && prod.getIva().getPorcentaje() != null) {
				comboIva.setSelectedItem(prod.getIva().getPorcentaje().stripTrailingZeros().toPlainString());
			} else {
				comboIva.setSelectedIndex(0);
			}
			recalcularTotales();
		});

		// --- Botón eliminar línea ---
		JButton btnEliminarLinea = new JButton("Eliminar línea");
		EstiloUI.aplicarEstiloBoton(btnEliminarLinea);
		btnEliminarLinea.addActionListener(e -> {
			int fila = tablaDetalles.getSelectedRow();
			if (fila >= 0) {
				modeloTabla.removeRow(fila);
				detallesVenta.remove(fila);
				recalcularTotales();
			}
		});

		// Añadir todos los campos al panel superior
		panelLinea.add(new JLabel("Producto:"));
		panelLinea.add(comboProducto);
		panelLinea.add(new JLabel("Cantidad:"));
		panelLinea.add(campoCantidad);
		panelLinea.add(new JLabel("Precio Unit:"));
		panelLinea.add(campoPrecioUnitario);
		panelLinea.add(new JLabel("IVA:"));
		panelLinea.add(comboIva);
		panelLinea.add(btnAgregarLinea);
		panelLinea.add(btnEliminarLinea);

		panel.add(panelLinea, BorderLayout.NORTH);
		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Panel de botones inferiores (guardar/cancelar)
	 */
	private JPanel crearPanelInferior() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setBackground(EstiloUI.getColor("secundario"));
		JButton btnGuardar = new JButton("Guardar Venta");
		JButton btnCancelar = new JButton("Cancelar");
		EstiloUI.aplicarEstiloBoton(btnGuardar);
		EstiloUI.aplicarEstiloBoton(btnCancelar);

		btnGuardar.addActionListener(this::guardarVenta);
		btnCancelar.addActionListener(e -> dispose());
		panel.add(btnGuardar);
		panel.add(btnCancelar);
		return panel;
	}

	/**
	 * Recalcula los totales al añadir/eliminar líneas
	 */
	private void recalcularTotales() {
		BigDecimal totalSinIva = BigDecimal.ZERO;
		BigDecimal totalConIva = BigDecimal.ZERO;
		for (DetalleVenta d : detallesVenta) {
			totalSinIva = totalSinIva.add(d.getSubtotalSinIva());
			totalConIva = totalConIva.add(d.getSubtotalConIva());
		}
		campoTotalSinIva.setText(totalSinIva.toPlainString());
		campoTotalConIva.setText(totalConIva.toPlainString());
	}

	/**
	 * Guarda la venta con sus detalles (alta)
	 */
	private void guardarVenta(ActionEvent e) {
		try {
			Cliente cliente = (Cliente) comboCliente.getSelectedItem();
			if (cliente == null || detallesVenta.isEmpty())
				throw new IllegalArgumentException("Selecciona cliente y añade al menos una línea de venta.");

			Empleado empleado = usuarioEnSesion.getEmpleado();
			if (empleado == null) {
				JOptionPane.showMessageDialog(this, "El usuario actual no tiene un empleado asociado.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			Venta venta = new Venta();
			venta.setCliente(cliente);
			venta.setEmpleado(empleado);
			venta.setFecha(LocalDateTime.now());
			venta.setTotalSinIva(new BigDecimal(campoTotalSinIva.getText()));
			venta.setTotalConIva(new BigDecimal(campoTotalConIva.getText()));
			venta.setEstado(Estado.activo);

			ventaController.crearVentaConDetalles(venta, detallesVenta);

			JOptionPane.showMessageDialog(this, "Venta guardada correctamente.");

			if (onVentaGuardada != null)
				onVentaGuardada.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error al guardar venta",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
