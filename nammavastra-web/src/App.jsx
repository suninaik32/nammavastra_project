import React from 'react';
import { BrowserRouter, Routes, Route, NavLink } from 'react-router-dom';
import { LayoutDashboard, Image as ImageIcon, Calculator, Upload } from 'lucide-react';

import Dashboard from './pages/Dashboard';
import Gallery from './pages/Gallery';
import PriceCalculator from './pages/PriceCalculator';

function Sidebar() {
  return (
    <div className="sidebar">
      <div className="sidebar-logo">
        {/* Simple Silk Logo SVG Placeholder */}
        <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8"/>
          <polyline points="16 6 12 2 8 6"/>
          <line x1="12" y1="2" x2="12" y2="15"/>
        </svg>
        Namma-Vastra
      </div>
      <div className="nav-links">
        <NavLink to="/" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
          <LayoutDashboard size={20} />
          Dashboard
        </NavLink>
        <NavLink to="/gallery" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
          <ImageIcon size={20} />
          My Gallery
        </NavLink>
        <NavLink to="/calculator" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
          <Calculator size={20} />
          Calculator
        </NavLink>
      </div>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <div className="app-container">
        <Sidebar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/gallery" element={<Gallery />} />
            <Route path="/calculator" element={<PriceCalculator />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
