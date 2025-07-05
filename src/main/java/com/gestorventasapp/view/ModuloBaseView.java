package com.gestorventasapp.view;

import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.util.FooterPanel;
import com.gestorventasapp.model.Usuario;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Plantilla base reutilizable para cualquier módulo de gestión (Clientes,
 * Proveedores...). Incorpora encabezado, footer, menú, tabla con buscador y
 * panel de botones. Herédala y personaliza los métodos abstractos para cada
 * módulo.
 */
public abstract class ModuloBaseView {

	protected JFrame ventana;
	protected JTable tabla;
	protected DefaultTableModel modeloTabla;
	protected JTextField campoBuscador;
	protected JPanel panelBotones;
	protected Usuario usuarioEnSesion; 
	protected VistaPrincipal vistaPrincipal; 

	// --- NUEVO: panel de filtros arriba de la tabla (buscador + combos, etc.) ---
	protected JPanel panelFiltros;

	/**
	 * Constructor base.
	 *
	 * @param usuarioEnSesion Usuario autenticado en la sesión.
	 * @param vistaPrincipal  Referencia a la vista principal (para menús,
	 *                        navegación y controladores).
	 * @param titulo          Título a mostrar en el encabezado y ventana.
	 * @param columnas        Columnas que tendrá la tabla del módulo.
	 */
	public ModuloBaseView(Usuario usuarioEnSesion, VistaPrincipal vistaPrincipal, String titulo, String[] columnas) {
		this.usuarioEnSesion = usuarioEnSesion;
		this.vistaPrincipal = vistaPrincipal;

		ventana = new JFrame(titulo);
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ventana.setSize(950, 620);
		ventana.setMinimumSize(new Dimension(850, 550));
		ventana.setLayout(new BorderLayout());

		// Menú superior modular (ahora recibe VistaPrincipal)
		ventana.setJMenuBar(crearBarraMenu());

		// Encabezado profesional reutilizable
		JPanel encabezado = EstiloUI.crearEncabezado(titulo, usuarioEnSesion);
		ventana.add(encabezado, BorderLayout.NORTH);

		// --- NUEVO: Panel superior de filtros (buscador + combos, etc.) ---
		panelFiltros = new JPanel();
		panelFiltros.setBackground(EstiloUI.getColor("secundario"));
		panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 4));


		// Panel central: tabla y botones
		JPanel panelCentral = new JPanel(new BorderLayout(18, 18));
		panelCentral.setBackground(EstiloUI.getColor("secundario"));
		panelCentral.setBorder(BorderFactory.createEmptyBorder(18, 24, 10, 24));

		// --- Añadir panel de filtros arriba del panel central ---
		panelCentral.add(panelFiltros, BorderLayout.NORTH);

		// Tabla central
		modeloTabla = new DefaultTableModel(columnas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabla = new JTable(modeloTabla);
		EstiloUI.configurarEstiloTabla(tabla);
		JScrollPane scrollTabla = new JScrollPane(tabla);
		panelCentral.add(scrollTabla, BorderLayout.CENTER);

		// Panel de botones abajo
		panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		EstiloUI.aplicarEstiloPanelBotones(panelBotones);
		inicializarBotones();
		panelCentral.add(panelBotones, BorderLayout.SOUTH);

		// Añadir panel central al centro de la ventana
		ventana.add(panelCentral, BorderLayout.CENTER);

		// Footer uniforme para toda la app
		ventana.add(new FooterPanel(), BorderLayout.SOUTH);

		// Al cerrar ventana, liberar recursos si es necesario (sobrescribible)
		ventana.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCerrarVentana();
			}
		});

		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);

	}

	/**
	 * Menú superior del módulo. Puedes sobrescribir para añadir/quitar elementos.
	 */
	protected JMenuBar crearBarraMenu() {
		// Ahora pasamos la vista principal al menú, para controladores y usuario
		return EstiloUI.crearBarraMenu(ventana, vistaPrincipal);
	}

	// Inicializa los botones de la vista (sobrescribir en cada módulo)
	protected abstract void inicializarBotones();

	// Cargar los datos originales de la tabla (sobrescribir en cada módulo)
	protected abstract void cargarDatosOriginales();

	// Obtener los datos filtrados según el texto del buscador
	protected abstract Object[][] obtenerDatosFiltrados(String textoFiltro);

	// Acción al cerrar la ventana (opcional, puede sobrescribirse)
	protected void onCerrarVentana() {
		// Por defecto, nada especial.
	}

	// Refresca el contenido de la tabla con los datos dados
	protected void actualizarTabla(Object[][] datos) {
		modeloTabla.setRowCount(0);
		for (Object[] fila : datos) {
			modeloTabla.addRow(fila);
		}
	}

	// Hace visible la ventana del módulo
	public void mostrar() {
		ventana.setVisible(true);
	}

	// Oculta la ventana del módulo
	public void ocultar() {
		ventana.setVisible(false);
	}

	// Configura el filtro dinámico del buscador (solo si usas campoBuscador
	// siempre)
	protected void configurarBuscadorDinamico() {
		if (campoBuscador != null) {
			campoBuscador.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					filtrarTabla();
				}

				public void removeUpdate(DocumentEvent e) {
					filtrarTabla();
				}

				public void changedUpdate(DocumentEvent e) {
					filtrarTabla();
				}
			});
		}
	}

	// Lógica para filtrar la tabla según el texto del buscador
	protected void filtrarTabla() {
		String texto = campoBuscador != null ? campoBuscador.getText().trim().toLowerCase() : "";
		if (texto.isEmpty()) {
			cargarDatosOriginales();
		} else {
			Object[][] datosFiltrados = obtenerDatosFiltrados(texto);
			actualizarTabla(datosFiltrados);
		}
	}

	/**
	 * Ejecuta una tarea en segundo plano usando SwingWorker y muestra un indicador
	 * de carga.
	 * 
	 * @param tareaBG         Código a ejecutar en segundo plano.
	 * @param despuesDeCargar Código a ejecutar después, puede ser null.
	 */
	protected void ejecutarSwingWorker(Runnable tareaBG, Runnable despuesDeCargar) {
		// Indicador de carga simple
		JDialog dialogoCarga = new JDialog(ventana, "Cargando...", true);
		JPanel panelCarga = new JPanel();
		panelCarga.add(new JLabel("Cargando datos..."));
		dialogoCarga.getContentPane().add(panelCarga);
		dialogoCarga.setSize(220, 70);
		dialogoCarga.setLocationRelativeTo(ventana);

		new SwingWorker<Void, Void>() {
			protected Void doInBackground() {
				tareaBG.run();
				return null;
			}

			protected void done() {
				dialogoCarga.dispose();
				if (despuesDeCargar != null)
					despuesDeCargar.run();
			}
		}.execute();
		dialogoCarga.setVisible(true);
	}
}
