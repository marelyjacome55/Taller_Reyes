# 🕷️ Scrapy Cotizador - Microservicio de Scraping

Microservicio Python que realiza scraping de precios de refacciones automotrices en múltiples plataformas de e-commerce.

## 🎯 Objetivo

Buscar productos automotrices (refacciones) en **Mercado Libre**, **Amazon** y **Autozone** según parámetros específicos y retornar los resultados ordenados por menor precio.

## 🏗️ Arquitectura (4 Capas)

```
┌─────────────────────────────────────────┐
│  PRESENTACIÓN (app/)                    │  FastAPI - Endpoints HTTP
├─────────────────────────────────────────┤
│  APLICACIÓN (application/)              │  Casos de uso, orquestación
├─────────────────────────────────────────┤
│  DOMINIO (domain/)                      │  Entidades, lógica de negocio
├─────────────────────────────────────────┤
│  INFRAESTRUCTURA (infrastructure/)      │  Scrapy, PostgreSQL, HTTP
└─────────────────────────────────────────┘
```

## 📋 Parámetros de Búsqueda

- **nombre_pieza** (obligatorio): Nombre de la refacción (ej: "bujia NGK")
- **marca** (opcional): Marca del vehículo (ej: "Honda")
- **modelo** (opcional): Modelo del vehículo (ej: "Civic")
- **anio** (opcional): Año del vehículo (ej: "2015")
- **version** (opcional): Versión del modelo (ej: "EX")
- **mecanica** (opcional): Tipo de mecánica (ej: "Manual")

## 🚀 Instalación

```bash
# Crear entorno virtual
python -m venv venv
source venv/bin/activate  # Linux/Mac
.\venv\Scripts\activate   # Windows

# Instalar dependencias
pip install -r requirements.txt

# Configurar variables de entorno
cp .env.example .env
# Editar .env con tus credenciales de PostgreSQL/Supabase
```

## ▶️ Ejecución

```bash
# Modo desarrollo
uvicorn scraping_service.app.main:app --reload --port 8000

# Modo producción
uvicorn scraping_service.app.main:app --host 0.0.0.0 --port 8000
```

## 📡 API Endpoints

### POST `/scrapy/buscar`

Busca refacciones en todas las plataformas.

**Request:**
```json
{
  "nombre_pieza": "bujia NGK",
  "marca": "Honda",
  "modelo": "Civic",
  "anio": 2015,
  "version": "EX",
  "mecanica": "Manual"
}
```

**Response:**
```json
{
  "success": true,
  "total_results": 15,
  "search_params": {
    "nombre_pieza": "bujia NGK",
    "marca": "Honda",
    "modelo": "Civic",
    "anio": 2015
  },
  "results": [
    {
      "titulo": "Bujia NGK Original Honda Civic 2015",
      "precio": 120.00,
      "url": "https://mercadolibre.com.mx/...",
      "fuente": "MERCADO_LIBRE",
      "metadata": {
        "shipping": "Gratis",
        "rating": 4.8,
        "vendidos": 150
      }
    }
  ]
}
```

## 🗄️ Integración con Base de Datos

### Tabla `cache_scrapy`
Almacena resultados de búsqueda con TTL (24-48 horas).

### Tabla `resultados_scrapy`
Almacena productos individuales normalizados.

## 🧪 Testing

```bash
# Ejecutar tests
pytest scraping_service/tests/

# Con cobertura
pytest --cov=scraping_service scraping_service/tests/
```

## 📦 Estructura del Proyecto

Ver documentación completa en `/docs/scrapy_arquitectura.md`

## 🔗 Integración con Sistema

```
React → Spring Boot → Python Scrapy → PostgreSQL/Supabase
```

## 📝 Notas de Desarrollo

- **Cache:** Los resultados se cachean por 24 horas por defecto
- **Rate Limiting:** Respeta `DOWNLOAD_DELAY` en `settings.py`
- **User-Agent:** Rotación automática para evitar bloqueos
- **Proxy:** Configurable en `middlewares.py`

## 🛠️ Stack Tecnológico

- **FastAPI**: Framework web asíncrono
- **Scrapy**: Framework de scraping
- **PostgreSQL/Supabase**: Base de datos
- **Pydantic**: Validación de datos
- **asyncpg**: Cliente PostgreSQL asíncrono
