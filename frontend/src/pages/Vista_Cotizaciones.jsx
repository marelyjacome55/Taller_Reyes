import { useState, useEffect, useRef } from "react";
import {
  Eye,
  Download,
  Pencil,
  Trash2,
  FilePlus,
  ClipboardList,
  UserPlus,
  Users,
  UserPlus2,
  UsersRound,
  LogOut,
  ChevronDown,
} from "lucide-react";

export default function CotizacionesAdmin() {
  const [search, setSearch] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef(null);

  // Cerrar menú al hacer clic fuera
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  // Datos simulados
  const cotizaciones = [
    { id: 1, cliente: "Empresa Alfa", fecha: "2025-11-01", total: "$12,500", estado: "Pendiente" },
    { id: 2, cliente: "GlobalSoft", fecha: "2025-11-02", total: "$8,200", estado: "Confirmada" },
    { id: 3, cliente: "TechSolutions", fecha: "2025-11-03", total: "$5,600", estado: "Rechazada" },
  ];

  const resultados = cotizaciones.filter((c) =>
    c.cliente.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-indigo-900 text-white py-5 px-8 shadow-md flex items-center justify-between">
        <div className="flex items-center gap-6 relative">
          {/* Menú desplegable */}
          <div className="relative" ref={menuRef}>
            <button
              onClick={() => setMenuOpen(!menuOpen)}
              className="flex items-center gap-2 bg-indigo-800 hover:bg-indigo-700 px-4 py-2 rounded-lg transition"
            >
              <span>Menú</span>
              <ChevronDown size={18} />
            </button>

            {menuOpen && (
              <div className="absolute left-0 mt-2 w-72 bg-white rounded-lg shadow-lg border border-gray-200 z-10 overflow-hidden">
                {/* Encabezado del menú */}
                <div className="bg-indigo-900 text-white text-center py-4">
                  <p className="text-sm uppercase font-semibold text-indigo-200">Administrador</p>
                  <img
                    src="https://cdn-icons-png.flaticon.com/512/149/149071.png"
                    alt="Foto de perfil"
                    className="w-20 h-20 rounded-full mx-auto my-2 border-2 border-white object-cover"
                  />
                  <p className="font-medium">Luis Martínez</p>
                </div>

                {/* Cuerpo del menú */}
                <div className="p-4 space-y-3 text-gray-700">
                  {/* Cotizaciones */}
                  <button className="w-full flex items-center justify-center gap-2 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md transition">
                    <FilePlus size={18} />
                    <span>Generar nueva cotización</span>
                  </button>

                  <button className="w-full flex items-center justify-center gap-2 bg-indigo-100 text-indigo-700 px-4 py-2 rounded-md">
                    <ClipboardList size={18} />
                    <span>Ver cotizaciones</span>
                  </button>

                  {/* Empleados */}
                  <p className="text-center text-xs text-gray-500 -mt-2">Empleados</p>
                  <div className="flex justify-center gap-2">
                    <button className="flex-1 flex items-center justify-center gap-2 bg-gray-100 hover:bg-gray-200 px-3 py-2 rounded-md transition">
                      <UserPlus size={16} className="text-indigo-600" />
                      <span className="text-sm">Añadir</span>
                    </button>
                    <button className="flex-1 flex items-center justify-center gap-2 bg-gray-100 hover:bg-gray-200 px-3 py-2 rounded-md transition">
                      <Users size={16} className="text-indigo-600" />
                      <span className="text-sm">Ver</span>
                    </button>
                  </div>
                  

                  {/* Clientes */}
                  <p className="text-center text-xs text-gray-500 -mt-2">Clientes</p>
                  <div className="flex justify-center gap-2">
                    <button className="flex-1 flex items-center justify-center gap-2 bg-gray-100 hover:bg-gray-200 px-3 py-2 rounded-md transition">
                      <UserPlus2 size={16} className="text-indigo-600" />
                      <span className="text-sm">Añadir</span>
                    </button>
                    <button className="flex-1 flex items-center justify-center gap-2 bg-gray-100 hover:bg-gray-200 px-3 py-2 rounded-md transition">
                      <UsersRound size={16} className="text-indigo-600" />
                      <span className="text-sm">Ver</span>
                    </button>
                  </div>
                  

                  {/* Cerrar sesión */}
                  <div className="border-t pt-3">
                    <button className="w-full flex items-center justify-center gap-2 text-red-600 hover:bg-red-50 px-4 py-2 rounded-md transition">
                      <LogOut size={18} />
                      <span>Cerrar sesión</span>
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Ruta */}
          <div>
            <h1 className="text-2xl font-semibold tracking-wide">Administrador</h1>
            <p className="text-sm text-indigo-200">/ Ver cotizaciones</p>
          </div>
        </div>
      </header>

      {/* Contenido principal */}
      <main className="p-8">
        {/* Buscador */}
        <div className="flex justify-between items-center mb-6">
          <input
            type="text"
            placeholder="Buscar cotización..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-1/3 px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>

        {/* Tabla */}
        <div className="bg-white shadow-md rounded-lg overflow-hidden">
          <table className="w-full border-collapse">
            <thead className="bg-gray-100 text-gray-700 text-left">
              <tr>
                <th className="py-3 px-4">No. Cotización</th>
                <th className="py-3 px-4">Cliente</th>
                <th className="py-3 px-4">Fecha</th>
                <th className="py-3 px-4">Total</th>
                <th className="py-3 px-4">Estado</th>
                <th className="py-3 px-4 text-center">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {resultados.map((c, index) => (
                <tr key={c.id} className="border-b hover:bg-gray-50 transition">
                  <td className="py-3 px-4 text-gray-800">{index + 1}</td>
                  <td className="py-3 px-4">{c.cliente}</td>
                  <td className="py-3 px-4">{c.fecha}</td>
                  <td className="py-3 px-4">{c.total}</td>
                  <td className="py-3 px-4">
                    <span
                      className={`px-2 py-1 text-sm rounded-md ${
                        c.estado === "Confirmada"
                          ? "bg-green-100 text-green-700"
                          : c.estado === "Rechazada"
                          ? "bg-red-100 text-red-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                    >
                      {c.estado}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center flex justify-center gap-4">
                    <button title="Ver detalles" className="text-blue-500 hover:text-blue-700 transition">
                      <Eye size={20} />
                    </button>
                    <button title="Descargar cotización" className="text-indigo-500 hover:text-indigo-700 transition">
                      <Download size={20} />
                    </button>
                    <button title="Editar cotización" className="text-green-500 hover:text-green-700 transition">
                      <Pencil size={20} />
                    </button>
                    <button title="Eliminar cotización" className="text-red-500 hover:text-red-700 transition">
                      <Trash2 size={20} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {resultados.length === 0 && (
            <div className="text-center py-6 text-gray-500">
              No se encontraron cotizaciones.
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
