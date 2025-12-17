import React, { useState, useEffect } from "react";
import { useOutletContext, Link } from "react-router-dom"; // <-- agregué Link
import { Trash2, Search } from "lucide-react";

export default function Cotizaciones() {
  const { open, setOpen } = useOutletContext(); // <-- Obtener control del menú lateral

  /* -------------------- PIEZAS PRINCIPALES -------------------- */
  const [piezas, setPiezas] = useState([
    {
      id: 1,
      nombre: "Filtro de aceite",
      cantidad: 1,
      unidad: "pieza",
      precio: 250,
      descuento: 0,
      iva: 0.16,
      retIva: 0.04,
    },
    {
      id: 2,
      nombre: "Aceite sintético",
      cantidad: 2,
      unidad: "litro",
      precio: 180,
      descuento: 5,
      iva: 0.16,
      retIva: 0.04,
    },
  ]);

  /* -------------------- SERVICIOS (NUEVO) -------------------- */
  const [servicios, setServicios] = useState([
    { id: Date.now() + 1, nombre: "", descripcion: "", monto: 0 },
    { id: Date.now() + 2, nombre: "", descripcion: "", monto: 0 },
    { id: Date.now() + 3, nombre: "", descripcion: "", monto: 0 },
  ]);

  const agregarServicio = () => {
    setServicios((prev) => [
      ...prev,
      { id: Date.now(), nombre: "", descripcion: "", monto: 0 },
    ]);
  };

  const actualizarServicio = (id, campo, valor) => {
    setServicios((prev) =>
      prev.map((s) =>
        s.id === id ? { ...s, [campo]: campo === "monto" ? Number(valor) : valor } : s
      )
    );
  };

  const eliminarServicio = (id) => {
    setServicios((prev) => prev.filter((s) => s.id !== id));
  };

  const totalManoObra = servicios.reduce((sum, s) => sum + Number(s.monto || 0), 0);

  /* -------------------- SCRAPY SIMULADO -------------------- */
  const sitios = ["Mercado Libre", "AutoZone", "Refaccionaria Napa", "Amazon"];
  const [site, setSite] = useState(sitios[0]);
  const [queryRef, setQueryRef] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  // sampleCatalog extendido con metadatos para filtrar
  const sampleCatalog = [
    {
      pieza: "Filtro de aceite",
      precio: 150,
      de: "AutoZone",
      marca: "Toyota",
      modelo: "Corolla",
      ano: "2018",
      version: "Base",
      mecanica: "Automática",
    },
    {
      pieza: "Bujía NGK",
      precio: 120,
      de: "Mercado Libre",
      marca: "Honda",
      modelo: "Civic",
      ano: "2016",
      version: "Sport",
      mecanica: "Manual",
    },
    {
      pieza: "Balata delantera",
      precio: 700,
      de: "Refaccionaria Napa",
      marca: "Ford",
      modelo: "F-150",
      ano: "2020",
      version: "Platinum",
      mecanica: "Automática",
    },
    {
      pieza: "Alternador genérico",
      precio: 2200,
      de: "Amazon",
      marca: "Nissan",
      modelo: "Sentra",
      ano: "2019",
      version: "SR",
      mecanica: "Manual",
    },
  ];

  // opciones para los selects (puedes sustituir por datos reales)
  const marcas = ["", "Toyota", "Honda", "Ford", "Nissan"];
  const modelos = ["", "Corolla", "Civic", "F-150", "Sentra"];
  const anos = ["", "2016", "2018", "2019", "2020"];
  const versiones = ["", "Base", "Sport", "Platinum", "SR"];
  const mecanicas = ["", "Manual", "Automática"];

  // filtros tipo Mercado Libre (marca, modelo, año, versión, mecánica)
  const [marca, setMarca] = useState("");
  const [modelo, setModelo] = useState("");
  const [ano, setAno] = useState("");
  const [version, setVersion] = useState("");
  const [mecanica, setMecanica] = useState("");

  const handleBuscar = () => {
    const q = queryRef.trim().toLowerCase();

    const results = sampleCatalog
      .filter((r) => {
        // búsqueda por texto en nombre de pieza
        const matchesQuery = q ? r.pieza.toLowerCase().includes(q) : true;

        // sitio
        const matchesSite = site ? r.de === site : true;

        // filtros (si están especificados)
        const matchesMarca = marca ? r.marca === marca : true;
        const matchesModelo = modelo ? r.modelo === modelo : true;
        const matchesAno = ano ? r.ano === ano : true;
        const matchesVersion = version ? r.version === version : true;
        const matchesMecanica = mecanica ? r.mecanica === mecanica : true;

        return (
          matchesQuery &&
          matchesSite &&
          matchesMarca &&
          matchesModelo &&
          matchesAno &&
          matchesVersion &&
          matchesMecanica
        );
      })
      .map((r, idx) => ({ id: Date.now() + idx, ...r }));

    setSearchResults(results);

    // console para debug (opcional)
    console.log("Buscar Refacciones - filtros:", {
      marca,
      modelo,
      ano,
      version: version || "(opcional vacío)",
      mecanica: mecanica || "(opcional vacío)",
    });
  };

  const handleSeleccionar = (res) => {
    const nueva = {
      id: Date.now(),
      nombre: res.pieza,
      cantidad: 1,
      unidad: "pieza",
      precio: res.precio,
      descuento: 0,
      iva: 0.16,
      retIva: 0.04,
    };
    setPiezas((prev) => [...prev, nueva]);
    setQueryRef("");
    setSearchResults([]);
  };

  /* -------------------- ELIMINAR PIEZA -------------------- */
  const eliminarPieza = (id) => {
    setPiezas((prev) => prev.filter((p) => p.id !== id));
  };

  /* -------------------- ACTUALIZAR CAMPOS -------------------- */
  const actualizarCampo = (id, campo, valor) => {
    setPiezas((prev) =>
      prev.map((p) =>
        p.id === id ? { ...p, [campo]: Number(valor) || 0 } : p
      )
    );
  };

  /* -------------------- FECHA AUTOMÁTICA -------------------- */
  const [fechaActual, setFechaActual] = useState("");

  useEffect(() => {
    const hoy = new Date();
    const f =
      hoy.getFullYear() +
      "-" +
      String(hoy.getMonth() + 1).padStart(2, "0") +
      "-" +
      String(hoy.getDate()).padStart(2, "0");
    setFechaActual(f);
  }, []);

  /* -------------------- TOTALES -------------------- */
  const calcularTotales = () => {
    let importe = 0;
    let descuentoTotal = 0;
    let subtotal = 0;
    let ivaTotal = 0;
    let retIvaTotal = 0;

    piezas.forEach((p) => {
      const base = p.precio * p.cantidad;
      const desc = (base * p.descuento) / 100;
      const baseGrav = base - desc;
      const iva = baseGrav * p.iva;
      const ret = baseGrav * p.retIva;

      importe += base;
      descuentoTotal += desc;
      subtotal += baseGrav;
      ivaTotal += iva;
      retIvaTotal += ret;
    });

    const total = subtotal + ivaTotal - retIvaTotal + totalManoObra;

    return { importe, descuentoTotal, subtotal, ivaTotal, retIvaTotal, total };
  };

  const totales = calcularTotales();

  /* -------------------- RENDER -------------------- */
  return (
    <div className="min-h-screen bg-gray-100 text-gray-800">
      {/* HEADER */}
      <header className="bg-indigo-900 text-white px-6 py-4 flex items-center shadow gap-4">
        {/* Botón hamburguesa */}
        <button
          onClick={() => setOpen(!open)}
          className="p-2 bg-indigo-700 text-white rounded-lg shadow-md"
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

        {/* Texto al lado del botón */}
        <div className="flex flex-col">
          <div className="text-lg font-semibold">Administrador</div>
          <div className="text-sm text-indigo-200">/ Generar Cotizaciones</div>
        </div>
      </header>

      <main className="p-6 max-w-7xl mx-auto space-y-6">
        {/* -------------------- LOGO + HONORARIOS + RECEPTOR -------------------- */}
        <div className="grid grid-cols-3 gap-6 items-start">
          {/* LOGO */}
          <div className="flex items-center justify-center bg-white border rounded-lg shadow p-4">
            <img
              src="https://via.placeholder.com/160x60?text=LOGO"
              alt="logo"
              className="max-h-16 object-contain"
            />
          </div>

          {/* HONORARIOS (CFDI removido y reorganizado) */}
          <div className="bg-white border rounded-lg shadow p-4 h-full">
            <h3 className="font-semibold mb-3">Honorarios</h3>

            {/* ahora en 2 columnas para evitar el hueco */}
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-sm block mb-1">Método de pago</label>
                <select className="w-full border rounded px-2 py-1">
                  <option>Transferencia</option>
                  <option>Efectivo</option>
                </select>
              </div>

              <div>
                <label className="text-sm block mb-1">Forma de pago</label>
                <select className="w-full border rounded px-2 py-1">
                  <option>Contado</option>
                  <option>Crédito</option>
                </select>
              </div>

              <div>
                <label className="text-sm block mb-1">Fecha</label>
                <input
                  type="date"
                  value={fechaActual}
                  readOnly
                  className="w-full border rounded px-2 py-1 bg-gray-100"
                />
              </div>

              <div>
                <label className="text-sm block mb-1">Folio</label>
                <input className="w-full border rounded px-2 py-1" />
              </div>

              <div>
                <label className="text-sm block mb-1">Condición de pago</label>
                <input className="w-full border rounded px-2 py-1" />
              </div>

              {/* espacio aprovechado si en el futuro añades otro campo */}
              <div />
            </div>

            <div className="mt-3 grid grid-cols-3 gap-2 text-sm">
              <label className="flex items-center gap-2">
                <input type="checkbox" /> Divisa
              </label>
              <label className="flex items-center gap-2">
                <input type="checkbox" /> TC
              </label>
              <label className="flex items-center gap-2">
                <input type="checkbox" /> Serv par cost
              </label>
            </div>
          </div>

          {/* RECEPTOR */}
          <div className="bg-white border rounded-lg shadow p-4 h-full">
            <h3 className="font-semibold mb-3">Receptor</h3>
            <label className="text-sm block mb-1">Buscar cliente</label>
            <div className="relative mb-3">
              <input
                className="w-full border rounded px-2 py-1 pr-9"
                placeholder="Escribe nombre del cliente..."
              />
              <Search className="absolute right-2 top-2 text-gray-400 w-4 h-4" />
            </div>

            <div className="space-y-2">
              <input className="w-full border rounded px-2 py-1" placeholder="Nombre" />
              <input className="w-full border rounded px-2 py-1" placeholder="Teléfono" />
              <input className="w-full border rounded px-2 py-1" placeholder="Correo" />
              <input className="w-full border rounded px-2 py-1" placeholder="Dirección" />
            </div>
          </div>
        </div>

        {/* -------------------- ENCABEZADO + SERVICIOS -------------------- */}
        <div className="grid grid-cols-2 gap-6">
          {/* Encabezado */}
          <div className="bg-white border rounded-lg shadow p-4">
            <h4 className="font-semibold mb-2">Encabezado</h4>
            <textarea className="w-full border rounded p-2 h-28 resize-none" />
          </div>

          {/* SERVICIOS (NUEVA TABLA) */}
          <div className="bg-white border rounded-lg shadow p-4">
            <h4 className="font-semibold mb-3">Servicios (Mano de obra)</h4>

            <table className="w-full text-sm border-collapse">
              <thead className="bg-indigo-100">
                <tr>
                  <th className="border p-2">Nombre</th>
                  <th className="border p-2">Descripción</th>
                  <th className="border p-2">Monto</th>
                  <th className="border p-2">Acción</th>
                </tr>
              </thead>

              <tbody>
                {servicios.map((s) => (
                  <tr key={s.id} className="hover:bg-gray-50">
                    <td className="border p-2">
                      <input
                        className="w-full border rounded px-2 py-1"
                        value={s.nombre}
                        onChange={(e) => actualizarServicio(s.id, "nombre", e.target.value)}
                      />
                    </td>

                    <td className="border p-2">
                      <input
                        className="w-full border rounded px-2 py-1"
                        value={s.descripcion}
                        onChange={(e) =>
                          actualizarServicio(s.id, "descripcion", e.target.value)
                        }
                      />
                    </td>

                    <td className="border p-2">
                      <input
                        type="number"
                        className="w-full border rounded px-2 py-1 text-right"
                        value={s.monto}
                        onChange={(e) => actualizarServicio(s.id, "monto", e.target.value)}
                      />
                    </td>

                    <td className="border p-2 text-center">
                      <button
                        onClick={() => eliminarServicio(s.id)}
                        className="text-red-600 font-bold"
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            <button
              className="mt-3 bg-green-600 hover:bg-green-700 text-white py-1 px-3 rounded"
              onClick={agregarServicio}
            >
              + Agregar servicio
            </button>
          </div>
        </div>

        {/* -------------------- TABLA PRINCIPAL + SCRAPY + TOTALES -------------------- */}
        <div className="grid grid-cols-3 gap-6">
          {/* TABLA PRINCIPAL */}
          <div className="col-span-2 bg-white border rounded-lg shadow p-4 overflow-x-auto">
            <table className="w-full text-sm border-collapse">
              <thead className="bg-indigo-100">
                <tr>
                  <th className="p-2 border">Acción</th>
                  <th className="p-2 border">Cantidad</th>
                  <th className="p-2 border">Unidad</th>
                  <th className="p-2 border">Nombre</th>
                  <th className="p-2 border">Precio unitario</th>
                  <th className="p-2 border">Descuento (%)</th>
                  <th className="p-2 border">IVA</th>
                  <th className="p-2 border">Ret. IVA</th>
                  <th className="p-2 border">Subtotal</th>
                  <th className="p-2 border">Total impuestos</th>
                </tr>
              </thead>

              <tbody>
                {piezas.length === 0 ? (
                  <tr>
                    <td colSpan="10" className="text-center p-6 text-gray-400">
                      No hay partidas
                    </td>
                  </tr>
                ) : (
                  piezas.map((p) => {
                    const base = p.precio * p.cantidad;
                    const descuento = (base * p.descuento) / 100;
                    const baseGrav = base - descuento;
                    const iva = baseGrav * p.iva;
                    const ret = baseGrav * p.retIva;
                    const subtotal = baseGrav;
                    const impuestos = iva - ret;

                    return (
                      <tr key={p.id} className="hover:bg-gray-50">
                        <td className="p-2 border text-center">
                          <button
                            onClick={() => eliminarPieza(p.id)}
                            className="text-red-600"
                          >
                            Eliminar
                          </button>
                        </td>

                        <td className="p-2 border text-center">
                          <input
                            type="number"
                            min="1"
                            value={p.cantidad}
                            onChange={(e) =>
                              actualizarCampo(p.id, "cantidad", e.target.value)
                            }
                            className="w-16 border rounded px-1 text-center"
                          />
                        </td>

                        <td className="p-2 border text-center">{p.unidad}</td>
                        <td className="p-2 border">{p.nombre}</td>

                        <td className="p-2 border text-right">
                          ${p.precio.toFixed(2)}
                        </td>

                        <td className="p-2 border text-center">
                          <input
                            type="number"
                            min="0"
                            max="100"
                            value={p.descuento}
                            onChange={(e) =>
                              actualizarCampo(p.id, "descuento", e.target.value)
                            }
                            className="w-16 border rounded px-1 text-center"
                          />
                        </td>

                        <td className="p-2 border text-center">
                          {(p.iva * 100).toFixed(0)}%
                        </td>

                        <td className="p-2 border text-center">
                          {(p.retIva * 100).toFixed(0)}%
                        </td>

                        <td className="p-2 border text-right">
                          ${subtotal.toFixed(2)}
                        </td>

                        <td className="p-2 border text-right">
                          ${impuestos.toFixed(2)}
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>

          {/* SCRAPY + TOTALES */}
          <div className="flex flex-col gap-6">
            {/* SCRAPY */}
            <div className="bg-white border rounded-lg shadow p-4">
              <h4 className="font-semibold mb-2">Buscar Refacciones</h4>

              <label className="text-sm mb-1 block">Pieza a buscar</label>
              <input
                value={queryRef}
                onChange={(e) => setQueryRef(e.target.value)}
                className="w-full border rounded px-2 py-1 mb-2"
                placeholder="Escribe nombre de la pieza..."
              />

              {/* FILTROS TIPO MERCADO LIBRE (dropdowns) */}
              {/* FILTROS DE REFACCIONES */}
<div className="mt-4 bg-white p-4 rounded-lg shadow-md">

  <h2 className="font-bold mb-3 text-gray-700">Filtros de búsqueda</h2>

  <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

    {/* Marca */}
    <div>
      <label className="font-semibold">Marca</label>
      <input
        list="marca-options"
        className="w-full p-2 border rounded mt-1"
        placeholder="Selecciona o escribe..."
      />
      <datalist id="marca-options">
        <option value="Nissan" />
        <option value="Toyota" />
        <option value="Honda" />
        <option value="Mazda" />
        <option value="Ford" />
      </datalist>
    </div>

    {/* Modelo */}
    <div>
      <label className="font-semibold">Modelo</label>
      <input
        list="modelo-options"
        className="w-full p-2 border rounded mt-1"
        placeholder="Selecciona o escribe..."
      />
      <datalist id="modelo-options">
        <option value="Sentra" />
        <option value="Civic" />
        <option value="Corolla" />
        <option value="CX-5" />
        <option value="F-150" />
      </datalist>
    </div>

    {/* Año */}
    <div>
      <label className="font-semibold">Año</label>
      <input
        list="anio-options"
        className="w-full p-2 border rounded mt-1"
        placeholder="Selecciona o escribe..."
      />
      <datalist id="anio-options">
        {Array.from({ length: 25 }, (_, i) => {
          const year = 2025 - i;
          return <option key={year} value={year} />;
        })}
      </datalist>
    </div>

    {/* Versión */}
    <div>
      <label className="font-semibold">Versión (opcional)</label>
      <input
        list="version-options"
        className="w-full p-2 border rounded mt-1"
        placeholder="Selecciona o escribe..."
      />
      <datalist id="version-options">
        <option value="Base" />
        <option value="Sport" />
        <option value="Premium" />
        <option value="XL" />
        <option value="Especial" />
      </datalist>
    </div>

    {/* Mecánica */}
    <div>
      <label className="font-semibold">Motor (opcional)</label>
      <input
        list="mecanica-options"
        className="w-full p-2 border rounded mt-1"
        placeholder="Selecciona o escribe..."
      />
      <datalist id="mecanica-options">
        <option value="Automático" />
        <option value="Manual" />
        <option value="Tiptronic" />
        <option value="CVT" />
      </datalist>
    </div>

  </div>
</div>


              <label className="text-sm mb-1 block">Dónde buscar</label>
              <select
                className="w-full border rounded px-2 py-1 mb-3"
                value={site}
                onChange={(e) => setSite(e.target.value)}
              >
                {sitios.map((s) => (
                  <option key={s} value={s}>
                    {s}
                  </option>
                ))}
              </select>

              <div className="flex gap-2 mb-3">
                <button
                  onClick={handleBuscar}
                  className="flex-1 bg-indigo-600 text-white py-2 rounded"
                >
                  Buscar
                </button>
                <button
                  onClick={() => {
                    setQueryRef("");
                    setSearchResults([]);
                    // no limpiamos filtros por defecto (según tu petición previa)
                  }}
                  className="bg-gray-200 py-2 px-3 rounded"
                >
                  Limpiar
                </button>
              </div>

              <h5 className="font-medium mb-2">Resultados</h5>
              <div className="max-h-48 overflow-auto border rounded">
                {searchResults.length === 0 ? (
                  <div className="p-3 text-sm text-gray-500">No hay resultados</div>
                ) : (
                  searchResults.map((r) => (
                    <div
                      key={r.id}
                      className="p-2 border-b flex justify-between items-center"
                    >
                      <div>
                        <div className="font-medium">{r.pieza}</div>
                        <div className="text-xs text-gray-500">
                          {r.de} — {r.marca} {r.modelo} {r.ano}
                        </div>
                      </div>

                      <div className="text-right">
                        <div className="font-semibold mb-1">
                          ${r.precio.toFixed(2)}
                        </div>
                        <button
                          onClick={() => handleSeleccionar(r)}
                          className="bg-green-600 text-white px-3 py-1 rounded text-sm"
                        >
                          Seleccionar
                        </button>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </div>

            {/* TOTALES */}
            <div className="bg-white border rounded-lg shadow p-4 space-y-3">
              <h4 className="font-semibold">Totales</h4>

              <div className="text-sm space-y-1">
                <div className="flex justify-between">
                  <span>Importe</span>
                  <strong>${totales.importe.toFixed(2)}</strong>
                </div>

                <div className="flex justify-between">
                  <span>Descuento</span>
                  <strong>${totales.descuentoTotal.toFixed(2)}</strong>
                </div>

                <div className="flex justify-between">
                  <span>Subtotal</span>
                  <strong>${totales.subtotal.toFixed(2)}</strong>
                </div>

                <div className="flex justify-between">
                  <span>IVA</span>
                  <strong>${totales.ivaTotal.toFixed(2)}</strong>
                </div>

                <div className="flex justify-between">
                  <span>Ret. IVA</span>
                  <strong>${totales.retIvaTotal.toFixed(2)}</strong>
                </div>

                <div className="flex justify-between text-indigo-700 font-bold border-t pt-2">
                  <span>Total Mano de Obra</span>
                  <span>${totalManoObra.toFixed(2)}</span>
                </div>

                <div className="flex justify-between font-bold text-indigo-900 border-t pt-2 text-lg">
                  <span>Gran Total</span>
                  <span>${totales.total.toFixed(2)}</span>
                </div>
              </div>

              <div className="flex flex-col gap-2">
                <button className="bg-indigo-600 text-white py-2 rounded">
                  Guardar
                </button>
                <button className="bg-gray-800 text-white py-2 rounded">
                  Generar PDF
                </button>

                {/* Conexión a la interfaz EnviarCorreo.jsx vía ruta */}
                <Link to="/enviar-correo" className="block">
                  <button className="w-full bg-blue-600 text-white py-2 rounded">
                    Enviar cotización
                  </button>
                </Link>

                <button className="bg-red-600 text-white py-2 rounded">
                  Eliminar
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
