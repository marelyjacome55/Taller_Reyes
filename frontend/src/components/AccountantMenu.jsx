import React from "react";
import { Link } from "react-router-dom";

export default function AccountantMenu({ open, setOpen }) {
  return (
    <>
      {open && (
        <div
          onClick={() => setOpen(false)}
          className="fixed inset-0 bg-black bg-opacity-40 z-40"
        />
      )}

      <aside
        className={`fixed top-0 left-0 h-full w-72 bg-emerald-900 text-white p-6 z-50
        transform transition-transform ${open ? "translate-x-0" : "-translate-x-full"}`}
      >
        <button
          onClick={() => setOpen(false)}
          className="absolute top-4 right-4 text-xl"
        >
          ✕
        </button>

        <div className="text-center mt-8 mb-6">
          <p className="text-xl font-semibold">Contador</p>
          <p className="text-emerald-200 text-sm">Vista financiera</p>
        </div>

        <nav className="flex flex-col gap-3">
          <Link to="/ver-cotizaciones" className="bg-emerald-700 py-2 rounded text-center">
            Ver cotizaciones
          </Link>

          <Link to="/ver-clientes" className="bg-emerald-700 py-2 rounded text-center">
            Ver clientes
          </Link>

          <Link to="/" className="bg-red-600 py-2 rounded text-center">
            Cerrar sesión
          </Link>
        </nav>
      </aside>
    </>
  );
}
