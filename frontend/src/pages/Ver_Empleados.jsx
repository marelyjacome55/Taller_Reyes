import React, { useState } from "react";
import AdminMenu from "../components/AdminMenu"; // Ajusta la ruta si es necesario
import { FaPlus, FaEdit, FaTrash } from "react-icons/fa";

const VerEmpleados = () => {
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const [filters, setFilters] = useState({
    area: "",
    puesto: "",
    estatus: "",
    fechaIngreso: "",
  });

  const [empleados, setEmpleados] = useState([
    {
      id: 1,
      nombre: "Juan Pérez",
      correo: "juan@example.com",
      telefono: "555-123-4567",
      puesto: "Cotizador",
      area: "Administración",
      fechaIngreso: "2023-03-10",
      estatus: "Activo",
    },
    {
      id: 2,
      nombre: "Carlos Hernández",
      correo: "carlos@example.com",
      telefono: "555-888-9999",
      puesto: "Gerente",
      area: "Taller",
      fechaIngreso: "2022-07-21",
      estatus: "Inactivo",
    },
  ]);

  // FILTRO
  const empleadosFiltrados = empleados.filter((e) => {
    const matchSearch = e.nombre.toLowerCase().includes(search.toLowerCase());
    const matchFilters =
      (filters.area === "" || e.area === filters.area) &&
      (filters.puesto === "" || e.puesto === filters.puesto) &&
      (filters.estatus === "" || e.estatus === filters.estatus) &&
      (filters.fechaIngreso === "" || e.fechaIngreso.startsWith(filters.fechaIngreso));
    return matchSearch && matchFilters;
  });

  // Cambiar estatus
  const toggleEstatus = (id) => {
    setEmpleados((prev) =>
      prev.map((e) =>
        e.id === id
          ? { ...e, estatus: e.estatus === "Activo" ? "Inactivo" : "Activo" }
          : e
      )
    );
  };

  // Funciones de botones (simulados)
  const handleEditar = (id) => alert(`Editar empleado ${id} (simulado)`);
  const handleEliminar = (id) => alert(`Eliminar empleado ${id} (simulado)`);

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
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>

          {/* Texto junto al menú */}
          <div className="flex flex-col">
            <div className="text-lg font-semibold">Administrador</div>
            <div className="text-sm text-blue-200">/ Ver Empleados</div>
          </div>
        </header>

        {/* BUSCADOR + BOTÓN AGREGAR */}
        <div className="flex items-center justify-between p-6">
          <div>
            <label className="block font-semibold mb-1">Buscar empleado</label>
            <input
              type="text"
              placeholder="Nombre del empleado..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="border border-gray-300 rounded px-3 py-2 w-72"
            />
          </div>

          <button className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded shadow flex items-center gap-2">
            <FaPlus /> Añadir empleado
          </button>
        </div>

        {/* FILTROS RÁPIDOS */}
        <p className="font-semibold mb-2 flex items-center gap-2 px-6">
          <span>⚡</span> Filtros rápidos
        </p>

        <div className="bg-white p-4 rounded shadow mb-6 flex gap-4 flex-wrap px-6">
          <select
            value={filters.area}
            onChange={(e) => setFilters({ ...filters, area: e.target.value })}
            className="border px-2 py-2 rounded"
          >
            <option value="">Área</option>
            <option value="Taller">Taller</option>
            <option value="Administración">Administración</option>
          </select>

          <select
            value={filters.puesto}
            onChange={(e) => setFilters({ ...filters, puesto: e.target.value })}
            className="border px-2 py-2 rounded"
          >
            <option value="">Puesto</option>
            <option value="Cotizador">Cotizador</option>
            <option value="Gerente">Gerente</option>
            <option value="Contador">Contador</option>
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
        <div className="bg-white p-4 rounded shadow overflow-auto px-6">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-gray-200 text-gray-700">
                <th className="p-3 border">Nombre completo</th>
                <th className="p-3 border">Correo electrónico</th>
                <th className="p-3 border">Teléfono</th>
                <th className="p-3 border">Puesto / Área</th>
                <th className="p-3 border">Fecha ingreso</th>
                <th className="p-3 border">Estatus</th>
                <th className="p-3 border text-center">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {empleadosFiltrados.length > 0 ? (
                empleadosFiltrados.map((e) => (
                  <tr key={e.id} className="hover:bg-gray-100">
                    <td className="p-3 border">{e.nombre}</td>
                    <td className="p-3 border">{e.correo}</td>
                    <td className="p-3 border">{e.telefono}</td>
                    <td className="p-3 border">{`${e.puesto} / ${e.area}`}</td>
                    <td className="p-3 border">{e.fechaIngreso}</td>
                    <td className="p-3 border">
                      <button
                        onClick={() => toggleEstatus(e.id)}
                        className={`font-semibold ${e.estatus === "Activo" ? "text-green-600" : "text-red-600"}`}
                      >
                        {e.estatus}
                      </button>
                    </td>
                    <td className="p-3 border text-center flex justify-center gap-2">
                      <button onClick={() => handleEditar(e.id)} className="text-blue-600 hover:underline flex items-center gap-1">
                        <FaEdit /> Editar
                      </button>
                      <button onClick={() => handleEliminar(e.id)} className="text-red-600 hover:underline flex items-center gap-1">
                        <FaTrash /> Eliminar
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" className="text-center p-4 text-gray-500">
                    No se encontraron empleados.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default VerEmpleados;
