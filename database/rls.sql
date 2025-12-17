-- =========================================================
-- rls.sql
-- RLS backend-first: frontend NO accede tablas directo.
-- Backend accede por JDBC con credenciales de DB.
-- =========================================================

-- Activar RLS en tablas
ALTER TABLE usuario ENABLE ROW LEVEL SECURITY;
ALTER TABLE cliente ENABLE ROW LEVEL SECURITY;
ALTER TABLE cotizaciones ENABLE ROW LEVEL SECURITY;
ALTER TABLE cotizacion_servicios ENABLE ROW LEVEL SECURITY;
ALTER TABLE cotizacion_piezas ENABLE ROW LEVEL SECURITY;
ALTER TABLE envios_correo ENABLE ROW LEVEL SECURITY;
ALTER TABLE pieza ENABLE ROW LEVEL SECURITY;
ALTER TABLE cache_scrapy ENABLE ROW LEVEL SECURITY;
ALTER TABLE resultados_scrapy ENABLE ROW LEVEL SECURITY;
ALTER TABLE configuracion ENABLE ROW LEVEL SECURITY;

-- Importante:
-- No se crean POLICIES aquí a propósito.
-- Con RLS ON y sin policies, anon/authenticated NO acceden.
-- El backend (con rol de DB privilegiado) será el que opere.
