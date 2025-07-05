package com.gestorventasapp.view;

import com.gestorventasapp.controller.UsuarioController;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.service.UsuarioServiceImpl;
import com.gestorventasapp.dao.UsuarioDAOImpl;
import com.gestorventasapp.util.EstiloUI;
import com.gestorventasapp.util.FooterPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.function.Consumer;

public class LoginView extends JFrame {

	private JTextField campoUsuario;
	private JPasswordField campoContrasena;
	private JLabel lblErrorUsuario;
	private JLabel lblErrorContrasena;
	private JLabel lblMensajeLogin;
	private JButton btnLogin;

	private final Consumer<Usuario> onLoginSuccess;

	private final UsuarioController usuarioController = new UsuarioController(
			new UsuarioServiceImpl(new UsuarioDAOImpl()));

	public static Usuario usuarioAutenticado = null;

	public LoginView(Consumer<Usuario> onLoginSuccess) {

		this.onLoginSuccess = onLoginSuccess;

		setTitle("Inicio de Sesión");
		setSize(650, 650);
		setMinimumSize(new Dimension(440, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); 
		setResizable(true);

		setLayout(new BorderLayout());

		// Panel central con BoxLayout para centrar y distribuir mejor los elementos
		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panelCentral.setBackground(EstiloUI.getColor("secundario"));

		// Logo (opcional)
		JLabel lblLogo = new JLabel();
		URL logoUrl = EstiloUI.class.getResource("/logo.jpg");
		if (logoUrl != null) {
			lblLogo.setIcon(
					new ImageIcon(new ImageIcon(logoUrl).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
		}
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Título
		JLabel lblTitulo = new JLabel("Gestor de Ventas - Login");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		panelCentral.add(Box.createVerticalStrut(40));
		panelCentral.add(lblLogo);
		panelCentral.add(Box.createVerticalStrut(18));
		panelCentral.add(lblTitulo);
		panelCentral.add(Box.createVerticalStrut(40));

		// Panel de campos
		JPanel panelCampos = new JPanel();
		panelCampos.setLayout(new GridBagLayout());
		panelCampos.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 14, 5, 14);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;

		// Usuario
		JLabel lblUsuario = new JLabel("Usuario:");
		EstiloUI.aplicarEstiloEtiqueta(lblUsuario);
		panelCampos.add(lblUsuario, gbc);

		gbc.gridx = 1;
		campoUsuario = new JTextField();
		campoUsuario.setPreferredSize(new Dimension(220, 28));
		EstiloUI.aplicarEstiloCampoTexto(campoUsuario);
		panelCampos.add(campoUsuario, gbc);

		// Error usuario
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		lblErrorUsuario = new JLabel(" ");
		lblErrorUsuario.setForeground(Color.RED);
		lblErrorUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		panelCampos.add(lblErrorUsuario, gbc);

		// Contraseña
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		JLabel lblContrasena = new JLabel("Contraseña:");
		EstiloUI.aplicarEstiloEtiqueta(lblContrasena);
		panelCampos.add(lblContrasena, gbc);

		gbc.gridx = 1;
		campoContrasena = new JPasswordField();
		campoContrasena.setPreferredSize(new Dimension(220, 28));
		EstiloUI.aplicarEstiloCampoTexto(campoContrasena);
		panelCampos.add(campoContrasena, gbc);

		// Error contraseña
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		lblErrorContrasena = new JLabel(" ");
		lblErrorContrasena.setForeground(Color.RED);
		lblErrorContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		panelCampos.add(lblErrorContrasena, gbc);

		panelCentral.add(panelCampos);

		// Mensaje general
		panelCentral.add(Box.createVerticalStrut(10));
		lblMensajeLogin = new JLabel(" ");
		lblMensajeLogin.setForeground(Color.RED);
		lblMensajeLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblMensajeLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelCentral.add(lblMensajeLogin);

		// Botón login
		panelCentral.add(Box.createVerticalStrut(20));
		btnLogin = new JButton("Iniciar Sesión");
		EstiloUI.aplicarEstiloBoton(btnLogin);
		btnLogin.setEnabled(false);
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelCentral.add(btnLogin);

		// DocumentListener para validaciones
		campoUsuario.getDocument().addDocumentListener(new SimpleDocListener(this::validarCampos));
		campoContrasena.getDocument().addDocumentListener(new SimpleDocListener(this::validarCampos));
		btnLogin.addActionListener(e -> login());

		// ENTER hace login
		campoContrasena.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && btnLogin.isEnabled()) {
					login();
				}
			}
		});

		add(panelCentral, BorderLayout.CENTER);
		add(new FooterPanel(), BorderLayout.SOUTH);
	}

	// Validaciones básicas
	private void validarCampos() {
		String usuario = campoUsuario.getText().trim();
		String pass = new String(campoContrasena.getPassword()).trim();

		boolean usuarioOk = usuario.matches("^.{1,50}$");
		boolean passOk = pass.matches("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,100}$");

		lblErrorUsuario.setText(usuarioOk ? " " : "El usuario es obligatorio (máx 50 caracteres)");
		lblErrorContrasena.setText(passOk ? " " : "Mín. 6 caracteres, al menos una mayúscula y un número");

		campoUsuario.setBackground(usuarioOk ? EstiloUI.getColor("secundario") : new Color(255, 220, 220));
		campoContrasena.setBackground(passOk ? EstiloUI.getColor("secundario") : new Color(255, 220, 220));

		btnLogin.setEnabled(usuarioOk && passOk);
		lblMensajeLogin.setText(" ");
	}

	private void login() {
		String usuario = campoUsuario.getText().trim();
		String pass = new String(campoContrasena.getPassword()).trim();

		try {
			boolean loginValido = usuarioController.checkLogin(usuario, pass);

			if (loginValido) {
				usuarioAutenticado = usuarioController.buscarPorNombreUsuario(usuario);
				JOptionPane.showMessageDialog(this, "¡Bienvenido/a, " + usuarioAutenticado.getNombreUsuario() + "!",
						"Login correcto", JOptionPane.INFORMATION_MESSAGE);

				// USO DEL CALLBACK
				if (onLoginSuccess != null) {
					onLoginSuccess.accept(usuarioAutenticado);
				}
				dispose();

			} else {
				lblMensajeLogin.setText("Usuario o contraseña incorrectos, o usuario inactivo.");
			}
		} catch (Exception ex) {
			lblMensajeLogin.setText("Error en el login: " + ex.getMessage());
		}
	}

	// DocumentListener simplificado
	private static class SimpleDocListener implements javax.swing.event.DocumentListener {
		private final Runnable onChange;

		SimpleDocListener(Runnable onChange) {
			this.onChange = onChange;
		}

		public void insertUpdate(DocumentEvent e) {
			onChange.run();
		}

		public void removeUpdate(DocumentEvent e) {
			onChange.run();
		}

		public void changedUpdate(DocumentEvent e) {
			onChange.run();
		}
	}
}
