# Setup rápido para las 4 capas

Resumen: frontend (React/Vite), backend (Spring Boot), scrapy (Python), database (Supabase/Postgres).

Variables de entorno importantes:

- Backend (Spring Boot):
  - SPRING_DATASOURCE_URL
  - SPRING_DATASOURCE_USERNAME
  - SPRING_DATASOURCE_PASSWORD

- Scrapy:
  - DATABASE_URL (recomendado) o DB_HOST/DB_PORT/DB_NAME/DB_USER/DB_PASSWORD

- Frontend (Vite):
  - VITE_API_URL (ej. https://mi-backend.example.com/api)

Instalación rápida (Linux):

- Frontend
  cd frontend
  npm install
  npm run dev

- Backend
  cd backend
  ./mvnw spring-boot:run

- Scrapy
  cd scrapy/scrapy_cotizador
  python -m venv .venv
  source .venv/bin/activate
  pip install -r requirements.txt
  # si usas Playwright
  playwright install

Base de datos:
- Aplica `database/schema.sql` en tu instancia de Postgres/Supabase.

Notas:
- `application.yaml.example` en `backend` muestra cómo usar variables de entorno para la conexión.
- No borres archivos originales de `ProyectoCotizaciones` hasta verificar que todo funciona; la carpeta `cotizaciones` fue movida aquí como `backend`.

