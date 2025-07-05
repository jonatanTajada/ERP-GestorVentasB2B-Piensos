package com.gestorventasapp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.gestorventasapp.controller.ClienteController;
import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.controller.VentaController;
import com.gestorventasapp.controller.CompraController; // <-- NUEVO: Importa el controller de compras
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.util.FooterPanel;

public class VistaPrincipal {

	private final IvaController ivaController;
	private final ClienteController clienteController;
	private final ProveedorController proveedorController;
	private final ProductoController productoController;
	private final CompraController compraController;
	private final VentaController ventaController;

	private final JFrame ventana;
	private final Usuario usuarioEnSesion;

	public VistaPrincipal(Usuario usuarioEnSesion, ClienteController clienteController,
			ProveedorController proveedorController, ProductoController productoController, IvaController ivaController,
			CompraController compraController, VentaController ventaController) {
		this.usuarioEnSesion = usuarioEnSesion;
		this.clienteController = clienteController;
		this.proveedorController = proveedorController;
		this.productoController = productoController;
		this.ivaController = ivaController;
		this.compraController = compraController;
		this.ventaController = ventaController;

		ventana = new JFrame("Gestión de Negocio B2B - Distribuidora de Piensos JonatanTR");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setMinimumSize(new Dimension(800, 580));
		ventana.setPreferredSize(new Dimension(1100, 700));
		ventana.setLayout(new BorderLayout());

		// Encabezado
		JPanel encabezado = crearEncabezadoDashboard();
		ventana.add(encabezado, BorderLayout.NORTH);

		// Menú superior
		ventana.setJMenuBar(EstiloUI.crearBarraMenu(ventana, this));

		// Panel central: acceso a módulos (Cards)
		JPanel panelCentral = crearPanelCentral();
		ventana.add(panelCentral, BorderLayout.CENTER);

		// Pie de página profesional
		ventana.add(new FooterPanel(), BorderLayout.SOUTH);

		ventana.setLocationRelativeTo(null);
		ventana.pack();
		ventana.setVisible(true);
	}

	private JPanel crearEncabezadoDashboard() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(EstiloUI.getColor("primario"));

		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
		panelInfo.setOpaque(false);

		JLabel lblUsuario = new JLabel("Usuario: " + usuarioEnSesion.getNombreUsuario());
		lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblUsuario.setForeground(EstiloUI.getColor("texto"));
		lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblFechaHora = new JLabel();
		lblFechaHora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblFechaHora.setForeground(EstiloUI.getColor("texto"));
		lblFechaHora.setAlignmentX(Component.LEFT_ALIGNMENT);

		Timer timer = new Timer(1000, e -> lblFechaHora.setText(fechaHoraActual()));
		timer.start();
		lblFechaHora.setText(fechaHoraActual());

		panelInfo.add(Box.createVerticalStrut(7));
		panelInfo.add(lblUsuario);
		panelInfo.add(lblFechaHora);

		JLabel lblTitulo = new JLabel("Gestor de Ventas y Compras B2B – Distribuidora de Piensos JonatanTR");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitulo.setForeground(EstiloUI.getColor("texto"));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblLogo = new JLabel();
		URL logoUrl = EstiloUI.class.getResource("/logo.jpg");
		if (logoUrl != null) {
			ImageIcon logoIcon = new ImageIcon(
					new ImageIcon(logoUrl).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
			lblLogo.setIcon(logoIcon);
		}
		lblLogo.setHorizontalAlignment(SwingConstants.RIGHT);

		panel.add(panelInfo, BorderLayout.WEST);
		panel.add(lblTitulo, BorderLayout.CENTER);
		panel.add(lblLogo, BorderLayout.EAST);

		return panel;
	}

	private JPanel crearPanelCentral() {
		JPanel panelCentral = new JPanel(new GridLayout(2, 3, 30, 30));
		panelCentral.setBackground(EstiloUI.getColor("secundario"));
		panelCentral.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		panelCentral.add(crearCardModulo("Clientes", "clientes.png", this::abrirClientes));
		panelCentral.add(crearCardModulo("Ventas", "ventas.png", this::abrirVentas));
		panelCentral.add(crearCardModulo("Productos", "producto.png", this::abrirProductos));
		panelCentral.add(crearCardModulo("Compras", "compras.png", this::abrirCompras)); // Ahora sí
		panelCentral.add(crearCardModulo("Proveedores", "proveedor.png", this::abrirProveedores));

		return panelCentral;
	}

	private JPanel crearCardModulo(String texto, String iconoFile, Runnable onClick) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(EstiloUI.getColor("primario"), 2, true),
				BorderFactory.createEmptyBorder(26, 18, 26, 18)));

		JLabel icono = new JLabel();
		URL imgUrl = getClass().getClassLoader().getResource("img/" + iconoFile);
		if (imgUrl != null) {
			ImageIcon icon = new ImageIcon(
					new ImageIcon(imgUrl).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
			icono.setIcon(icon);
		}
		icono.setHorizontalAlignment(SwingConstants.CENTER);
		card.add(icono, BorderLayout.CENTER);

		JLabel label = new JLabel(texto, SwingConstants.CENTER);
		label.setFont(new Font("Segoe UI", Font.BOLD, 19));
		label.setForeground(EstiloUI.getColor("primario"));
		card.add(label, BorderLayout.SOUTH);

		card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onClick.run();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				card.setBackground(EstiloUI.getColor("hover"));
				label.setForeground(Color.WHITE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				card.setBackground(Color.WHITE);
				label.setForeground(EstiloUI.getColor("primario"));
			}
		});

		return card;
	}

	// Métodos para abrir los módulos
	private void abrirClientes() {
		new ClienteView(usuarioEnSesion, this, clienteController).mostrar();
	}

	private void abrirProductos() {
		new ProductoView(usuarioEnSesion, this, productoController, proveedorController, ivaController).mostrar();
	}

	// MÉTODO LISTO PARA CAMBIAR CUANDO TENGAS LA VISTA DE COMPRAS
	private void abrirCompras() {
		new CompraView(usuarioEnSesion, this, compraController, proveedorController, ivaController, productoController)
				.mostrar();
	}

	private void abrirVentas() {
		new VentaView(usuarioEnSesion, this, ventaController, clienteController, ivaController, productoController)
				.mostrar();
	}

	private void abrirProveedores() {
		new ProveedorView(usuarioEnSesion, this, proveedorController).mostrar();
	}

	private String fechaHoraActual() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss"));
	}

	// Getters para los controladores y usuario
	public Usuario getUsuarioEnSesion() {
		return usuarioEnSesion;
	}

	public ClienteController getClienteController() {
		return clienteController;
	}

	public ProveedorController getProveedorController() {
		return proveedorController;
	}

	public ProductoController getProductoController() {
		return productoController;
	}

	public IvaController getIvaController() {
		return ivaController;
	}

	public CompraController getCompraController() {
		return compraController;
	}

	public VentaController getVentaController() {
		return ventaController;
	}
}
