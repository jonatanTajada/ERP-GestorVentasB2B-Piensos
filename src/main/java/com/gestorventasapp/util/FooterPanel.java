package com.gestorventasapp.util;

import javax.swing.*;
import java.awt.*;

/**
 * Pie de página fijo para toda la aplicación. Muestra datos legales, autoría, y
 * contexto del proyecto TFG.
 */
public class FooterPanel extends JPanel {
	public FooterPanel() {
		setBackground(EstiloUI.getColor("secundario"));
		setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
		setLayout(new BorderLayout());

		JLabel lblInfo = new JLabel(
				"Distribuidora de Piensos JonatanTR S.L.  |  © 2025  |  Software de Gestión Integral B2B  |  Proyecto TFG DAM   |  Autor: Jonatan Tajada");
		lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblInfo.setForeground(EstiloUI.getColor("texto"));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);

		add(lblInfo, BorderLayout.CENTER);
	}
}
