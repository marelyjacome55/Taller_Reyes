-- =========================================================
-- keepalive.sql
-- Mantener actividad mínima en Supabase (plan free) usando pg_cron
-- Ejecuta un SELECT 1 de forma programada.
-- =========================================================

-- 1) Habilitar extensión (si ya existe, no hace nada)
CREATE EXTENSION IF NOT EXISTS pg_cron;

-- 2) (Opcional) Ver jobs existentes antes de crear uno nuevo
-- SELECT * FROM cron.job;

-- 3) Evitar duplicado: si ya existe un job con ese nombre, bórralo
DO $$
DECLARE
  existing_job_id INTEGER;
BEGIN
  SELECT jobid INTO existing_job_id
  FROM cron.job
  WHERE jobname = 'keepalive-daily'
  LIMIT 1;

  IF existing_job_id IS NOT NULL THEN
    PERFORM cron.unschedule(existing_job_id);
  END IF;
END $$;

-- 4) Crear job diario a las 12:00 (formato cron: min hora día_mes mes día_semana)
--    Nota: la hora depende de la zona horaria del servidor/DB.
SELECT cron.schedule(
  'keepalive-daily',
  '0 12 * * *',
  $$ SELECT 1; $$
);

-- 5) Confirmar que quedó activo
SELECT jobid, jobname, schedule, command, active
FROM cron.job
WHERE jobname = 'keepalive-daily';

-- 6) (Opcional) Ver historial de ejecuciones (después de que corra al menos 1 vez)
-- SELECT *
-- FROM cron.job_run_details
-- WHERE jobid = (SELECT jobid FROM cron.job WHERE jobname='keepalive-daily')
-- ORDER BY start_time DESC
-- LIMIT 20;
