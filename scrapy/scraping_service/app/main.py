"""
🟦 CAPA PRESENTACIÓN - API HTTP
FastAPI Application - Punto de entrada principal
"""

"""F
astAPI → framework web (capa de presentación).
CORSMiddleware → permite que tu API acepte peticiones desde React o Spring Boot.
asynccontextmanager → maneja acciones de startup/shutdown.
os → para leer variables de entorno.
load_dotenv() → carga tus .env así no pones claves en el código.
routers → contiene tus endpoints /scrapy/buscar, etc.
init_db / close_db → inicializan y cierran el pool de conexiones a PostgreSQL.
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
import os
from dotenv import load_dotenv

from .routers import router
from ..infrastructure.db.connection import init_db, close_db

# Cargar variables de entorno
load_dotenv()

"""
Se ejecuta solo una vez cuando arranca FastAPI.
Crea el pool de conexiones a la DB (mucho más eficiente que conectar en cada request).
Al apagar el server, cierra conexiones correctamente.
"""
@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Gestión del ciclo de vida de la aplicación
    - Startup: Inicializa pool de conexiones DB
    - Shutdown: Cierra conexiones
    """
    # Startup
    await init_db()
    print("✅ Database pool initialized")
    yield
    # Shutdown
    await close_db()
    print("✅ Database connections closed")


# Crear aplicación FastAPI
app = FastAPI(
    title="Scrapy Cotizador API", #Nombre de la API (visible en Swagger /docs)
    description="Microservicio de scraping para refacciones automotrices", #Descripción
    version="1.0.0", #Versión de la API
    lifespan=lifespan # Gestión del ciclo de vida
)

# Configurar CORS
"""
Que React pueda consumir esta API sin errores de navegador.
Que Spring Boot también pueda interactuar con tu Scrapy API.
Que tu API esté lista para producción (controlando orígenes desde .env).
"""
allowed_origins = os.getenv("ALLOWED_ORIGINS", "http://localhost:3000").split(",")
app.add_middleware(
    CORSMiddleware,
    allow_origins=allowed_origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Incluir routers
app.include_router(router, prefix="/scrapy", tags=["scraping"])

"""
Endpoints básicos de salud y root
Sirve para saber si el microservicio está vivo.
Spring Boot puede usar este endpoint para monitoreo.
"""

@app.get("/")
async def root():
    """Health check endpoint"""
    return {
        "service": "Scrapy Cotizador",
        "status": "running",
        "version": "1.0.0"
    }

"""
Endpoint /health 
para monitoreo de salud del servicio
Ideal para Kubernetes, Azure, AWS, Docker healthchecks.
"""
@app.get("/health")
async def health_check():
    """Endpoint de salud para monitoreo"""
    return {"status": "healthy"}
