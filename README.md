# Taller_Reyes

Resumen rápido (estado al 15 de diciembre de 2025)

- Repositorio reorganizado en capas: `frontend/`, `backend/`, `scrapy/`, `database/`.
- Dependencias instaladas localmente (Node, Maven, Python venv, Playwright) y proyectos preparados para ejecutar.
- Backend (`backend`) configurado para usar el pooler de Supabase por defecto via `SPRING_DATASOURCE_URL`.
- Conectividad: desde esta máquina se confirmó acceso TCP al pooler `aws-1-us-east-1.pooler.supabase.com:5432` (nc succeeded).

Qué se eliminó

- Se removió el virtualenv embebido en `scrapy/scrapy_cotizador/.venv` (no debe estar en el repo). Si necesitas recrearlo:

	```bash
	cd scrapy/scrapy_cotizador
	python -m venv .venv
	source .venv/bin/activate
	pip install -r requirements.txt
	python -m playwright install chromium
	```

Cómo arrancar cada capa (mínimo)

- Backend (usa pooler, variables de entorno):

	```bash
	export SPRING_DATASOURCE_URL="jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres?sslmode=require&connectTimeout=10&socketTimeout=30&prepareThreshold=0"
	export SPRING_DATASOURCE_USERNAME="postgres"
	export SPRING_DATASOURCE_PASSWORD="<TU_PASSWORD>"
	cd backend
	java -jar target/cotizaciones-0.0.1-SNAPSHOT.jar
	```

- Frontend (Vite):

	```bash
	cd frontend
	npm install
	npm run dev
	```

- Scrapy (requiere venv):

	```bash
	cd scrapy/scrapy_cotizador
	source .venv/bin/activate
	# Ejecuta spiders o el servicio según la documentación del paquete
	```

Estado final e indicaciones

- Estado actual: la aplicación backend puede conectar al pooler de Supabase desde esta máquina. Falta ejecutar el backend con las variables de entorno y verificar que JPA valide el esquema y que Scrapy escriba en la DB.
- Si alguna red sigue dando timeout desde otra ubicación, prueba con hotspot/vpn o despliega en una VM pública.

Si quieres, añado un apartado adicional con pruebas automáticas o un script `make dev` para arrancar todo en local.
