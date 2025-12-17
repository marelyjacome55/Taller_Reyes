-- =========================================================
-- schema.sql
-- Esquema final (Supabase Postgres) alineado a UI + backend-first
-- =========================================================

-- -------------------------
-- ENUMS
-- -------------------------
CREATE TYPE area_nombre AS ENUM ('Taller', 'Administracion');

CREATE TYPE puesto_nombre AS ENUM ('Cotizador', 'Administrador', 'Contador');

CREATE TYPE tipo_cliente_enum AS ENUM ('Particular', 'Empresa');

CREATE TYPE estado_cotizacion AS ENUM ('Aprobado', 'Desaprobado');

CREATE TYPE estatus_usuario AS ENUM ('Activo', 'Inactivo');

CREATE TYPE metodo_pago_enum AS ENUM ('Transferencia', 'Efectivo');

CREATE TYPE forma_pago_enum AS ENUM ('Contado', 'Pagos');

-- -------------------------
-- TABLAS PRINCIPALES
-- -------------------------
CREATE TABLE area (
  id_area BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre area_nombre NOT NULL
);

CREATE TABLE puesto (
  id_puesto BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre puesto_nombre NOT NULL,
  id_area BIGINT REFERENCES area(id_area)
);

-- Empleados (relacionado con Supabase Auth: UUID)
CREATE TABLE usuario (
  id_usuario UUID PRIMARY KEY,
  nombre VARCHAR(250) NOT NULL,
  correo VARCHAR(150) NOT NULL,
  telefono VARCHAR(20),
  direccion TEXT,

  id_puesto BIGINT REFERENCES puesto(id_puesto),
  id_area BIGINT REFERENCES area(id_area),

  fecha_ingreso DATE,
  estatus estatus_usuario NOT NULL
);

-- Recomendado: correo único para login
CREATE UNIQUE INDEX ux_usuario_correo ON usuario (LOWER(correo));

-- Clientes
CREATE TABLE cliente (
  id_cliente BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre VARCHAR(250) NOT NULL,

  correo VARCHAR(150),
  telefono VARCHAR(20),
  rfc VARCHAR(20),
  direccion TEXT,

  tipo_cliente tipo_cliente_enum NOT NULL,
  estatus BOOLEAN NOT NULL DEFAULT TRUE,

  fecha_ingreso DATE NOT NULL
);

-- -------------------------
-- PIEZA (CATÁLOGO)
-- -------------------------
CREATE TABLE pieza (
  id_pieza BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  cantidad INT,
  unidad VARCHAR(50),
  nombre VARCHAR(150) NOT NULL,
  precio_unitario DECIMAL(12,2),
  descuento DECIMAL(12,2),
  iva DECIMAL(12,2),
  retencion_iva DECIMAL(12,2),
  subtotal DECIMAL(12,2),
  total_con_impuestos DECIMAL(12,2)
);

-- -------------------------
-- COTIZACIONES (CABECERA)
-- -------------------------
CREATE TABLE cotizaciones (
  id_cotizaciones BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  folio VARCHAR(20),

  metodo_pago metodo_pago_enum,
  forma_pago forma_pago_enum,

  condiciones_pago VARCHAR(50),
  divisa VARCHAR(10),
  tipo_cambio DECIMAL(10,4),

  id_cliente BIGINT REFERENCES cliente(id_cliente),

  encabezado TEXT,

  -- VEHÍCULO
  marca VARCHAR(50) NOT NULL,
  modelo VARCHAR(50) NOT NULL,
  anio VARCHAR(10) NOT NULL,
  version VARCHAR(100),
  motor VARCHAR(100),

  -- TOTALES
  subtotal DECIMAL(12,2),
  descuento_total DECIMAL(12,2),
  iva_total DECIMAL(12,2),
  retencion_iva_total DECIMAL(12,2),
  total DECIMAL(12,2),
  gran_total DECIMAL(12,2),

  estado estado_cotizacion,

  fecha DATE,
  id_usuario UUID REFERENCES usuario(id_usuario)
);

-- -------------------------
-- COTIZACIÓN - SERVICIOS (MANO DE OBRA) (HIJA)
-- -------------------------
CREATE TABLE cotizacion_servicios (
  id_cotizacion_servicios BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  id_cotizaciones BIGINT NOT NULL REFERENCES cotizaciones(id_cotizaciones) ON DELETE CASCADE,

  nombre VARCHAR(150) NOT NULL,
  descripcion TEXT,
  monto DECIMAL(12,2) NOT NULL DEFAULT 0
);

-- -------------------------
-- COTIZACIÓN - PIEZAS (HIJA)
-- -------------------------
CREATE TABLE cotizacion_piezas (
  id_cotizacion_piezas BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  id_cotizaciones BIGINT REFERENCES cotizaciones(id_cotizaciones) ON DELETE CASCADE,

  nombre VARCHAR(150),
  unidad VARCHAR(50),
  cantidad INT,
  precio_unitario DECIMAL(12,2),
  descuento DECIMAL(12,2),
  iva DECIMAL(12,2),
  retencion_iva DECIMAL(12,2),
  subtotal DECIMAL(12,2),
  total_con_impuestos DECIMAL(12,2)
);

-- -------------------------
-- ENVÍOS DE CORREO
-- -------------------------
CREATE TABLE envios_correo (
  id_envios_correo BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  id_cotizaciones BIGINT REFERENCES cotizaciones(id_cotizaciones) ON DELETE CASCADE,

  -- UI: Nuevo mensaje
  correo_origen VARCHAR(150),
  correo_destino VARCHAR(150),
  asunto VARCHAR(200),
  cuerpo TEXT,
  pdf_url TEXT,

  -- Log técnico
  exito BOOLEAN,
  mensaje_error TEXT,
  fecha_envio TIMESTAMP,

  -- Respuesta cliente
  respuesta_cliente TEXT,
  fecha_respuesta TIMESTAMP,
  estado_resultante estado_cotizacion
);

-- -------------------------
-- SCRAPING
-- -------------------------
CREATE TABLE cache_scrapy (
  id_cache_scrapy BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  termino VARCHAR(150),
  data_json TEXT,
  fecha TIMESTAMP
);

CREATE TABLE resultados_scrapy (
  id_resultados_scrapy BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  id_cotizaciones BIGINT REFERENCES cotizaciones(id_cotizaciones) ON DELETE CASCADE,

  termino_busqueda VARCHAR(150),
  fuente VARCHAR(50),
  titulo VARCHAR(150),
  precio DECIMAL(12,2),
  url VARCHAR(255),
  fecha TIMESTAMP
);

-- -------------------------
-- CONFIGURACIÓN GLOBAL
-- -------------------------
CREATE TABLE configuracion (
  id_configuracion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  clave VARCHAR(100),
  valor TEXT,
  descripcion TEXT,
  actualizado_en TIMESTAMP
);
