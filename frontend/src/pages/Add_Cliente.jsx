import React, { useState } from "react";
import { useOutletContext } from "react-router-dom";

export default function AñadirCliente() {
  const [tipoCliente, setTipoCliente] = useState("");
  const [estatus, setEstatus] = useState("");
  const [fechaIngreso, setFechaIngreso] = useState(
    new Date().toISOString().substring(0, 10)
  );

  // Obtener control del menú lateral desde el layout
  const { open, setOpen } = useOutletContext();

  return (
    <div className="min-h-screen bg-gray-100 text-gray-900">
      {/* HEADER */}
      <header className="bg-indigo-900 text-white px-6 py-4 flex items-center shadow">
        <button
          onClick={() => setOpen(!open)}
          className="p-2 bg-indigo-700 text-white rounded-lg shadow-md mr-4"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
        <h1 className="text-lg font-semibold">Administrador / Añadir Cliente</h1>
      </header>

      <main className="p-6 max-w-6xl mx-auto space-y-6">
        {/* GRID CONTENEDORA */}
        <div className="grid grid-cols-2 gap-6">
          {/* CUADRO 1: DATOS DEL CLIENTE */}
          <div className="bg-white rounded-xl shadow p-6">
            <div className="bg-indigo-700 text-white py-2 px-4 rounded-lg mb-4 font-semibold text-center">
              Datos del Cliente
            </div>
            {/* Nombre */}
            <label className="font-semibold text-sm">Nombre</label>
            <input
              className="w-full border rounded px-3 py-2 mb-3"
              placeholder="Nombre del cliente"
            />
            {/* Correo */}
            <label className="font-semibold text-sm">Correo electrónico</label>
            <input
              className="w-full border rounded px-3 py-2 mb-3"
              type="email"
              placeholder="cliente@correo.com"
            />
            {/* Teléfono + RFC */}
            <div className="grid grid-cols-2 gap-4 mb-3">
              <div>
                <label className="font-semibold text-sm">Teléfono</label>
                <input
                  className="w-full border rounded px-3 py-2"
                  placeholder="55-0000-0000"
                />
              </div>
              <div>
                <label className="font-semibold text-sm">RFC (Opcional)</label>
                <input
                  className="w-full border rounded px-3 py-2"
                  placeholder="RFC"
                />
              </div>
            </div>
            {/* Dirección */}
            <label className="font-semibold text-sm">Dirección</label>
            <input
              className="w-full border rounded px-3 py-2"
              placeholder="Calle #, Colonia, Alcaldía"
            />
          </div>

          {/* CUADRO 2: INFORMACIÓN ADICIONAL */}
          <div className="bg-white rounded-xl shadow p-6">
            <div className="bg-indigo-700 text-white py-2 px-4 rounded-lg mb-4 font-semibold text-center">
              Información Adicional
            </div>
            {/* Tipo de Cliente */}
            <h3 className="text-center font-semibold mb-2">Tipo de Cliente</h3>
            <div className="flex flex-col gap-2 mb-4">
              <label className="flex items-center gap-2">
                <input
                  type="radio"
                  name="tipoCliente"
                  value="Empresa"
                  checked={tipoCliente === "Empresa"}
                  onChange={(e) => setTipoCliente(e.target.value)}
                />
                Empresa
              </label>
              <label className="flex items-center gap-2">
                <input
                  type="radio"
                  name="tipoCliente"
                  value="Particular"
                  checked={tipoCliente === "Particular"}
                  onChange={(e) => setTipoCliente(e.target.value)}
                />
                Particular
              </label>
            </div>
            {/* Estatus */}
            <h3 className="text-center font-semibold mb-2">Estatus</h3>
            <div className="flex flex-col gap-2 mb-4">
              <label className="flex items-center gap-2">
                <input
                  type="radio"
                  name="estatus"
                  value="Activo"
                  checked={estatus === "Activo"}
                  onChange={(e) => setEstatus(e.target.value)}
                />
                Activo
              </label>
              <label className="flex items-center gap-2">
                <input
                  type="radio"
                  name="estatus"
                  value="Inactivo"
                  checked={estatus === "Inactivo"}
                  onChange={(e) => setEstatus(e.target.value)}
                />
                Inactivo
              </label>
            </div>
            {/* Fecha de ingreso */}
            <label className="font-semibold text-center block">Fecha de ingreso</label>
            <input
              type="date"
              className="w-full border rounded px-3 py-2"
              value={fechaIngreso}
              onChange={(e) => setFechaIngreso(e.target.value)}
            />
          </div>
        </div>

        {/* BOTONES FINALES */}
        <div className="flex justify-center gap-4 mt-4 text-center">
          <button className="bg-indigo-700 hover:bg-indigo-800 text-white px-6 py-2 rounded-lg shadow">
            Guardar Cliente
          </button>
          <button className="bg-gray-300 hover:bg-gray-400 text-gray-700 px-6 py-2 rounded-lg shadow">
            Cancelar
          </button>
        </div>
      </main>
    </div>
  );
}
