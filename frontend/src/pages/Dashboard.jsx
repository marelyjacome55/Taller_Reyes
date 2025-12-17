import { useNavigate } from "react-router-dom";
import {
  FilePlus,
  ClipboardList,
  UserPlus,
  Users,
  UserPlus2,
  UsersRound,
  LogOut,
} from "lucide-react";

export default function Dashboard() {
  const navigate = useNavigate();

  /** PALETA: Cambio de fondo **/
  const backgroundColor = "#ECE7D1"; // ← Color suave tipo arena (de la paleta)

  return (
    <div
      className="min-h-screen flex flex-col items-center justify-between p-10"
      style={{ backgroundColor }}
    >
      {/* ===== TÍTULO CENTRADO ===== */}
      <h1 className="text-4xl font-bold text-orange-700 text-center drop-shadow-lg mb-8">
        Panel de Control
      </h1>

      {/* ===== GRID DE OPCIONES ===== */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 w-full max-w-5xl">

        {/* Generar Cotización */}
        <button
          onClick={() => navigate("/cotizaciones")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <FilePlus size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">Generar Cotización</p>
        </button>

        {/* Ver Cotizaciones */}
        <button
          onClick={() => navigate("/ver-cotizaciones")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <ClipboardList size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">Ver Cotizaciones</p>
        </button>

        {/* Añadir Empleado */}
        <button
          onClick={() => navigate("/add-empleado")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <UserPlus size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">Añadir Empleado</p>
        </button>

        {/* Ver Empleados */}
        <button
          onClick={() => navigate("/ver-empleados")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <Users size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">Ver Empleados</p>
        </button>

        {/* Añadir Cliente */}
        <button
          onClick={() => navigate("/add-cliente")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <UserPlus2 size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">Añadir Cliente</p>
        </button>

        {/* Ver Clientes */}
        <button
          onClick={() => navigate("/ver-clientes")}
          className="bg-white p-6 rounded-2xl shadow-xl flex flex-col items-center gap-3 hover:scale-105 transition"
        >
          <UsersRound size={42} className="text-orange-600" />
          <p className="text-lg font-semibold text-gray-800">Ver Clientes</p>
        </button>
      </div>

      {/* ===== BOTÓN DE CERRAR SESIÓN (CENTRADO ABAJO) ===== */}
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
