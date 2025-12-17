import React, { useState } from "react";
import { Eye, EyeOff, ArrowLeftCircle, Save } from "lucide-react";

const RecuperarContrasena = () => {
  const [newPass, setNewPass] = useState("");
  const [confirmPass, setConfirmPass] = useState("");

  const [showNew, setShowNew] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      
      {/* Contenedor principal */}
      <div className="bg-white w-full max-w-md rounded-2xl shadow-lg p-8">

        {/* Título */}
        <h2 className="text-2xl font-bold text-center text-blue-700 mb-1">
          Recuperar Contraseña
        </h2>

        <p className="text-center text-gray-600 mb-6">
          Ingresa tu nueva contraseña para continuar
        </p>

        {/* Nueva contraseña */}
        <label className="block font-medium mb-1">Nueva contraseña</label>
        <div className="relative mb-4">
          <input
            type={showNew ? "text" : "password"}
            value={newPass}
            onChange={(e) => setNewPass(e.target.value)}
            placeholder="Ingresa tu nueva contraseña"
            className="w-full border rounded-lg px-3 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            type="button"
            onClick={() => setShowNew(!showNew)}
            className="absolute right-3 top-2.5 text-gray-500 hover:text-gray-700"
          >
            {showNew ? <EyeOff size={20} /> : <Eye size={20} />}
          </button>
        </div>

        {/* Confirmar contraseña */}
        <label className="block font-medium mb-1">Confirmar contraseña</label>
        <div className="relative mb-6">
          <input
            type={showConfirm ? "text" : "password"}
            value={confirmPass}
            onChange={(e) => setConfirmPass(e.target.value)}
            placeholder="Confirma tu contraseña"
            className="w-full border rounded-lg px-3 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            type="button"
            onClick={() => setShowConfirm(!showConfirm)}
            className="absolute right-3 top-2.5 text-gray-500 hover:text-gray-700"
          >
            {showConfirm ? <EyeOff size={20} /> : <Eye size={20} />}
          </button>
        </div>

        {/* Botones */}
        <div className="flex flex-col gap-3">

          {/* Guardar nueva contraseña */}
          <button className="bg-blue-700 text-white py-2 rounded-lg flex items-center justify-center gap-2 hover:bg-blue-800 transition">
            <Save size={18} />
            Guardar nueva contraseña
          </button>

          {/* Volver al inicio */}
          <button className="bg-gray-200 text-gray-800 py-2 rounded-lg flex items-center justify-center gap-2 hover:bg-gray-300 transition">
            <ArrowLeftCircle size={20} />
            Volver al inicio de sesión
          </button>

        </div>
      </div>
    </div>
  );
};

export default RecuperarContrasena;
