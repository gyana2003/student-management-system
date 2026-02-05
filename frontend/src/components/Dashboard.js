import React from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../utils/authService';
import '../styles/Dashboard.css';

function Dashboard() {
  const user = authService.getCurrentUser();
  const navigate = useNavigate();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  const menuItems = [
    { id: 'attendance', label: '📊 Attendance', path: '/attendance' },
    { id: 'marks', label: '📈 Marks', path: '/marks' },
    { id: 'advisor', label: '🤖 AI Advisor', path: '/advisor' },
    { id: 'analytics', label: '📉 Analytics', path: '/analytics' },
  ];

  return (
    <div className="dashboard">
      <nav className="navbar">
        <div className="navbar-content">
          <h1>🎓 Student Management System</h1>
          <div className="user-section">
            <span>Welcome, {user?.username}!</span>
            <button onClick={handleLogout} className="logout-btn">Logout</button>
          </div>
        </div>
      </nav>

      <div className="dashboard-container">
        <div className="menu-bar">
          {menuItems.map(item => (
            <button
              key={item.id}
              className="menu-btn"
              onClick={() => navigate(item.path)}
            >
              {item.label}
            </button>
          ))}
        </div>

        <div className="dashboard-content">
          <div className="welcome-section">
            <h2>Welcome to Your Dashboard!</h2>
            <p>Select an option from the menu to get started</p>
            <div className="quick-stats">
              <div className="stat">
                <p>Check your attendance</p>
                <button onClick={() => navigate('/attendance')}>View →</button>
              </div>
              <div className="stat">
                <p>View your marks</p>
                <button onClick={() => navigate('/marks')}>View →</button>
              </div>
              <div className="stat">
                <p>Get AI guidance</p>
                <button onClick={() => navigate('/advisor')}>Ask →</button>
              </div>
              <div className="stat">
                <p>See your analytics</p>
                <button onClick={() => navigate('/analytics')}>Analyze →</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
