import React, { useState } from "react";
import AdminMenu from "../components/AdminMenu"; // Ajusta la ruta si es necesario

const VerClientes = () => {
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const [filters, setFilters] = useState({
    tipo: "",
    estatus: "",
    fechaIngreso: "",
  });
  const [clientes, setClientes] = useState([
    {
      id: 1,
      nombre: "Transportes Rivera S.A.",
      correo: "contacto@rivera.com",
      telefono: "555-123-4567",
      tipo: "Empresa",
      direccion: "Av. Central 123, CDMX",
      fechaIngreso: "2023-03-10",
      estatus: "Activo",
    },
    {
      id: 2,
      nombre: "Carlos Hernández",
      correo: "carlos@example.com",
      telefono: "555-888-9999",
      tipo: "Particular",
      direccion: "Calle Sur 45, CDMX",
      fechaIngreso: "2022-07-21",
      estatus: "Inactivo",
    },
  ]);

  // FILTRO
  const clientesFiltrados = clientes.filter((c) => {
    const matchSearch = c.nombre.toLowerCase().includes(search.toLowerCase());
    const matchFilters =
      (filters.tipo === "" || c.tipo === filters.tipo) &&
      (filters.estatus === "" || c.estatus === filters.estatus) &&
      (filters.fechaIngreso === "" || c.fechaIngreso.startsWith(filters.fechaIngreso));
    return matchSearch && matchFilters;
  });

  // Cambiar estatus (Activo ↔ Inactivo)
  const toggleEstatus = (id) => {
    setClientes((prev) =>
      prev.map((c) =>
        c.id === id
          ? { ...c, estatus: c.estatus === "Activo" ? "Inactivo" : "Activo" }
          : c
      )
    );
  };

  return (
    <div className="min-h-screen flex bg-gray-100">
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
            <div className="text-sm text-blue-200">/ Ver Clientes</div>
          </div>
        </header>

        {/* CONTENIDO */}
        <main className="p-6">

          {/* Buscar + Añadir cliente */}
          <div className="flex items-center justify-between mb-6">
            <div>
              <label className="block font-semibold mb-1">Buscar cliente</label>
              <input
                type="text"
                placeholder="Nombre del cliente..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="border border-gray-300 rounded px-3 py-2 w-72"
              />
            </div>

            <button className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded shadow">
              Añadir cliente
            </button>
          </div>

          {/* FILTROS RÁPIDOS */}
          <p className="font-semibold mb-2 flex items-center gap-2">
            <span>⚡</span> Filtros rápidos
          </p>

          <div className="bg-white p-4 rounded shadow mb-6 flex gap-4 flex-wrap">
            <select
              value={filters.tipo}
              onChange={(e) => setFilters({ ...filters, tipo: e.target.value })}
              className="border px-2 py-2 rounded"
            >
              <option value="">Tipo</option>
              <option value="Empresa">Empresa</option>
              <option value="Particular">Particular</option>
            </select>

            <select
              value={filters.estatus}
              onChange={(e) => setFilters({ ...filters, estatus: e.target.value })}
              className="border px-2 py-2 rounded"
            >
              <option value="">Estatus</option>
              <option value="Activo">Activo</option>
              <option value="Inactivo">Inactivo</option>
            </select>

            <input
              type="date"
              value={filters.fechaIngreso}
              onChange={(e) => setFilters({ ...filters, fechaIngreso: e.target.value })}
              className="border px-2 py-2 rounded"
            />
          </div>

          {/* TABLA */}
          <div className="bg-white p-4 rounded shadow overflow-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-200 text-gray-700">
                  <th className="p-3 border">Nombre</th>
                  <th className="p-3 border">Correo electrónico</th>
                  <th className="p-3 border">Teléfono</th>
                  <th className="p-3 border">Tipo</th>
                  <th className="p-3 border">Dirección</th>
                  <th className="p-3 border">Fecha ingreso</th>
                  <th className="p-3 border">Estatus</th>
                  <th className="p-3 border text-center">Acciones</th>
                </tr>
              </thead>

              <tbody>
                {clientesFiltrados.length > 0 ? (
                  clientesFiltrados.map((c) => (
                    <tr key={c.id} className="hover:bg-gray-100">
                      <td className="p-3 border">{c.nombre}</td>
                      <td className="p-3 border">{c.correo}</td>
                      <td className="p-3 border">{c.telefono}</td>
                      <td className="p-3 border">{c.tipo}</td>
                      <td className="p-3 border">{c.direccion}</td>
                      <td className="p-3 border">{c.fechaIngreso}</td>

                      <td className="p-3 border">
                        <button
                          onClick={() => toggleEstatus(c.id)}
                          className={`font-semibold ${
                            c.estatus === "Activo"
                              ? "text-green-600"
                              : "text-red-600"
                          }`}
                        >
                          {c.estatus}
                        </button>
                      </td>

                      <td className="p-3 border text-center">
                        <button className="text-blue-600 hover:underline">Editar</button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="8" className="text-center p-4 text-gray-500">
                      No se encontraron clientes.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

        </main>
      </div>
    </div>
  );
};

export default VerClientes;
