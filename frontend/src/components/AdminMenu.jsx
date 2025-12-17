import React from "react";
import { Link } from "react-router-dom";

export default function AdminMenu({ open, setOpen }) {
  return (
    <>
      {/* Fondo oscuro semi-transparente solo cuando el menú está abierto */}
      {open && (
        <div
          onClick={() => setOpen(false)}
          className="fixed inset-0 bg-black bg-opacity-40 z-40"
        ></div>
      )}

      {/* Menú lateral sobrepuesto */}
      <aside
        className={`
          fixed top-0 left-0 h-full w-72 bg-indigo-900 text-white shadow-lg p-6
          transform transition-transform duration-300 z-50
          ${open ? "translate-x-0" : "-translate-x-full"}
        `}
      >
        {/* Botón cerrar */}
        <button
          onClick={() => setOpen(false)}
          className="absolute right-4 top-4 text-white text-xl"
        >
          ✕
        </button>

        {/* Sección superior: puesto, foto, nombre e ID */}
        <div className="flex flex-col items-center mt-8 mb-6">
          <p className="text-xl font-semibold text-center">Administrador</p>
          <img
            src="https://via.placeholder.com/150"
            className="rounded-full my-4 border-4 border-white shadow-lg"
          />
          <p className="text-white font-bold text-center text-lg">Nombre Empleado</p>
          <p className="text-indigo-200 text-sm text-center">ID_Empleado</p>
        </div>

        {/* Menú de navegación */}
        <nav className="flex flex-col items-center space-y-3">
          <Link to="/cotizaciones" className="w-3/4 text-center bg-indigo-700 px-4 py-2 rounded">
            Generar cotización
          </Link>

          <Link to="/ver-cotizaciones" className="w-3/4 text-center bg-indigo-700 px-4 py-2 rounded">
            Ver Cotizaciones
          </Link>

          <div className="grid grid-cols-2 gap-2 w-3/4">
            <Link to="/add-empleado" className="bg-indigo-700 px-2 py-2 rounded text-center">
              Añadir Empleado
            </Link>
            <Link to="/ver-empleados" className="bg-indigo-700 px-2 py-2 rounded text-center">
              Ver Empleados
            </Link>
          </div>

          <div className="grid grid-cols-2 gap-2 w-3/4">
            <Link to="/add-cliente" className="bg-indigo-700 px-2 py-2 rounded text-center">
              Añadir Cliente
            </Link>
            <Link to="/ver-clientes" className="bg-indigo-700 px-2 py-2 rounded text-center">
              Ver Clientes
            </Link>
          </div>

          <Link to="/" className="w-3/4 text-center bg-red-600 px-4 py-2 rounded">
            Cerrar sesión
          </Link>
        </nav>
      </aside>
    </>
  );
}
