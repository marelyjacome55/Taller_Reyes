import React, { useState } from "react";
import { FaEye, FaDownload, FaEdit, FaTrash } from "react-icons/fa";
import AdminMenu from "../components/AdminMenu"; // Asegúrate de la ruta correcta

const VerCotizaciones = () => {
  const [open, setOpen] = useState(false);

  const cotizaciones = [
    { id: 1, cliente: "Juan Pérez", fecha: "2025-02-10", total: "$12,500.00", estado: "No Aprobada" },
    { id: 2, cliente: "Transportes AXA", fecha: "2025-02-12", total: "$8,900.00", estado: "Aprobada" },
  ];

  return (
    <div className="min-h-screen bg-gray-100 flex">
      {/* MENÚ LATERAL */}
      <AdminMenu open={open} setOpen={setOpen} />

      <div className="flex-1">
        {/* HEADER */}
        <header className="bg-blue-700 text-white px-6 py-4 flex items-center gap-4 shadow-md">
          {/* Botón hamburguesa */}
          <button
            onClick={() => setOpen(!open)}
            className="p-2 bg-blue-600 rounded-lg shadow-md"
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

          {/* Texto junto al botón */}
          <div className="flex flex-col">
            <div className="text-lg font-semibold">Administrador</div>
            <div className="text-sm text-indigo-200">/ Ver Cotizaciones</div>
          </div>
        </header>

        {/* CONTENIDO */}
        <div className="p-6">
          <div className="overflow-x-auto bg-white rounded-xl shadow-md p-4">
            <table className="min-w-full">
              <thead>
                <tr className="bg-gray-200 text-left text-sm text-gray-700">
                  <th className="p-3">No. Cotización</th>
                  <th className="p-3">Cliente</th>
                  <th className="p-3">Fecha</th>
                  <th className="p-3">Total</th>
                  <th className="p-3">Estado</th>
                  <th className="p-3 text-center w-32">Acciones</th>
                </tr>
              </thead>

              <tbody>
                {cotizaciones.map((coti) => (
                  <tr key={coti.id} className="border-t">
                    <td className="p-3">{coti.id}</td>
                    <td className="p-3">{coti.cliente}</td>
                    <td className="p-3">{coti.fecha}</td>
                    <td className="p-3">{coti.total}</td>
                    <td className="p-3">{coti.estado}</td>

                    <td className="p-2">
                      <div className="flex flex-col gap-3 items-center justify-center">
                        <button className="flex flex-col items-center text-blue-600 hover:text-blue-800">
                          <FaEye className="text-lg" />
                          <span className="text-xs">Ver</span>
                        </button>

                        <button className="flex flex-col items-center text-green-600 hover:text-green-800">
                          <FaDownload className="text-lg" />
                          <span className="text-xs">Descargar</span>
                        </button>

                        <button className="flex flex-col items-center text-yellow-600 hover:text-yellow-800">
                          <FaEdit className="text-lg" />
                          <span className="text-xs">Editar</span>
                        </button>

                        <button className="flex flex-col items-center text-red-600 hover:text-red-800">
                          <FaTrash className="text-lg" />
                          <span className="text-xs">Eliminar</span>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default VerCotizaciones;
