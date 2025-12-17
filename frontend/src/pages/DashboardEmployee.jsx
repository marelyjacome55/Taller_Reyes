import { useNavigate } from "react-router-dom";
import {
  FilePlus,
  ClipboardList,
  LogOut,
} from "lucide-react";

export default function DashboardEmployee() {
  const navigate = useNavigate();

  const backgroundColor = "#ECE7D1";

  return (
    <div
      className="min-h-screen flex flex-col items-center justify-between p-10"
      style={{ backgroundColor }}
    >
      {/* ===== TÍTULO ===== */}
      <h1 className="text-4xl font-bold text-orange-700 text-center drop-shadow-lg mb-8">
        Panel de Empleado
      </h1>

      {/* ===== OPCIONES ===== */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-8 w-full max-w-3xl">

        {/* Generar Cotización */}
        <button
          onClick={() => navigate("/cotizaciones")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <FilePlus size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">
            Generar Cotización
          </p>
        </button>

        {/* Ver Cotizaciones */}
        <button
          onClick={() => navigate("/ver-cotizaciones")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <ClipboardList size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">
            Ver Cotizaciones
          </p>
        </button>
      </div>

      {/* ===== CERRAR SESIÓN ===== */}
      <button
        onClick={() => navigate("/")}
        className="mt-10 bg-red-500 text-white px-8 py-3 rounded-xl shadow-lg flex items-center gap-3 hover:bg-red-600 transition"
      >
        <LogOut size={24} />
        Cerrar Sesión
      </button>
    </div>
  );
}
