import React, { useState } from "react";
import { Mail, ArrowLeft, Send } from "lucide-react";

const RestablecerContraseña = () => {
  const [correo, setCorreo] = useState("");

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="bg-white w-full max-w-md rounded-xl shadow-lg p-8">

        {/* TÍTULO */}
        <h2 className="text-2xl font-bold text-blue-800 text-center mb-4">
          Restablecer contraseña
        </h2>

        {/* SUBTEXTOS */}
        <p className="text-gray-700 text-center mb-2">
          Ingresa el correo electrónico con el que fuiste registrado por el administrador.
        </p>
        <p className="text-gray-500 text-center mb-6">
          El sistema enviará un enlace para restablecer la contraseña.
        </p>

        {/* CORREO */}
        <label className="font-semibold text-gray-700">Correo electrónico</label>
        <div className="relative mt-1 mb-6">
          <input
            type="email"
            placeholder="ejemplo@correo.com"
            value={correo}
            onChange={(e) => setCorreo(e.target.value)}
            className="w-full border border-gray-300 rounded px-3 py-2 pr-10 outline-blue-500"
          />
          <Mail className="w-5 h-5 text-gray-400 absolute right-3 top-2.5" />
        </div>

        {/* BOTONES */}
        <div className="flex flex-col gap-3">

          {/* ENVIAR */}
          <button className="bg-blue-700 hover:bg-blue-800 text-white py-2 rounded flex items-center justify-center gap-2 shadow">
            <Send className="w-5 h-5" />
            Enviar enlace de recuperación
          </button>

          {/* VOLVER */}
          <button className="bg-gray-300 hover:bg-gray-400 text-gray-800 py-2 rounded flex items-center justify-center gap-2 shadow">
            <ArrowLeft className="w-5 h-5" />
            Volver al inicio de sesión
          </button>
        </div>
      </div>
    </div>
  );
};

export default RestablecerContraseña;
