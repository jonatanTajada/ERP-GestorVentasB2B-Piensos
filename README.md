# GestorVentasB2B-Piensos

Aplicación de escritorio profesional para la gestión integral de ventas, compras, stock y clientes en una distribuidora B2B de piensos (perros, gatos y caballos).

**Jonatan Tajada Rico- 2025**

---

## 📑 Tabla de contenidos
- [Resumen](#resumen)
- [Estado del desarrollo](#estado-del-desarrollo)
- [Mapa mental](#mapa-mental)
- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Instalación y puesta en marcha](#instalación-y-puesta-en-marcha)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Documentación](#documentación)
- [Autoría y créditos](#autoría-y-créditos)
- [Licencia](#licencia)

---

## 📝 Resumen

Gestor de Ventas App es una solución avanzada para empresas distribuidoras/intermediarias B2B de piensos. Permite centralizar y optimizar todas las operaciones: gestión de clientes, proveedores, productos, compras y ventas, con máxima trazabilidad, seguridad y eficiencia.

---

## 🧠 Estado del desarrollo

> **Nota importante:**  
> El desarrollo de la **interfaz de usuario** (Swing) sigue en curso y se va ampliando poco a poco en función del tiempo disponible.
>
> - **La base de datos implementa:**
>   - 15 tablas principales totalmente normalizadas.
>   - 21 vistas SQL de análisis y explotación de datos (KPIs, rankings, informes, etc.).
>   - 10 índices para acelerar consultas habituales.
>   - 1 trigger de auditoría para control de cambios en stock.
>
> - **El backend Java ya tiene implementados numerosos módulos** (devoluciones, auditoría, informes, etc.) que aún no están reflejados en la interfaz visual.
>
> - **Actualmente la interfaz Swing incluye 5 módulos principales** (Clientes, Productos, Proveedores, Compras y Ventas), pero el diseño es totalmente modular y está preparado para incorporar fácilmente el resto de funcionalidades existentes en la base de datos y la lógica de negocio.
>
> - Toda la estructura está pensada para evolucionar hacia una solución ERP B2B más completa en futuras versiones.

---

## 🗺️ Mapa mental

![Mapa mental de la arquitectura del proyecto](Documentacion_ gestorVentasApp/mapa_mental_arquitectura.png)

---

## 🚀 Características principales

- Multiusuario y multipantalla.
- Gestión completa de clientes, proveedores, productos, compras y ventas.
- Buscadores y filtros avanzados en todas las tablas.
- Eliminación lógica (no se borra físicamente ningún dato relevante).
- Control automático de stock tras cada operación.
- Auditoría y trazabilidad total de operaciones.
- Validaciones estrictas (DNI, emails, stock, precios…).
- Interfaz profesional Swing.
- Actualización automática y manual de datos.
- Base de datos preparada para futuras ampliaciones: devoluciones, informes, dashboard de KPIs, integración externa, multi-idioma...

---

## 🛠️ Tecnologías utilizadas

- **Java 17+** (Eclipse IDE)
- **Swing / SwingWorker** (interfaz y gestión de hilos)
- **MySQL 8.x** (gestión de base de datos)
- **Hibernate ORM**
- **Lombok, Jakarta Bean Validation**
- **Git** (control de versiones)

---

## 🏗️ Instalación y puesta en marcha

1. Clona el repositorio:

   git clone https://github.com/jonatanTajada/GestorVentasB2B-Piensos.git

2. Importa la base de datos desde base de datos/BBDDGestorVentasApp.sql.

3. Configura la conexión a MySQL en hibernate.cfg.xml.

4. Abre el proyecto en Eclipse y ejecuta MainApp.java.


## 📁 Estructura del proyecto

src/main/java/com/gestorventasapp/
  ├── app/
  ├── controller/
  ├── dao/
  ├── enums/
  ├── exceptions/
  ├── model/
  ├── service/
  ├── util/
  └── view/
Documentacion_ gestorVentasApp/
  ├── mapa_mental_arquitectura.png
  ├── Documentacion tecnica.pdf
  └── Documentacion Usabilidad.pdf
base de datos/
  └── BBDDGestorVentasApp.sql


## 📚 Documentación

Documentación técnica (PDF)

Guía de usabilidad (PDF)

Mapa mental de arquitectura (PNG)

Script de la base de datos (SQL)

Toda la documentación adicional se encuentra en la carpeta correspondiente.


## 👤 Autoría y créditos

Jonatan Tajada Rico
tajadarico@gmail.com

## 🏷️ Licencia

Uso libre para revisión y aprendizaje.
Para uso comercial, contactar con el autor.

