package com.gestorventasapp.view;

import javax.swing.*;
import java.awt.*;

public class AcercaDeView extends JDialog {
	public AcercaDeView(JFrame parent) {
		super(parent, "Acerca de la aplicación", true);
		setSize(580, 410);
		setLocationRelativeTo(parent);
		setResizable(false);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 248, 255));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(28, 34, 18, 34));

		JLabel lblTitulo = new JLabel("Gestor de Ventas B2B – Distribuidora de Piensos");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblVersion = new JLabel("Versión 1.0  |  Autor: Jonatan Tajada Rico");
		lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setBackground(new Color(245, 248, 255));
		area.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		area.setText(
				"""
							¿Preguntas frecuentes?


						- ¿Quién puede acceder a la aplicación?
						  Solo empleados/usuarios registrados y activos en el sistema, cada uno vinculado a un empleado real de la empresa.

						- ¿Se puede modificar o eliminar cualquier dato?
						  Sí, puedes modificar registros desde cada módulo. La eliminación es lógica: el dato queda como “inactivo” pero nunca se borra de la base de datos.

						- ¿Qué ocurre si intento guardar un registro incompleto?
						  El sistema valida los datos obligatorios y muestra mensajes de ayuda, evitando errores y registros inválidos.

						- ¿Cómo se actualiza el stock de productos?
						  El inventario se actualiza automáticamente al registrar compras, ventas o devoluciones, asegurando que el stock nunca sea negativo.

						- ¿La base de datos es ampliable?
						  Sí, el sistema está diseñado para evolucionar y adaptarse a nuevas necesidades, productos, informes y usuarios.

						- ¿Se pueden exportar los datos?
						  De forma básica sí, puedes copiar los datos de las tablas o generar informes. (Funcionalidad de exportación avanzada sujeta a futuras mejoras).

						- ¿A qué animales está orientada la aplicación?
						  Únicamente a empresas que gestionan piensos para perros, gatos y caballos, cumpliendo la normativa vigente.

						- ¿Qué ocurre si olvido mi usuario o contraseña?
						  Consulta al administrador de la empresa para el restablecimiento de tu cuenta.

						- ¿Qué hacer si la aplicación muestra un mensaje de error?
						  Revisa los datos introducidos y consulta los mensajes de ayuda. Si el problema persiste, contacta con el soporte técnico de la empresa.

									¡Gracias por confiar en el Gestor de Ventas B2B!
							""");

		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(9));
		panel.add(lblVersion);
		panel.add(Box.createVerticalStrut(18));
		panel.add(new JScrollPane(area));
		panel.add(Box.createVerticalStrut(16));

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		btnCerrar.addActionListener(e -> dispose());
		btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(btnCerrar);
		add(panel);
	}

	public static void mostrar(JFrame parent) {
		new AcercaDeView(parent).setVisible(true);
	}
}
