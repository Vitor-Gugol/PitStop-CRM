
import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import NovaOrdem from "./pages/NovaOrdem";
import FluxoCarros from './pages/FluxoCarros';
import Header from './components/Header';
import ControleMecanicos from './pages/ControleMecanicos';
import './pages/global.css';
import styles from './components/App.module.css';
import ComissoesMecanicos from './components/ComissoesMecanicos';

function App() {
    return (
        <Router>
            <AppContent />
        </Router>
    );
}

function AppContent() {
    const location = useLocation();
    const isLoginPage = location.pathname === '/login';

    return (
        <div>
            {!isLoginPage && <Header />} {/* Renderiza o Header se não estiver na página de login */}
            <div className={styles.appContent}>
                <Routes>
                     <Route path="/dashboard" element={<Dashboard />} />
                         <Route path="/relatorio-fluxo-carros" element={<FluxoCarros />} />
                        <Route path="/controle-mecanicos" element={<ControleMecanicos />} />
                        <Route path="/comissoes-mecanicos" element={<ComissoesMecanicos />} /> {/* Adicione esta rota */}
                        <Route path="/" element={<Navigate to="/login" />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/nova-ordem" element={<NovaOrdem />} />
                        <Route path="/relatorio-fluxo-carros" element={<FluxoCarros />} />
                        
                </Routes>
            </div>
        </div>
    );
}

export default App;