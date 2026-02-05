import React, { useState, useEffect } from 'react';
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { toast } from 'react-toastify';
import analyticsService from '../utils/analyticsService';
import authService from '../utils/authService';
import '../styles/Analytics.css';

function Analytics() {
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);
  const user = authService.getCurrentUser();

  useEffect(() => {
    fetchAnalytics();
  }, []);

  const fetchAnalytics = async () => {
    try {
      if (user?.id) {
        const data = await analyticsService.getStudentAnalytics(user.id);
        setAnalytics(data);
      }
    } catch (error) {
      toast.error('Failed to fetch analytics');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (!analytics) return <div>No analytics data available</div>;

  const chartData = [
    { name: 'Attendance', value: analytics.attendancePercentage },
    { name: 'Marks', value: analytics.averageMarks }
  ];

  return (
    <div className="analytics-container">
      <h2>📊 Analytics Dashboard</h2>

      <div className="analytics-grid">
        <div className="stat-card">
          <h3>Attendance</h3>
          <p className="big-number">{analytics.attendancePercentage.toFixed(1)}%</p>
          <p className="small-text">Overall attendance</p>
        </div>

        <div className="stat-card">
          <h3>Average Marks</h3>
          <p className="big-number">{analytics.averageMarks.toFixed(1)}%</p>
          <p className="small-text">Overall performance</p>
        </div>

        <div className="stat-card">
          <h3>Grade</h3>
          <p className="big-number">{analytics.overallGrade}</p>
          <p className="small-text">Current grade</p>
        </div>

        <div className="stat-card">
          <h3>Status</h3>
          <p className="big-number">{analytics.performanceStatus}</p>
          <p className="small-text">Performance level</p>
        </div>

        <div className="stat-card">
          <h3>Subjects</h3>
          <p className="big-number">{analytics.totalSubjects}</p>
          <p className="small-text">Total subjects</p>
        </div>

        <div className="stat-card">
          <h3>Exams</h3>
          <p className="big-number">{analytics.totalExams}</p>
          <p className="small-text">Total exams</p>
        </div>
      </div>

      <div className="charts-container">
        <div className="chart">
          <h3>Performance Comparison</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis domain={[0, 100]} />
              <Tooltip />
              <Bar dataKey="value" fill="#8884d8" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="chart">
          <h3>Progress Trend</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis domain={[0, 100]} />
              <Tooltip />
              <Line type="monotone" dataKey="value" stroke="#82ca9d" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

export default Analytics;
