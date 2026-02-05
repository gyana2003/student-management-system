import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import attendanceService from '../utils/attendanceService';
import authService from '../utils/authService';
import api from '../utils/api';
import '../styles/Attendance.css';

function Attendance() {
  const [attendances, setAttendances] = useState([]);
  const [percentage, setPercentage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [exporting, setExporting] = useState(false);
  const [importing, setImporting] = useState(false);
  const [showEmailForm, setShowEmailForm] = useState(false);
  const [emailPayload, setEmailPayload] = useState({ status: 'Good' });
  const user = authService.getCurrentUser();

  useEffect(() => {
    fetchAttendance();
  }, []);

  const fetchAttendance = async () => {
    try {
      if (user?.id) {
        const data = await attendanceService.getStudentAttendance(user.id);
        setAttendances(data);
        const percent = await attendanceService.getAttendancePercentage(user.id);
        setPercentage(percent);
      }
    } catch (error) {
      toast.error('Failed to fetch attendance');
    } finally {
      setLoading(false);
    }
  };

  const handlePdfExport = async () => {
    setExporting(true);
    try {
      const response = await api.get(`/export/attendance/${user.id}`);
      const blob = new Blob([response.data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `attendance_${user.id}.pdf`);
      document.body.appendChild(link);
      link.click();
      toast.success('Attendance report exported as PDF');
    } catch (error) {
      toast.error('Failed to export PDF');
    } finally {
      setExporting(false);
    }
  };

  const handleEmailNotification = async () => {
    try {
      await api.post(`/email/send-attendance-notification/${user.id}?percentage=${percentage}&status=${emailPayload.status}`);
      toast.success('Attendance notification sent via email');
      setShowEmailForm(false);
    } catch (error) {
      toast.error('Failed to send email');
    }
  };

  const handleExcelImport = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    setImporting(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch('http://localhost:8081/api/import/attendance', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: formData
      });
      
      const result = await response.json();
      if (response.ok) {
        toast.success(`Import successful: ${result.success} records added`);
        fetchAttendance();
      } else {
        toast.error(result.error || 'Import failed');
      }
    } catch (error) {
      toast.error('Failed to import file');
    } finally {
      setImporting(false);
      event.target.value = '';
    }
  };

  const downloadTemplate = async () => {
    try {
      const response = await api.get('/import/attendance/template');
      const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'attendance_template.xlsx');
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      toast.error('Failed to download template');
    }
  };

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="attendance-container">
      <h2>📊 Attendance</h2>
      
      <div className="attendance-actions">
        <button onClick={handlePdfExport} disabled={exporting} className="btn-primary">
          {exporting ? 'Exporting...' : '📥 Export as PDF'}
        </button>
        <button onClick={() => setShowEmailForm(!showEmailForm)} className="btn-primary">
          📧 Send Email Notification
        </button>
        <button onClick={downloadTemplate} className="btn-primary">
          📄 Download Template
        </button>
        <label className="btn-primary" htmlFor="excel-upload">
          📤 {importing ? 'Importing...' : 'Import Excel'}
        </label>
        <input 
          id="excel-upload"
          type="file" 
          accept=".xlsx" 
          onChange={handleExcelImport}
          disabled={importing}
          style={{ display: 'none' }}
        />
      </div>

      {showEmailForm && (
        <div className="email-form">
          <h3>Send Attendance Notification</h3>
          <div className="form-group">
            <label>Status:</label>
            <select value={emailPayload.status} onChange={(e) => setEmailPayload({...emailPayload, status: e.target.value})}>
              <option>Excellent</option>
              <option>Good</option>
              <option>Average</option>
              <option>Poor</option>
            </select>
          </div>
          <div className="form-actions">
            <button onClick={handleEmailNotification} className="btn-success">Send</button>
            <button onClick={() => setShowEmailForm(false)} className="btn-cancel">Cancel</button>
          </div>
        </div>
      )}

      <div className="attendance-stats">
        <div className="stat-card">
          <h3>Overall Attendance</h3>
          <p className="percentage">{percentage.toFixed(2)}%</p>
        </div>
      </div>

      <div className="attendance-list">
        <h3>Attendance Records</h3>
        {attendances.length === 0 ? (
          <p>No attendance records found</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Subject</th>
                <th>Status</th>
                <th>Remarks</th>
              </tr>
            </thead>
            <tbody>
              {attendances.map(attendance => (
                <tr key={attendance.id}>
                  <td>{new Date(attendance.attendanceDate).toLocaleDateString()}</td>
                  <td>{attendance.subject}</td>
                  <td className={`status ${attendance.status.toLowerCase()}`}>
                    {attendance.status}
                  </td>
                  <td>{attendance.remarks || '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default Attendance;
