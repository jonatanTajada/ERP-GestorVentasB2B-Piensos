package com.gestorventasapp.app;

import javax.swing.SwingUtilities;

import com.gestorventasapp.controller.ClienteController;
import com.gestorventasapp.controller.CompraController;
import com.gestorventasapp.controller.IvaController;
import com.gestorventasapp.controller.ProductoController;
import com.gestorventasapp.controller.ProveedorController;
import com.gestorventasapp.controller.VentaController;
import com.gestorventasapp.dao.ClienteDAOImpl;

import com.gestorventasapp.dao.CompraDAOImpl;
import com.gestorventasapp.dao.IvaDAOImpl;
import com.gestorventasapp.dao.ProductoDAOImpl;
import com.gestorventasapp.dao.ProveedorDAOImpl;
import com.gestorventasapp.dao.VentaDAOImpl;
import com.gestorventasapp.service.ClienteServiceImpl;
import com.gestorventasapp.service.CompraServiceImpl;
import com.gestorventasapp.service.IvaServiceImpl;
import com.gestorventasapp.service.ProductoServiceImpl;
import com.gestorventasapp.service.ProveedorServiceImpl;
import com.gestorventasapp.service.VentaServiceImpl;
import com.gestorventasapp.view.LoginView;
import com.gestorventasapp.view.VistaPrincipal;

public class MainApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// --- CLIENTES ---
			ClienteController clienteController = new ClienteController(new ClienteServiceImpl(new ClienteDAOImpl()));

			// --- PROVEEDORES ---
			ProveedorController proveedorController = new ProveedorController(
					new ProveedorServiceImpl(new ProveedorDAOImpl()));

			// --- PRODUCTOS ---
			ProductoServiceImpl productoService = new ProductoServiceImpl(new ProductoDAOImpl());
			ProductoController productoController = new ProductoController(productoService);

			// --- IVA ---
			IvaController ivaController = new IvaController(new IvaServiceImpl(new IvaDAOImpl()));

			// --- COMPRAS ---
			CompraServiceImpl compraService = new CompraServiceImpl(new CompraDAOImpl(), productoService);
			CompraController compraController = new CompraController(compraService);

			// --- VENTAS ---
			VentaServiceImpl ventaService = new VentaServiceImpl(new VentaDAOImpl());
			VentaController ventaController = new VentaController(ventaService);

			// --- LOGIN ---
			new LoginView(usuarioAutenticado -> {
				// Solo si el login es válido se abre la VistaPrincipal
				new VistaPrincipal(usuarioAutenticado, clienteController, proveedorController, productoController,
						ivaController, compraController, ventaController);
			}).setVisible(true);
		});
	}
}
