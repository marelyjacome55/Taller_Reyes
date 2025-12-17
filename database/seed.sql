-- =========================================================
-- seed.sql
-- Catálogos base para que el UI tenga opciones (Área/Puesto)
-- =========================================================

-- Áreas
INSERT INTO area (nombre) VALUES
('Taller'),
('Administracion');

-- Puestos
INSERT INTO puesto (nombre, id_area)
SELECT 'Cotizador', id_area FROM area WHERE nombre='Taller';

INSERT INTO puesto (nombre, id_area)
SELECT 'Administrador', id_area FROM area WHERE nombre='Administracion';

INSERT INTO puesto (nombre, id_area)
SELECT 'Contador', id_area FROM area WHERE nombre='Administracion';
