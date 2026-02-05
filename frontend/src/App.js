import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';

// Components
import Login from './components/Login';
import Register from './components/Register';
import ForgotPassword from './components/ForgotPassword';
import Dashboard from './components/Dashboard';
import Attendance from './components/Attendance';
import Marks from './components/Marks';
import StudentAdvisor from './components/StudentAdvisor';
import Analytics from './components/Analytics';
import PrivateRoute from './components/PrivateRoute';
import authService from './utils/authService';

function App() {
  return (
    <>
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={true}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />

      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          
          <Route
            path="/dashboard"
            element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            }
          />
          
          <Route
            path="/attendance"
            element={
              <PrivateRoute>
                <Attendance />
              </PrivateRoute>
            }
          />
          
          <Route
            path="/marks"
            element={
              <PrivateRoute>
                <Marks />
              </PrivateRoute>
            }
          />
          
          <Route
            path="/advisor"
            element={
              <PrivateRoute>
                <StudentAdvisor />
              </PrivateRoute>
            }
          />
          
          <Route
            path="/analytics"
            element={
              <PrivateRoute>
                <Analytics />
              </PrivateRoute>
            }
          />

          <Route path="/" element={<Navigate to={authService.isAuthenticated() ? "/dashboard" : "/login"} />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
