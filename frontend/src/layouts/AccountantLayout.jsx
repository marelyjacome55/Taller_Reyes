import React, { useState } from "react";
import { Outlet } from "react-router-dom";
import AccountantMenu from "../components/AccountantMenu";

export default function AccountantLayout() {
  const [open, setOpen] = useState(false);

  return (
    <div className="min-h-screen flex bg-gray-100">
      <AccountantMenu open={open} setOpen={setOpen} />

      <div className="flex-1">
        <button
          onClick={() => setOpen(true)}
          className="m-4 p-2 bg-emerald-700 text-white rounded lg:hidden"
        >
          ☰
        </button>

        <Outlet />
      </div>
    </div>
  );
}
