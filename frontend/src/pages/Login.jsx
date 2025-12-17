import React, { useState } from "react";
import { Eye, EyeOff } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [showPassword, setShowPassword] = useState(false);
  const [remember, setRemember] = useState(false);

  const navigate = useNavigate();

  const handleLogin = () => {
  // SIMULACIÓN DE ROL
  const rol = "admin"; 
  // valores posibles: admin | empleado | contador

  if (rol === "admin") {
    navigate("/dashboard");
  } else if (rol === "empleado") {
    navigate("/dashboard-empleado");
  } else if (rol === "contador") {
    navigate("/dashboard-contador");
  }
};


  return (
    <div className="min-h-screen bg-indigo-900 flex items-center justify-center p-4">
      <div className="bg-white w-full max-w-md rounded-2xl shadow-xl p-8 text-center">

        <h2 className="text-3xl font-bold text-indigo-900 mb-2">
          Bienvenido
        </h2>

        <p className="text-gray-500 mb-6">
          Ingresa con tu correo electrónico para continuar
        </p>

        {/* Correo */}
        <div className="mb-4 text-left">
          <label className="block font-medium text-gray-700 mb-1">
            Correo electrónico
          </label>
          <input
            type="email"
            className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-indigo-500 outline-none"
            placeholder="correo@ejemplo.com"
          />
        </div>

        {/* Contraseña */}
        <div className="mb-4 text-left">
          <label className="block font-medium text-gray-700 mb-1">
            Contraseña
          </label>
          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              className="w-full border rounded-lg px-3 py-2 pr-10 focus:ring-2 focus:ring-indigo-500 outline-none"
              placeholder="••••••••"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-2.5 text-gray-500 hover:text-gray-700"
            >
              {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </button>
          </div>
        </div>

        {/* Recordar + recuperar */}
        <div className="flex items-center justify-between mb-6 text-sm">
          <label className="flex items-center gap-2 text-gray-700">
            <input
              type="checkbox"
              checked={remember}
              onChange={() => setRemember(!remember)}
            />
            Recordarme
          </label>

          <button
            onClick={() => navigate("/restablecer")}
            className="text-indigo-700 hover:underline"
          >
            ¿Olvidaste tu contraseña?
          </button>
        </div>

        {/* Botón */}
        <button
          onClick={handleLogin}
          className="w-full bg-indigo-700 hover:bg-indigo-800 transition text-white font-semibold py-2 rounded-lg shadow-md"
        >
          Ingresar
        </button>

        <p className="text-xs text-gray-400 mt-6">
          Sea bienvenido al sistema de cotizaciones
        </p>
      </div>
    </div>
  );
}
