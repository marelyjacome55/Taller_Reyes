import React, { useState } from "react";
import { Send, Minus, Maximize2, X, Paperclip, Trash2, Type } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function EnviarCorreo() {
  const [de, setDe] = useState("");
  const [para, setPara] = useState("");
  const [asunto, setAsunto] = useState("");
  const [mensaje, setMensaje] = useState("");

  const navigate = useNavigate();

  // ---- Cerrar ventana ----
  const handleClose = () => {
    navigate("/cotizaciones");
  };

  // ---- Eliminar correo ----
  const handleDelete = () => {
    const confirmDelete = window.confirm("¿Deseas eliminar este correo?");
    if (confirmDelete) {
      navigate("/cotizaciones");
    }
  };

  // ---- Enviar correo ----
  const handleSend = () => {
    alert("Correo enviado (simulado)");
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-6">
      <div className="w-full max-w-3xl bg-white rounded-xl shadow-lg border">

        {/* -------- CABECERA -------- */}
        <div className="flex justify-between items-center bg-indigo-700 text-white px-4 py-3 rounded-t-xl">
          <h2 className="text-lg font-semibold">Nuevo mensaje</h2>

          <div className="flex items-center gap-3">
           

            

            {/* Cerrar */}
            <button className="hover:text-red-300" onClick={handleClose}>
              <X size={18} />
            </button>
          </div>
        </div>

        {/* -------- CAMPOS DEL CORREO -------- */}
        <div className="p-5 space-y-4">

          {/* De */}
          <div>
            <label className="font-medium text-gray-700">De:</label>
            <input
              type="email"
              value={de}
              onChange={(e) => setDe(e.target.value)}
              placeholder="taller@example.com"
              className="w-full border rounded px-3 py-2 mt-1 text-gray-700 focus:outline-none focus:ring focus:ring-indigo-300"
            />
          </div>

          {/* Para */}
          <div>
            <label className="font-medium text-gray-700">Para:</label>
            <input
              type="email"
              value={para}
              onChange={(e) => setPara(e.target.value)}
              placeholder="cliente@example.com"
              className="w-full border rounded px-3 py-2 mt-1 text-gray-700 focus:outline-none focus:ring focus:ring-indigo-300"
            />
          </div>

          {/* Asunto */}
          <div>
            <label className="font-medium text-gray-700">Asunto:</label>
            <input
              type="text"
              value={asunto}
              onChange={(e) => setAsunto(e.target.value)}
              placeholder="Cotización solicitada"
              className="w-full border rounded px-3 py-2 mt-1 text-gray-700 focus:outline-none focus:ring focus:ring-indigo-300"
            />
          </div>

          {/* Redactar */}
          <div>
            <textarea
              value={mensaje}
              onChange={(e) => setMensaje(e.target.value)}
              placeholder="Redactar correo..."
              className="w-full border rounded px-3 py-2 h-48 text-gray-700 resize-none focus:outline-none focus:ring focus:ring-indigo-300"
            ></textarea>
          </div>
        </div>

        {/* -------- FOOTER DE ACCIONES -------- */}
        <div className="px-5 py-3 border-t flex justify-between items-center bg-gray-50 rounded-b-xl">

          {/* Botón Enviar */}
          <button
            onClick={handleSend}
            className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-5 py-2 rounded shadow"
          >
            <Send size={18} />
            Enviar
          </button>

          {/* Herramientas tipo Gmail */}
          <div className="flex items-center gap-5 text-gray-600">

            {/* Tipo de letra */}
            <button className="hover:text-indigo-600">
              <Type size={20} />
            </button>

            {/* Adjuntar archivo */}
            <button className="hover:text-indigo-600">
              <Paperclip size={20} />
            </button>

            {/* Eliminar */}
            <button className="hover:text-red-600" onClick={handleDelete}>
              <Trash2 size={20} />
            </button>

          </div>
        </div>
      </div>
    </div>
  );
}
