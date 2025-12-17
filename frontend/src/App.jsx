import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Cotizaciones from "./pages/Cotizaciones";
import VerCotizaciones from "./pages/VerCotizaciones";
import Add_Cliente from "./pages/Add_Cliente";     // ← Nombre corregido
import Add_Empleado from "./pages/Add_Empleado";
import Ver_Empleados from "./pages/Ver_Empleados";
import VerClientes from "./pages/VerClientes";
import EnviarCorreo from "./pages/EnviarCorreo";
import Vista_Cotizaciones from "./pages/Vista_Cotizaciones";
import RecuperarContra from "./pages/RecuperarContra";
import RestablecerContra from "./pages/RestablecerContra";
import DashboardEmpleado from "./pages/DashboardEmployee";
import DashboardAccountant from "./pages/DashboardAccountant";


import AdminLayout from "./layouts/AdminLayout";
import DashboardEmployee from "./pages/DashboardEmployee";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AdminLayout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/dashboard-empleado" element={<DashboardEmployee />} />
        <Route path="/dashboard-contador" element={<DashboardAccountant />} />

        <Route path="/cotizaciones" element={<Cotizaciones />} />
        <Route path="/ver-cotizaciones" element={<VerCotizaciones />} />
        <Route path="/add-cliente" element={<Add_Cliente />} />
        <Route path="/add-empleado" element={<Add_Empleado />} />
        <Route path="/ver-empleados" element={<Ver_Empleados />} />
        <Route path="/ver-clientes" element={<VerClientes />} />
      </Route>


        {/* === LOGIN (FUERA DEL LAYOUT) === */}
        <Route path="/" element={<Login />} />

        {/* === PANTALLAS PROTEGIDAS DENTRO DEL ADMIN LAYOUT === */}
        <Route element={<AdminLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />   {/* ← AÑADIR ESTA */}
          <Route path="/cotizaciones" element={<Cotizaciones />} />
          <Route path="/ver-cotizaciones" element={<VerCotizaciones />} />

          <Route path="/add-cliente" element={<Add_Cliente />} />
          <Route path="/add-empleado" element={<Add_Empleado />} />

          <Route path="/ver-empleados" element={<Ver_Empleados />} />
          <Route path="/ver-clientes" element={<VerClientes />} />

          <Route path="/vista-cotizacion" element={<Vista_Cotizaciones />} />
          <Route path="/enviar-correo" element={<EnviarCorreo />} />

        </Route>

        {/* === CONTRASEÑAS (FUERA DEL LAYOUT) === */}
        <Route path="/recuperar" element={<RecuperarContra />} />
        <Route path="/restablecer" element={<RestablecerContra />} />
        

      </Routes>
    </BrowserRouter>
  );
}
