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

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.gestorventasapp.controller.CompraController;
import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.util.EstiloUI;

/**
 * Formulario profesional para alta de compras con detalle de líneas.
 */
public class CompraFormularioAgregar extends JDialog {

    private final CompraController compraController;
    private final ProveedorController proveedorController;
    private final IvaController ivaController;
    private final ProductoController productoController;
    private final Runnable onCompraGuardada;
    private final Usuario usuarioEnSesion;

    private JComboBox<Proveedor> comboProveedor;
    private JTextField campoFecha;
    private JTextField campoEmpleado;
    private JTextField campoTotalSinIva;
    private JTextField campoTotalConIva;
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    private List<DetalleCompra> detallesCompra = new ArrayList<>();


    // --- NUEVO: comboProducto como atributo para actualizar dinámicamente ---
    private JComboBox<Producto> comboProducto;
    private JTextField campoCantidad;
    private JTextField campoPrecioUnitario;
    private JComboBox<String> comboIva;

    public CompraFormularioAgregar(Window parent, String titulo, CompraController compraController,
                                   ProveedorController proveedorController, IvaController ivaController,
                                   ProductoController productoController,
                                   Runnable onCompraGuardada, Usuario usuarioEnSesion) {
        super(parent, titulo, ModalityType.APPLICATION_MODAL);
        this.compraController = compraController;
        this.proveedorController = proveedorController;
        this.ivaController = ivaController;
        this.productoController = productoController;
        this.onCompraGuardada = onCompraGuardada;
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
     * Cabecera con proveedor, fecha, empleado y totales.
     */
    private JPanel crearPanelCabecera() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 16, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 26, 8, 26));
        panel.setBackground(EstiloUI.getColor("secundario"));

        // --- Combo de Proveedores con renderer ---
        comboProveedor = new JComboBox<>(proveedorController.listarActivos().toArray(new Proveedor[0]));
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

        // Al cambiar de proveedor, actualiza productos del combo de productos
        comboProveedor.addActionListener(e -> recargarProductosProveedor());

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

        panel.add(new JLabel("Proveedor*:"));
        panel.add(comboProveedor);
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
     * Panel central para gestión de detalles de la compra (líneas de producto).
     * Incluye: selección de producto del proveedor, cantidad, precio autocompletado, IVA y tabla resumen.
     */
    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout(10, 6));
        panel.setBackground(EstiloUI.getColor("secundario"));

        // --- Tabla de detalles de compra ---
        String[] cols = { "Producto", "Formato", "Cantidad", "Precio Unit.", "IVA (%)", "Subtotal", "Total IVA" };
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tablaDetalles);

        // --- Panel superior para añadir línea ---
        JPanel panelLinea = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));

        // === Combo de productos dependiente del proveedor ===
        comboProducto = new JComboBox<>();
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

        // === Campos de cantidad, precio e IVA ===
        campoCantidad = new JTextField(5);
        campoPrecioUnitario = new JTextField(8);
        comboIva = new JComboBox<>(new String[] { "21", "10" });

        // === Carga productos al cambiar proveedor ===
        comboProveedor.addActionListener(e -> recargarProductosProveedor());

        // === Cuando cambias el producto, autocompleta el precio unitario desde la BD ===
        comboProducto.addActionListener(e -> {
            Producto prod = (Producto) comboProducto.getSelectedItem();
            if (prod != null) {
                // Precio compra autocompletado (editable por el usuario)
                campoPrecioUnitario.setText(prod.getPrecioCompra() != null ? prod.getPrecioCompra().toPlainString() : "");
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

            // Añadir fila visual
            modeloTabla.addRow(new Object[] { prod.getNombre(), prod.getFormato(), cantidad, precioUnit, ivaPct + "%",
                    subtotal, totalIva });

            // Añadir a la lista lógica (para BD)
            DetalleCompra det = DetalleCompra.builder().producto(prod).cantidad(cantidad).precioUnitario(precioUnit)
                    .porcentajeIva(ivaPct).subtotalSinIva(subtotal).subtotalConIva(totalIva).estado(Estado.activo)
                    .build();
            detallesCompra.add(det);

            // Limpia campos (excepto proveedor)
            campoCantidad.setText("");
            campoPrecioUnitario.setText(prod.getPrecioCompra() != null ? prod.getPrecioCompra().toPlainString() : "");
            // IVA vuelve al valor del producto seleccionado (o por defecto)
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
                detallesCompra.remove(fila);
                recalcularTotales();
            }
        });

        // --- Añadir todos los campos al panel superior ---
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

        // === AL FINAL: recarga los productos para el proveedor seleccionado al abrir el formulario ===
        recargarProductosProveedor();

        return panel;
    }


    /**
     * Recarga el combo de productos según el proveedor seleccionado
     */
    private void recargarProductosProveedor() {
        Proveedor proveedor = (Proveedor) comboProveedor.getSelectedItem();
        comboProducto.removeAllItems();
        if (proveedor != null) {
            List<Producto> productosProveedor = productoController.buscarPorProveedor(proveedor.getIdProveedor());
            for (Producto p : productosProveedor) {
                if (p.getEstado() == Estado.activo) {
                    comboProducto.addItem(p);
                }
            }
        }
        // Limpia los campos cuando cambias de proveedor
        campoPrecioUnitario.setText("");
        comboIva.setSelectedIndex(0);
    }



    /**
     * Panel de botones inferiores (guardar/cancelar)
     */
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(EstiloUI.getColor("secundario"));
        JButton btnGuardar = new JButton("Guardar Compra");
        JButton btnCancelar = new JButton("Cancelar");
        EstiloUI.aplicarEstiloBoton(btnGuardar);
        EstiloUI.aplicarEstiloBoton(btnCancelar);

        btnGuardar.addActionListener(this::guardarCompra);
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
        for (DetalleCompra d : detallesCompra) {
            totalSinIva = totalSinIva.add(d.getSubtotalSinIva());
            totalConIva = totalConIva.add(d.getSubtotalConIva());
        }
        campoTotalSinIva.setText(totalSinIva.toPlainString());
        campoTotalConIva.setText(totalConIva.toPlainString());
    }

    /**
     * Guarda la compra con sus detalles (alta)
     */
    private void guardarCompra(ActionEvent e) {
        try {
            Proveedor proveedor = (Proveedor) comboProveedor.getSelectedItem();
            if (proveedor == null || detallesCompra.isEmpty())
                throw new IllegalArgumentException("Selecciona proveedor y añade al menos una línea de compra.");

            // --- ¡VERIFICA empleado! ---
            Empleado empleado = usuarioEnSesion.getEmpleado();
            if (empleado == null) {
                JOptionPane.showMessageDialog(this, "El usuario actual no tiene un empleado asociado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Compra compra = new Compra();
            compra.setProveedor(proveedor);
            compra.setEmpleado(empleado); 
            compra.setFecha(LocalDateTime.now());
            compra.setTotalSinIva(new BigDecimal(campoTotalSinIva.getText()));
            compra.setTotalConIva(new BigDecimal(campoTotalConIva.getText()));
            compra.setEstado(Estado.activo);

            compraController.crearCompraConDetalles(compra, detallesCompra);

            JOptionPane.showMessageDialog(this, "Compra guardada correctamente.");

            if (onCompraGuardada != null)
                onCompraGuardada.run();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error al guardar compra",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
