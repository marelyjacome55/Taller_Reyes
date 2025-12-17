import React, { useState } from "react";
import { Outlet } from "react-router-dom";
import AdminMenu from "../components/AdminMenu";

export default function AdminLayout() {
  const [open, setOpen] = useState(false);

  return (
    <div className="min-h-screen flex bg-gray-100 relative">
      {/* Menú lateral sobrepuesto */}
      <AdminMenu open={open} setOpen={setOpen} />

      {/* Contenido principal */}
      <div className="flex-1 transition-all">
        {/* Botón hamburguesa dentro del header existente */}
        <Outlet context={{ open, setOpen }} />
      </div>
    </div>
  );
}
