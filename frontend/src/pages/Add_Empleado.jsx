import React, { useState } from "react";
import AdminMenu from "../components/AdminMenu"; // Ajusta la ruta si es necesario

const AgregarEmpleado = () => {
  const [open, setOpen] = useState(false);

  const [datosPersonales, setDatosPersonales] = useState({
    nombre: "",
    correo: "",
    telefono: "",
    fechaIngreso: "",
    direccion: "",
  });

  const [datosLaborales, setDatosLaborales] = useState({
    puesto: "",
    area: "",
    estatus: "",
  });

  const limpiarCampos = () => {
    setDatosPersonales({
      nombre: "",
      correo: "",
      telefono: "",
      fechaIngreso: "",
      direccion: "",
    });

    setDatosLaborales({
      puesto: "",
      area: "",
      estatus: "",
    });
  };

  const handleGuardar = () => {
    console.log("Datos personales:", datosPersonales);
    console.log("Datos laborales:", datosLaborales);

    alert("Empleado guardado (simulado)");
  };

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
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          </button>

          {/* Texto junto al botón */}
          <div className="flex flex-col">
            <div className="text-lg font-semibold">Administrador</div>
            <div className="text-sm text-blue-200">/ Añadir Empleado</div>
          </div>
        </header>

        {/* CONTENIDO */}
        <main className="p-8 flex flex-col items-center">
          {/* CONTENEDOR PRINCIPAL CENTRADO */}
          <div className="w-full max-w-6xl flex justify-between gap-8">

            {/* DATOS PERSONALES */}
            <div className="bg-white rounded-xl shadow p-6 w-1/2 border border-gray-200">
              <h2 className="text-xl font-semibold text-gray-700 mb-4 text-center">
                Datos Personales
              </h2>

              <div className="space-y-3">
                <div>
                  <label className="block font-medium text-gray-600">Nombre completo</label>
                  <input
                    type="text"
                    value={datosPersonales.nombre}
                    onChange={(e) =>
                      setDatosPersonales({ ...datosPersonales, nombre: e.target.value })
                    }
                    className="w-full border border-gray-300 rounded-lg p-2"
                  />
                </div>

                <div>
                  <label className="block font-medium text-gray-600">Correo electrónico</label>
                  <input
                    type="email"
                    value={datosPersonales.correo}
                    onChange={(e) =>
                      setDatosPersonales({ ...datosPersonales, correo: e.target.value })
                    }
                    className="w-full border border-gray-300 rounded-lg p-2"
                  />
                </div>

                <div>
                  <label className="block font-medium text-gray-600">Teléfono</label>
                  <input
                    type="text"
                    value={datosPersonales.telefono}
                    onChange={(e) =>
                      setDatosPersonales({ ...datosPersonales, telefono: e.target.value })
                    }
                    className="w-full border border-gray-300 rounded-lg p-2"
                  />
                </div>

                <div>
                  <label className="block font-medium text-gray-600">Fecha de ingreso</label>
                  <input
                    type="date"
                    value={datosPersonales.fechaIngreso}
                    onChange={(e) =>
                      setDatosPersonales({ ...datosPersonales, fechaIngreso: e.target.value })
                    }
                    className="w-full border border-gray-300 rounded-lg p-2"
                  />
                </div>

                <div>
                  <label className="block font-medium text-gray-600">Dirección</label>
                  <textarea
                    value={datosPersonales.direccion}
                    onChange={(e) =>
                      setDatosPersonales({ ...datosPersonales, direccion: e.target.value })
                    }
                    className="w-full border border-gray-300 rounded-lg p-2 h-20"
                  />
                </div>
              </div>
            </div>

            {/* DATOS LABORALES */}
            <div className="bg-white rounded-xl shadow p-6 w-1/2 border border-gray-200">
              <h2 className="text-xl font-semibold text-gray-700 mb-4 text-center">
                Datos Laborales
              </h2>

              {/* Puesto */}
              <h3 className="font-semibold text-gray-700 mt-2 text-center">Puesto</h3>
              <div className="flex flex-col gap-2 mt-2">
                {["Cotizador", "Gerente", "Contador"].map((op) => (
                  <label key={op} className="flex items-center gap-2">
                    <input
                      type="radio"
                      name="puesto"
                      value={op}
                      checked={datosLaborales.puesto === op}
                      onChange={(e) =>
                        setDatosLaborales({ ...datosLaborales, puesto: e.target.value })
                      }
                    />
                    {op}
                  </label>
                ))}
              </div>

              {/* Área */}
              <h3 className="font-semibold text-gray-700 mt-6 text-center">Área</h3>
              <div className="flex flex-col gap-2 mt-2">
                {["Taller", "Administración"].map((op) => (
                  <label key={op} className="flex items-center gap-2">
                    <input
                      type="radio"
                      name="area"
                      value={op}
                      checked={datosLaborales.area === op}
                      onChange={(e) =>
                        setDatosLaborales({ ...datosLaborales, area: e.target.value })
                      }
                    />
                    {op}
                  </label>
                ))}
              </div>

              {/* Estatus */}
              <h3 className="font-semibold text-gray-700 mt-6 text-center">Estatus</h3>
              <div className="flex flex-col gap-2 mt-2">
                {["Activo", "Inactivo"].map((op) => (
                  <label key={op} className="flex items-center gap-2">
                    <input
                      type="radio"
                      name="estatus"
                      value={op}
                      checked={datosLaborales.estatus === op}
                      onChange={(e) =>
                        setDatosLaborales({ ...datosLaborales, estatus: e.target.value })
                      }
                    />
                    {op}
                  </label>
                ))}
              </div>
            </div>

          </div>

          {/* BOTONES */}
          <div className="mt-8 flex justify-center gap-6">
            <button
              onClick={handleGuardar}
              className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg shadow font-medium"
            >
              Guardar empleado
            </button>

            <button
              onClick={limpiarCampos}
              className="bg-gray-400 hover:bg-gray-500 text-white px-6 py-3 rounded-lg font-medium"
            >
              Cancelar
            </button>
          </div>
        </main>
      </div>
    </div>
  );
};

export default AgregarEmpleado;
