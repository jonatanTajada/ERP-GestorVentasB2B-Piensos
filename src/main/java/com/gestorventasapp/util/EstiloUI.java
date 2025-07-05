package com.gestorventasapp.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.view.AcercaDeView;
import com.gestorventasapp.view.ClienteView;
import com.gestorventasapp.view.CompraView;
import com.gestorventasapp.view.ProductoView;
import com.gestorventasapp.view.ProveedorView;
import com.gestorventasapp.view.VentaView;
import com.gestorventasapp.view.VistaPrincipal;

/**
 * Clase de utilidades para estilos visuales de la aplicación. Permite cambiar
 * tema claro/oscuro y unificar colores, fuentes y componentes comunes.
 */
public class EstiloUI {

	public enum Tema {
		CLARO, OSCURO
	}

	// Paletas de colores por tema
	private static final Map<String, Color> coloresClaro = new HashMap<>();
	private static final Map<String, Color> coloresOscuro = new HashMap<>();

	private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
	private static final Font FUENTE_GENERAL = new Font("Segoe UI", Font.PLAIN, 14);

	private static Tema temaActual = Tema.CLARO;

	static {
		// Tema claro
		coloresClaro.put("primario", new Color(41, 128, 185));
		coloresClaro.put("secundario", new Color(236, 240, 241));
		coloresClaro.put("texto", new Color(44, 62, 80));
		coloresClaro.put("hover", new Color(52, 152, 219));
		coloresClaro.put("boton", new Color(210, 230, 250));
		coloresClaro.put("boton_hover", new Color(180, 210, 240));
		coloresClaro.put("workspace", new Color(50, 50, 50));

		// Tema oscuro
		coloresOscuro.put("primario", new Color(30, 50, 70));
		coloresOscuro.put("secundario", new Color(44, 62, 80));
		coloresOscuro.put("texto", new Color(236, 240, 241));
		coloresOscuro.put("hover", new Color(41, 128, 185));
		coloresOscuro.put("boton", new Color(70, 90, 110));
		coloresOscuro.put("boton_hover", new Color(50, 70, 90));
		coloresOscuro.put("workspace", new Color(22, 22, 22));
	}

	public static Color getColor(String key) {
		return temaActual == Tema.CLARO ? coloresClaro.get(key) : coloresOscuro.get(key);
	}

	public static void cambiarTema(Tema nuevoTema) {
		temaActual = nuevoTema;
		UIManager.put("control", getColor("secundario"));
	}

	// ---- ESTILOS BÁSICOS ----

	public static void aplicarEstiloBoton(JButton boton) {
		boton.setBackground(getColor("boton"));
		boton.setForeground(getColor("texto"));
		boton.setFont(FUENTE_GENERAL);
		boton.setFocusPainted(false);
		boton.setBorder(new RoundedBorder(15));
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				boton.setBackground(getColor("boton_hover"));
			}

			public void mouseExited(MouseEvent evt) {
				boton.setBackground(getColor("boton"));
			}
		});
		boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(getColor("hover"), 1),
				BorderFactory.createEmptyBorder(10, 20, 10, 20)));
	}

	public static void aplicarEstiloBotonWorkspace(JButton boton) {
		boton.setBackground(getColor("workspace"));
		boton.setForeground(temaActual == Tema.CLARO ? Color.WHITE : getColor("texto"));
		boton.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 16));
		boton.setFocusPainted(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.setBorder(new RoundedBorder(20));
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent evt) {
				boton.setBackground(getColor("boton_hover"));
				boton.setForeground(getColor("texto"));
			}

			@Override
			public void mouseExited(MouseEvent evt) {
				boton.setBackground(getColor("workspace"));
				boton.setForeground(temaActual == Tema.CLARO ? Color.WHITE : getColor("texto"));
			}
		});
		boton.setContentAreaFilled(false);
		boton.setOpaque(false);
	}

	public static void aplicarEstiloEtiqueta(JLabel etiqueta) {
		etiqueta.setFont(FUENTE_GENERAL);
		etiqueta.setForeground(getColor("texto"));
	}

	public static void aplicarEstiloCampoTexto(JTextField campoTexto) {
		campoTexto.setFont(FUENTE_GENERAL);
		campoTexto.setForeground(getColor("texto"));
		campoTexto.setBorder(BorderFactory.createLineBorder(getColor("primario"), 1));
		campoTexto.setBackground(getColor("secundario"));
	}

	public static void configurarEstiloTabla(JTable tabla) {
		tabla.setFont(FUENTE_GENERAL);
		tabla.setRowHeight(25);
		tabla.setGridColor(getColor("secundario"));
		JTableHeader header = tabla.getTableHeader();
		header.setBackground(getColor("primario"));
		header.setForeground(getColor("texto"));
		header.setFont(FUENTE_GENERAL);
		tabla.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
	}

	public static void aplicarEstiloPanelBotones(JPanel panel) {
		panel.setBackground(getColor("secundario"));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
	}

	// ---- COMPONENTES REUTILIZABLES ----

	/**
	 * Crea el encabezado estándar para cualquier vista principal. Muestra el título
	 * (centrado), el usuario logueado y la fecha/hora (a la izquierda), y el logo
	 * corporativo (a la derecha).
	 *
	 * @param titulo  Título de la pantalla o módulo.
	 * @param usuario Usuario autenticado en sesión (no nulo).
	 * @return JPanel encabezado con el estilo corporativo y datos dinámicos.
	 */

	public static JPanel crearEncabezado(String titulo, Usuario usuario) {
		JPanel panelEncabezado = new JPanel(new BorderLayout());
		panelEncabezado.setBackground(getColor("primario"));

		// CENTRO: Título de la pantalla
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(FUENTE_TITULO);
		lblTitulo.setForeground(getColor("texto"));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		// OESTE: Panel lateral vertical con nombre de usuario, fecha y hora
		JPanel panelIzquierdo = new JPanel();
		panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
		panelIzquierdo.setOpaque(false);
		panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 12));

		// Nombre de usuario (si existe sesión)
		JLabel lblUsuario = new JLabel();
		if (usuario != null) {
			lblUsuario.setText("Usuario: " + usuario.getNombreUsuario());
		} else {
			lblUsuario.setText("Usuario: -");
		}
		lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblUsuario.setForeground(getColor("texto"));
		lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelIzquierdo.add(lblUsuario);

		// Fecha y hora dinámicas
		JLabel lblFechaHora = new JLabel();
		lblFechaHora.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblFechaHora.setForeground(getColor("texto"));
		lblFechaHora.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelIzquierdo.add(lblFechaHora);

		// Timer para actualizar cada segundo
		Timer timer = new Timer(1000, e -> {
			String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			lblFechaHora.setText("Fecha: " + fecha + "   Hora: " + hora);
		});
		timer.start();

		// ESTE: Logo corporativo (opcional)
		JLabel lblLogo = new JLabel();
		java.net.URL logoUrl = EstiloUI.class.getResource("/logo.jpg");
		if (logoUrl != null) {
			ImageIcon logoIcon = new ImageIcon(
					new ImageIcon(logoUrl).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			lblLogo.setIcon(logoIcon);
		}
		lblLogo.setHorizontalAlignment(SwingConstants.RIGHT);

		// Montaje final
		panelEncabezado.add(panelIzquierdo, BorderLayout.WEST);
		panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
		panelEncabezado.add(lblLogo, BorderLayout.EAST);

		return panelEncabezado;
	}

	public static JLabel crearEtiquetaWorkspace(String texto) {
		JLabel lblWorkspace = new JLabel(texto);
		lblWorkspace.setFont(FUENTE_GENERAL);
		lblWorkspace.setForeground(getColor("texto"));
		lblWorkspace.setHorizontalAlignment(SwingConstants.CENTER);
		lblWorkspace.setBorder(new EmptyBorder(10, 0, 10, 0));
		return lblWorkspace;
	}

	public static JPanel crearPanelBuscadorDinamico(String textoEtiqueta, JTextField campoBuscador) {
		JPanel panelBuscador = new JPanel(new BorderLayout(10, 0));
		panelBuscador.setBackground(getColor("secundario"));
		JLabel lblEtiqueta = new JLabel(textoEtiqueta);
		aplicarEstiloEtiqueta(lblEtiqueta);
		panelBuscador.add(lblEtiqueta, BorderLayout.WEST);
		aplicarEstiloCampoTexto(campoBuscador);
		panelBuscador.add(campoBuscador, BorderLayout.CENTER);
		return panelBuscador;
	}

	public static JPanel crearPanelFiltroEstado(String textoEtiqueta, JComboBox<String> comboEstados) {
		JPanel panelFiltro = new JPanel(new BorderLayout(10, 0));
		panelFiltro.setBackground(getColor("secundario"));
		JLabel lblEtiqueta = new JLabel(textoEtiqueta);
		aplicarEstiloEtiqueta(lblEtiqueta);
		panelFiltro.add(lblEtiqueta, BorderLayout.WEST);
		comboEstados.setFont(FUENTE_GENERAL);
		comboEstados.setBackground(getColor("secundario"));
		comboEstados.setForeground(getColor("texto"));
		panelFiltro.add(comboEstados, BorderLayout.CENTER);
		return panelFiltro;
	}

	public static void configurarToolTip(JComponent componente, String texto) {
		componente.setToolTipText(texto);
		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(5000);
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Clases internas para render y bordes
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

	static class RoundedBorder implements Border {
		private int radius;

		RoundedBorder(int radius) {
			this.radius = radius;
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
		}

		@Override
		public boolean isBorderOpaque() {
			return true;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------

	static class AlternatingRowRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (!isSelected) {
				c.setBackground(row % 2 == 0 ? getColor("secundario") : Color.WHITE);
			}
			return c;
		}
	}

	// ---- Cambiar tema desde menú/botón ----

	public static void toggleTema() {
		if (temaActual == Tema.CLARO) {
			cambiarTema(Tema.OSCURO);
		} else {
			cambiarTema(Tema.CLARO);
		}
	}

	public static JButton crearBotonCambiarTema(JFrame ventana) {
		JButton btnTema = new JButton("Cambiar tema");
		aplicarEstiloBoton(btnTema);
		btnTema.addActionListener(e -> {
			toggleTema();
			ventana.repaint();
			JOptionPane.showMessageDialog(ventana,
					"Tema cambiado a " + (temaActual == Tema.CLARO ? "claro" : "oscuro"));
		});
		return btnTema;
	}

	/**
	 * Crea la barra de menú principal con acceso a los módulos más importantes.
	 * Modifica aquí para añadir o quitar accesos directos.
	 */
	public static JMenuBar crearBarraMenu(JFrame ventana, VistaPrincipal vistaPrincipal) {
		JMenuBar barraMenu = new JMenuBar();

		JMenu menuGestion = new JMenu("Gestión");
		JMenuItem menuClientes = new JMenuItem("Clientes");
		JMenuItem menuProveedores = new JMenuItem("Proveedores");
		JMenuItem menuProductos = new JMenuItem("Productos");
		JMenuItem menuVentas = new JMenuItem("Ventas");
		JMenuItem menuCompras = new JMenuItem("Compras");

		// --- MENÚ FUNCIONAL PARA LOS MÓDULOS YA IMPLEMENTADOS ---
		menuClientes.addActionListener(e -> new ClienteView(vistaPrincipal.getUsuarioEnSesion(), vistaPrincipal,
				vistaPrincipal.getClienteController()).mostrar());

		menuProveedores.addActionListener(e -> new ProveedorView(vistaPrincipal.getUsuarioEnSesion(), vistaPrincipal,
				vistaPrincipal.getProveedorController()).mostrar());

		menuProductos.addActionListener(e -> new ProductoView(vistaPrincipal.getUsuarioEnSesion(), vistaPrincipal,
				vistaPrincipal.getProductoController(), vistaPrincipal.getProveedorController(),
				vistaPrincipal.getIvaController()).mostrar());

		// --- NUEVO: MENÚ FUNCIONAL PARA COMPRAS ---
		menuCompras.addActionListener(e -> new CompraView(vistaPrincipal.getUsuarioEnSesion(), vistaPrincipal,
				vistaPrincipal.getCompraController(), vistaPrincipal.getProveedorController(),
				vistaPrincipal.getIvaController(), vistaPrincipal.getProductoController()).mostrar());

		menuVentas.addActionListener(e -> new VentaView(vistaPrincipal.getUsuarioEnSesion(), vistaPrincipal,
				vistaPrincipal.getVentaController(), vistaPrincipal.getClienteController(),
				vistaPrincipal.getIvaController(), vistaPrincipal.getProductoController()).mostrar());

		menuGestion.add(menuClientes);
		menuGestion.add(menuProveedores);
		menuGestion.add(menuProductos);
		menuGestion.add(menuVentas);
		menuGestion.add(menuCompras);
		menuGestion.add(menuVentas);
		barraMenu.add(menuGestion);

		JMenu menuAyuda = new JMenu("Ayuda");
		JMenuItem menuAcercaDe = new JMenuItem("Acerca de");
		menuAcercaDe.addActionListener(e -> AcercaDeView.mostrar(ventana));
		menuAyuda.add(menuAcercaDe);
		barraMenu.add(menuAyuda);

		return barraMenu;
	}

}
