-- ==========================================================================================
-- SCRIPT DE INICIALIZACIÓN – gestor_ventas_app  - Jonatan Tajada Rico
--
-- Proyecto profesional de base de datos para la gestión B2B de piensos para animales.
--
-- Contenido del script:
--   • 15 tablas principales normalizadas
--   • 10 índices de consulta frecuente
--   • 21 vistas de análisis y explotación de datos
--   • 1 trigger de auditoría de stock
--
-- Preparado para despliegue y documentación de Trabajo Fin de Grado.
-- ==========================================================================================

-- ===========================================================================
-- 0. CREACIÓN DE LA BASE DE DATOS
-- ===========================================================================
CREATE DATABASE IF NOT EXISTS gestor_ventas_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestor_ventas_app;

-- ===========================================================================
-- 1. TABLAS INDEPENDIENTES (SIN FOREIGN KEYS)
-- ===========================================================================

-- ----------------------------------------------------------
-- Tabla de Tipos de IVA
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS ivas (
    id_iva INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL,
    porcentaje DECIMAL(5,2) NOT NULL,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo'
);

-- ----------------------------------------------------------
-- Tabla de Clientes (solo empresas/autónomos, no domésticos)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    razon_social VARCHAR(100) NOT NULL,
    forma_juridica ENUM('SL', 'SA', 'COOPERATIVA', 'AUTONOMO') NOT NULL,
    cif_nif VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(150),
    localidad VARCHAR(80),
    codigo_postal VARCHAR(10),
    pais VARCHAR(50),
    telefono VARCHAR(9) NOT NULL CHECK (telefono REGEXP '^[6789][0-9]{8}$'),
    email VARCHAR(100) NOT NULL CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    tipo_cliente VARCHAR(50),
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo'
);
-- ----------------------------------------------------------
-- Tabla de Proveedores (fabricantes de pienso)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    razon_social VARCHAR(100) NOT NULL,
    forma_juridica ENUM('SL', 'SA', 'COOPERATIVA', 'AUTONOMO') NOT NULL,
    cif_nif VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(150),
    localidad VARCHAR(80),
    codigo_postal VARCHAR(10),
    pais VARCHAR(50),
    telefono VARCHAR(9) NOT NULL CHECK (telefono REGEXP '^[6789][0-9]{8}$'),
    email VARCHAR(100) NOT NULL CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo'
);

-- ----------------------------------------------------------
-- Tabla de Empleados
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS empleados (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE CHECK (dni REGEXP '^[0-9]{8}[A-Z]$'),
    nombre VARCHAR(50) NOT NULL,
    apellido1 VARCHAR(50) NOT NULL,
    apellido2 VARCHAR(50),
    direccion VARCHAR(150),
    localidad VARCHAR(80),
    codigo_postal VARCHAR(5) CHECK (codigo_postal REGEXP '^[0-9]{5}$'),
    pais VARCHAR(50),
    telefono VARCHAR(9) NOT NULL CHECK (telefono REGEXP '^[6789][0-9]{8}$'),
    email VARCHAR(100) NOT NULL CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo'
);

-- ----------------------------------------------------------
-- Tabla de Usuarios del sistema (login)
-- Relación 1:1 con empleado
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado INT NOT NULL UNIQUE,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL CHECK (contrasena REGEXP '^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{6,100}$'),
    tipo ENUM('admin', 'usuario') DEFAULT 'usuario',
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ===========================================================================
-- 2. TABLAS DEPENDIENTES (CON FOREIGN KEYS Y POLÍTICA RESTRICTIVA)
-- ===========================================================================

-- ----------------------------------------------------------
-- Tabla de Productos (solo pienso para perros, gatos, caballos)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    tipo_animal ENUM('Perro', 'Gato', 'Caballo') NOT NULL,
    marca VARCHAR(50) NOT NULL,
    formato VARCHAR(50) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL CHECK (precio_venta >= 0),
    precio_compra DECIMAL(10,2) NOT NULL CHECK (precio_compra >= 0),
    id_proveedor INT NOT NULL,
    id_iva INT NOT NULL,
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    stock_minimo INT DEFAULT 0 CHECK (stock_minimo >= 0),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_iva) REFERENCES ivas(id_iva) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ----------------------------------------------------------
-- Tabla de Compras a proveedores
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS compras (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_proveedor INT NOT NULL,
    id_empleado INT NOT NULL,
    total_sin_iva DECIMAL(12,2),
    total_con_iva DECIMAL(12,2),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ----------------------------------------------------------
-- Detalles de cada compra
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS detalles_compras (
    id_detalle_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_compra INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
    porcentaje_iva DECIMAL(5,2) NOT NULL CHECK (porcentaje_iva >= 0),
    subtotal_sin_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_sin_iva >= 0),
    subtotal_con_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_con_iva >= 0),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_compra) REFERENCES compras(id_compra) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT ON UPDATE CASCADE
);


-- ----------------------------------------------------------
-- Tabla de Ventas a clientes
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_cliente INT NOT NULL,
    id_empleado INT NOT NULL,
    total_sin_iva DECIMAL(12,2),
    total_con_iva DECIMAL(12,2),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ----------------------------------------------------------
-- Detalles de cada venta
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS detalles_ventas (
    id_detalle_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
    porcentaje_iva DECIMAL(5,2) NOT NULL CHECK (porcentaje_iva >= 0),
    subtotal_sin_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_sin_iva >= 0),
    subtotal_con_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_con_iva >= 0),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT ON UPDATE CASCADE
);


-- ----------------------------------------------------------
-- Tabla de Devoluciones de clientes (ventas)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS devoluciones_clientes (
    id_devolucion_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_empleado INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(255),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS detalles_devoluciones_clientes (
    id_detalle_devolucion_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_devolucion_cliente INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    porcentaje_iva DECIMAL(5,2) NOT NULL CHECK (porcentaje_iva >= 0),
    subtotal_sin_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_sin_iva >= 0),
    subtotal_con_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_con_iva >= 0),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_devolucion_cliente) REFERENCES devoluciones_clientes(id_devolucion_cliente) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT ON UPDATE CASCADE
);


-- ----------------------------------------------------------
-- Tabla de Devoluciones a proveedor (compras)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS devoluciones_proveedores (
    id_devolucion_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    id_compra INT NOT NULL,
    id_empleado INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(255),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_compra) REFERENCES compras(id_compra) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS detalles_devoluciones_proveedores (
    id_detalle_devolucion_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    id_devolucion_proveedor INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    porcentaje_iva DECIMAL(5,2) NOT NULL CHECK (porcentaje_iva >= 0),
    subtotal_sin_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_sin_iva >= 0),
    subtotal_con_iva DECIMAL(12,2) NOT NULL CHECK (subtotal_con_iva >= 0),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_devolucion_proveedor) REFERENCES devoluciones_proveedores(id_devolucion_proveedor) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS auditorias (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    tabla_modificada VARCHAR(50),
    accion VARCHAR(50),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT
);

-- -------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- ==========================================================
-- 			3. DML: INSERCIÓN DE DATOS 
-- ==========================================================

-- ================================
-- 1. TABLAS MAESTRAS/INDEPENDIENTES
-- ================================

-- ---- IVA
INSERT INTO ivas (descripcion, porcentaje) VALUES
('IVA GENERAL', 21.00),
('IVA REDUCIDO', 10.00);

-- ---- PROVEEDORES
INSERT INTO proveedores 
(razon_social, forma_juridica, cif_nif, direccion, localidad, codigo_postal, pais, telefono, email)
VALUES
('NutriCan SL', 'SL', 'B00000001', 'Calle Mayor 10', 'Madrid', '28001', 'España', '600000001', 'nutrican@proveedor.com'),
('EquiPiensos SA', 'SA', 'A00000002', 'Av. Equina 5', 'Sevilla', '41001', 'España', '600000002', 'equipiensos@proveedor.com'),
('CatFeed Coop', 'COOPERATIVA', 'F00000003', 'Plaza Gato 3', 'Barcelona', '08001', 'España', '600000003', 'catfeed@proveedor.com'),
('AnimalFood Autónomo', 'AUTONOMO', 'Z00000004', 'Calle Libre 9', 'Valencia', '46001', 'España', '600000004', 'animalfood@proveedor.com');

-- ---- CLIENTES
INSERT INTO clientes 
(razon_social, forma_juridica, cif_nif, direccion, localidad, codigo_postal, pais, telefono, email, tipo_cliente)
VALUES
('VetCan Bilbao SL', 'SL', 'B10000001', 'Av. Sabino 1', 'Bilbao', '48002', 'España', '611000001', 'vetcan@cliente.com', 'Clínica Veterinaria'),
('Tienda Gato Feliz Coop', 'COOPERATIVA', 'F10000002', 'Calle Felina 7', 'Madrid', '28002', 'España', '611000002', 'gatofeliz@cliente.com', 'Tienda'),
('Alimentos Mascotas SA', 'SA', 'A10000003', 'Av. Animal 11', 'Valencia', '46002', 'España', '611000003', 'alimentos@cliente.com', 'Distribuidor'),
('Veterinaria Central Autonomo', 'AUTONOMO', 'Y10000004', 'C/ Salud 5', 'Sevilla', '41002', 'España', '611000004', 'central@cliente.com', 'Clínica Veterinaria'),
('DogSuper SL', 'SL', 'B10000005', 'C/ Perro 1', 'Bilbao', '48002', 'España', '611000005', 'dogsuper@cliente.com', 'Tienda'),
('Piensos del Norte SA', 'SA', 'A10000006', 'C/ Norte 1', 'Santander', '39002', 'España', '611000006', 'piensosnorte@cliente.com', 'Distribuidor');

-- ---- EMPLEADOS
INSERT INTO empleados (dni, nombre, apellido1, apellido2, direccion, localidad, codigo_postal, pais, telefono, email)
VALUES
('12345678A', 'Jonatan', 'Tajada', 'Rico', 'C/ Principal 1', 'Madrid', '28001', 'España', '678912345', 'jonatan@empresa.com'),
('87654321B', 'Laura', 'Fernandez', 'Martinez', 'C/ Secundaria 2', 'Sevilla', '41001', 'España', '679123456', 'laura@empresa.com'),
('34567890C', 'Carlos', 'Perez', 'Garcia', 'C/ Tercera 3', 'Valencia', '46003', 'España', '690123456', 'carlos@empresa.com'),
('23456789D', 'Ana', 'Gomez', 'Ruiz', 'C/ Cuarta 4', 'Barcelona', '08004', 'España', '691234567', 'ana@empresa.com');

-- ---- USUARIOS (relación 1:1 con empleados)
INSERT INTO usuarios (id_empleado, nombre_usuario, contrasena, tipo)
VALUES
(1, 'jonatan', 'Password123', 'admin'),
(2, 'laura', 'Password123', 'usuario'),
(3, 'carlos', 'Password123', 'usuario'),
(4, 'ana', 'Password123', 'usuario');

-- ================================
-- 2. TABLAS DEPENDIENTES / OPERATIVAS
-- ================================

-- ---- PRODUCTOS
INSERT INTO productos (nombre, descripcion, tipo_animal, marca, formato, precio_venta, precio_compra, id_proveedor, id_iva, stock, stock_minimo)
VALUES
('Pienso Perro Adulto', 'Nutrición completa para perros adultos', 'Perro', 'NutriCan', '15kg', 36.50, 22.00, 1, 1, 150, 20),
('Pienso Gato Esterilizado', 'Alimento premium para gatos esterilizados', 'Gato', 'CatFeed', '10kg', 28.50, 17.00, 3, 2, 100, 10),
('Pienso Caballo Energy', 'Pienso energético para caballos', 'Caballo', 'EquiPiensos', '20kg', 42.00, 28.00, 2, 1, 80, 5),
('Pienso Perro Junior', 'Pienso especial para cachorros', 'Perro', 'NutriCan', '10kg', 23.00, 14.00, 1, 1, 50, 10),
('Pienso Gato Senior', 'Alimento gatos mayores', 'Gato', 'CatFeed', '5kg', 18.00, 11.00, 3, 2, 30, 5);

-- ---- COMPRAS
INSERT INTO compras (id_proveedor, id_empleado, total_sin_iva, total_con_iva)
VALUES
(1, 1, 220.00, 266.20),
(2, 2, 340.00, 411.40),
(3, 3, 190.00, 209.00),
(3, 4, 95.00, 104.50);

-- ---- DETALLES DE CADA COMPRA
INSERT INTO detalles_compras (id_compra, id_producto, cantidad, precio_unitario, porcentaje_iva, subtotal_sin_iva, subtotal_con_iva)
VALUES
(1, 1, 10, 22.00, 21.00, 220.00, 266.20),
(2, 3, 8, 28.00, 21.00, 224.00, 271.04),
(3, 2, 5, 17.00, 10.00, 85.00, 93.50),
(3, 5, 1, 11.00, 10.00, 11.00, 12.10),
(4, 4, 4, 14.00, 21.00, 56.00, 67.76);

-- ---- VENTAS
INSERT INTO ventas (id_cliente, id_empleado, total_sin_iva, total_con_iva)
VALUES
(1, 1, 100.00, 121.00),
(2, 2, 80.00, 96.80),
(3, 3, 65.00, 71.50),
(4, 4, 40.00, 44.00),
(5, 1, 75.00, 90.75);

-- ---- DETALLES DE VENTAS
INSERT INTO detalles_ventas (id_venta, id_producto, cantidad, precio_unitario, porcentaje_iva, subtotal_sin_iva, subtotal_con_iva)
VALUES
(1, 1, 2, 36.50, 21.00, 73.00, 88.33),
(1, 4, 1, 23.00, 21.00, 23.00, 27.83),
(2, 2, 3, 28.50, 10.00, 85.50, 94.05),
(3, 3, 1, 42.00, 21.00, 42.00, 50.82),
(4, 5, 2, 18.00, 10.00, 36.00, 39.60),
(5, 1, 1, 36.50, 21.00, 36.50, 44.16);

-- ---- DEVOLUCIONES DE CLIENTES
INSERT INTO devoluciones_clientes (id_venta, id_empleado, motivo)
VALUES
(1, 2, 'Cliente no satisfecho con el formato'),
(2, 1, 'Producto equivocado');

-- ---- DETALLES DE DEVOLUCIONES DE CLIENTES
INSERT INTO detalles_devoluciones_clientes (id_devolucion_cliente, id_producto, cantidad, porcentaje_iva, subtotal_sin_iva, subtotal_con_iva)
VALUES
(1, 4, 1, 21.00, 23.00, 27.83),
(2, 2, 1, 10.00, 28.50, 31.35);

-- ---- DEVOLUCIONES A PROVEEDOR
INSERT INTO devoluciones_proveedores (id_compra, id_empleado, motivo)
VALUES
(3, 3, 'Producto en mal estado'),
(4, 4, 'Fallo en el lote recibido');

-- ---- DETALLES DE DEVOLUCIONES A PROVEEDOR
INSERT INTO detalles_devoluciones_proveedores (id_devolucion_proveedor, id_producto, cantidad, porcentaje_iva, subtotal_sin_iva, subtotal_con_iva)
VALUES
(1, 2, 1, 10.00, 17.00, 18.70),
(2, 4, 2, 21.00, 28.00, 33.88);

-- ---- AUDITORÍAS (ejemplo)
INSERT INTO auditorias (tabla_modificada, accion, descripcion)
VALUES
('productos', 'INSERT', 'Alta inicial de productos de ejemplo'),
('clientes', 'INSERT', 'Alta inicial de clientes de ejemplo'),
('ventas', 'INSERT', 'Se registraron ventas iniciales'),
('compras', 'INSERT', 'Se registraron compras iniciales'),
('detalles_ventas', 'INSERT', 'Alta de detalles de ventas'),
('detalles_compras', 'INSERT', 'Alta de detalles de compras'),
('devoluciones_clientes', 'INSERT', 'Registro de devoluciones de clientes'),
('devoluciones_proveedores', 'INSERT', 'Registro de devoluciones a proveedores');


-- -------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- ===========================================================================
-- 			4. ÍNDICES ÚTILES PARA CONSULTAS FRECUENTES
-- ===========================================================================
CREATE INDEX idx_clientes_estado ON clientes (estado);
CREATE INDEX idx_proveedores_estado ON proveedores (estado);
CREATE INDEX idx_empleados_estado ON empleados (estado);
CREATE INDEX idx_productos_estado ON productos (estado);

CREATE INDEX idx_ventas_cliente ON ventas (id_cliente);
CREATE INDEX idx_ventas_empleado ON ventas (id_empleado);
CREATE INDEX idx_detalles_ventas_venta ON detalles_ventas (id_venta);

CREATE INDEX idx_compras_proveedor ON compras (id_proveedor);
CREATE INDEX idx_compras_empleado ON compras (id_empleado);
CREATE INDEX idx_detalles_compras_compra ON detalles_compras (id_compra);

-- --------------------------------------------------------------------------------------------------------------------------------------------------------------

-- ===========================================================================
-- 			5. VISTAS DE CONSULTA Y ANÁLISIS 
-- ===========================================================================
-- ===========================================================================
-- 1. VISTAS SOBRE CLIENTES Y PROVEEDORES
-- ===========================================================================

-- 1. Listado profesional de clientes activos
-- Devuelve los clientes con estado 'activo'
CREATE OR REPLACE VIEW vista_clientes_activos AS
SELECT
    id_cliente,
    razon_social,
    forma_juridica,
    cif_nif,
    direccion,
    localidad,
    codigo_postal,
    pais,
    telefono,
    email,
    tipo_cliente,
    fecha_alta
FROM clientes
WHERE estado = 'activo';

-- 2. Clientes inactivos (para campañas o gestión de bajas lógicas)
CREATE OR REPLACE VIEW vista_clientes_inactivos AS
SELECT
    id_cliente,
    razon_social,
    forma_juridica,
    cif_nif,
    direccion,
    localidad,
    codigo_postal,
    pais,
    telefono,
    email,
    tipo_cliente,
    fecha_alta
FROM clientes
WHERE estado = 'inactivo';

-- 3. Proveedores activos
-- Devuelve los proveedores con estado 'activo'
CREATE OR REPLACE VIEW vista_proveedores_activos AS
SELECT
    id_proveedor,
    razon_social,
    forma_juridica,
    cif_nif,
    direccion,
    localidad,
    codigo_postal,
    pais,
    telefono,
    email,
    fecha_alta
FROM proveedores
WHERE estado = 'activo';


-- ===============================================================================
-- 2. VISTAS SOBRE PRODUCTOS Y STOCK
-- ===============================================================================

-- 4. Productos activos con información extendida y stock actual
CREATE OR REPLACE VIEW vista_productos_stock AS
SELECT
    p.id_producto,
    p.nombre,
    p.descripcion,
    p.tipo_animal,
    p.marca,
    p.formato,
    p.precio_venta,
    p.precio_compra,
    p.stock,
    p.stock_minimo,
    pr.razon_social AS proveedor,
    i.porcentaje AS iva,
    p.estado
FROM productos p
JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor
JOIN ivas i ON p.id_iva = i.id_iva
WHERE p.estado = 'activo';

-- 5. Productos bajo mínimo de stock
-- Útil para gestión de pedidos o alertas
CREATE OR REPLACE VIEW vista_productos_bajo_minimo AS
SELECT
    id_producto,
    nombre,
    tipo_animal,
    marca,
    formato,
    stock,
    stock_minimo
FROM productos
WHERE estado = 'activo' AND stock <= stock_minimo;

-- 6. Productos sin movimientos en los últimos 3 meses (análisis de rotación)
CREATE OR REPLACE VIEW vista_productos_sin_venta_3m AS
SELECT
    p.id_producto,
    p.nombre,
    p.stock,
    p.estado
FROM productos p
WHERE p.id_producto NOT IN (
    SELECT DISTINCT dv.id_producto
    FROM detalles_ventas dv
    JOIN ventas v ON dv.id_venta = v.id_venta
    WHERE v.fecha >= DATE_SUB(NOW(), INTERVAL 3 MONTH)
)
AND p.estado = 'activo';

-- 7. Top 10 productos más vendidos
CREATE OR REPLACE VIEW vista_top_productos_vendidos AS
SELECT
    p.id_producto,
    p.nombre,
    SUM(dv.cantidad) AS total_vendido
FROM detalles_ventas dv
JOIN productos p ON dv.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre
ORDER BY total_vendido DESC
LIMIT 10;


-- ===============================================================================
-- 3. VISTAS SOBRE VENTAS
-- ===============================================================================

-- 8. Ventas detalladas (cliente, empleado, totales)
CREATE OR REPLACE VIEW vista_ventas_detalladas AS
SELECT
    v.id_venta,
    v.fecha,
    c.razon_social AS cliente,
    e.nombre AS empleado,
    v.total_sin_iva,
    v.total_con_iva,
    v.estado
FROM ventas v
JOIN clientes c ON v.id_cliente = c.id_cliente
JOIN empleados e ON v.id_empleado = e.id_empleado
WHERE v.estado = 'activo';

-- 9. Ventas detalladas con filtro de periodo (usada para BETWEEN fechas)
CREATE OR REPLACE VIEW vista_ventas_detalladas_periodo AS
SELECT
    v.id_venta,
    v.fecha,
    c.razon_social AS cliente,
    e.nombre AS empleado,
    v.total_sin_iva,
    v.total_con_iva,
    v.estado
FROM ventas v
JOIN clientes c ON v.id_cliente = c.id_cliente
JOIN empleados e ON v.id_empleado = e.id_empleado
WHERE v.estado = 'activo';

-- 10. Ventas por cliente (ranking de clientes)
CREATE OR REPLACE VIEW vista_ranking_clientes AS
SELECT
    c.id_cliente,
    c.razon_social,
    SUM(v.total_con_iva) AS total_facturado
FROM ventas v
JOIN clientes c ON v.id_cliente = c.id_cliente
GROUP BY c.id_cliente, c.razon_social
ORDER BY total_facturado DESC;

-- 11. Ventas totales por mes (análisis temporal)
CREATE OR REPLACE VIEW vista_ventas_por_mes AS
SELECT
    YEAR(fecha) AS anio,
    MONTH(fecha) AS mes,
    COUNT(id_venta) AS total_ventas,
    SUM(total_con_iva) AS importe_ventas
FROM ventas
WHERE estado = 'activo'
GROUP BY anio, mes
ORDER BY anio DESC, mes DESC;

-- 12. Ventas realizadas por empleado
CREATE OR REPLACE VIEW vista_ventas_por_empleado AS
SELECT
    e.id_empleado,
    e.nombre,
    e.apellido1,
    COUNT(v.id_venta) AS ventas_realizadas,
    SUM(v.total_con_iva) AS total_facturado
FROM empleados e
LEFT JOIN ventas v ON e.id_empleado = v.id_empleado AND v.estado = 'activo'
GROUP BY e.id_empleado, e.nombre, e.apellido1;


-- ===============================================================================
-- 4. VISTAS SOBRE COMPRAS
-- ===============================================================================

-- 13. Compras detalladas (proveedor, empleado, totales)
CREATE OR REPLACE VIEW vista_compras_detalladas AS
SELECT
    co.id_compra,
    co.fecha,
    pr.razon_social AS proveedor,
    e.nombre AS empleado,
    co.total_sin_iva,
    co.total_con_iva,
    co.estado
FROM compras co
JOIN proveedores pr ON co.id_proveedor = pr.id_proveedor
JOIN empleados e ON co.id_empleado = e.id_empleado
WHERE co.estado = 'activo';

-- 14. Compras detalladas con filtro de periodo (usada para BETWEEN fechas)
CREATE OR REPLACE VIEW vista_compras_detalladas_periodo AS
SELECT
    c.id_compra,
    c.fecha,
    p.razon_social AS proveedor,
    e.nombre AS empleado,
    c.total_sin_iva,
    c.total_con_iva,
    c.estado
FROM compras c
JOIN proveedores p ON c.id_proveedor = p.id_proveedor
JOIN empleados e ON c.id_empleado = e.id_empleado
WHERE c.estado = 'activo';

-- 15. Compras totales por mes
CREATE OR REPLACE VIEW vista_compras_por_mes AS
SELECT
    YEAR(fecha) AS anio,
    MONTH(fecha) AS mes,
    COUNT(id_compra) AS total_compras,
    SUM(total_con_iva) AS importe_compras
FROM compras
WHERE estado = 'activo'
GROUP BY anio, mes
ORDER BY anio DESC, mes DESC;

-- 16. Compras realizadas por empleado
CREATE OR REPLACE VIEW vista_compras_por_empleado AS
SELECT
    e.id_empleado,
    e.nombre,
    e.apellido1,
    COUNT(c.id_compra) AS compras_realizadas,
    SUM(c.total_con_iva) AS total_comprado
FROM empleados e
LEFT JOIN compras c ON e.id_empleado = c.id_empleado AND c.estado = 'activo'
GROUP BY e.id_empleado, e.nombre, e.apellido1;


-- ===============================================================================
-- 5. VISTAS SOBRE DEVOLUCIONES Y CALIDAD
-- ===============================================================================

-- 17. Devoluciones de clientes (con motivo, productos y empleados implicados)
CREATE OR REPLACE VIEW vista_devoluciones_clientes AS
SELECT
    dc.id_devolucion_cliente,
    v.id_venta,
    c.razon_social AS cliente,
    e.nombre AS empleado,
    dc.fecha,
    dc.motivo,
    dc.estado
FROM devoluciones_clientes dc
JOIN ventas v ON dc.id_venta = v.id_venta
JOIN clientes c ON v.id_cliente = c.id_cliente
JOIN empleados e ON dc.id_empleado = e.id_empleado
WHERE dc.estado = 'activo';

-- 18. Devoluciones a proveedor (motivo, productos, empleados)
CREATE OR REPLACE VIEW vista_devoluciones_proveedores AS
SELECT
    dp.id_devolucion_proveedor,
    co.id_compra,
    pr.razon_social AS proveedor,
    e.nombre AS empleado,
    dp.fecha,
    dp.motivo,
    dp.estado
FROM devoluciones_proveedores dp
JOIN compras co ON dp.id_compra = co.id_compra
JOIN proveedores pr ON co.id_proveedor = pr.id_proveedor
JOIN empleados e ON dp.id_empleado = e.id_empleado
WHERE dp.estado = 'activo';

-- 19. Productos devueltos por clientes (ranking)
CREATE OR REPLACE VIEW vista_productos_devueltos_clientes AS
SELECT
    p.id_producto,
    p.nombre,
    SUM(ddc.cantidad) AS veces_devueltos
FROM detalles_devoluciones_clientes ddc
JOIN productos p ON ddc.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre
ORDER BY veces_devueltos DESC;

-- 20. Productos devueltos a proveedores (ranking)
CREATE OR REPLACE VIEW vista_productos_devueltos_proveedores AS
SELECT
    p.id_producto,
    p.nombre,
    SUM(ddp.cantidad) AS veces_devueltos
FROM detalles_devoluciones_proveedores ddp
JOIN productos p ON ddp.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre
ORDER BY veces_devueltos DESC;


-- ===============================================================================
-- 6. VISTAS TRANSVERSALES Y AUDITORÍA
-- ===============================================================================

-- 21. Historial de operaciones de auditoría
CREATE OR REPLACE VIEW vista_auditoria AS
SELECT
    id_auditoria,
    tabla_modificada,
    accion,
    fecha,
    descripcion
FROM auditorias
ORDER BY fecha DESC;

-- --------------------------------------------------------------------------------------------------------------------------------------------------

-- ==========================================================================================
-- 			6. TRIGGER DE AUDITORÍA DE CAMBIOS DE STOCK EN PRODUCTOS
-- ==========================================================================================
--  Audita cualquier cambio de stock en la tabla productos (compras, ventas, devoluciones, ajustes, etc.)
DELIMITER $$
CREATE TRIGGER tr_auditoria_producto_stock
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
  IF OLD.stock != NEW.stock THEN
    INSERT INTO auditorias (tabla_modificada, accion, descripcion)
    VALUES (
      'productos',
      'UPDATE STOCK',
      CONCAT('Stock modificado de ', OLD.stock, ' a ', NEW.stock, ' para Producto ID: ', NEW.id_producto)
    );
  END IF;
END $$
DELIMITER ;


