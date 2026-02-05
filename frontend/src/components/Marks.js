import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import marksService from '../utils/marksService';
import authService from '../utils/authService';
import api from '../utils/api';
import '../styles/Marks.css';

function Marks() {
  const [marks, setMarks] = useState([]);
  const [averageMarks, setAverageMarks] = useState(0);
  const [loading, setLoading] = useState(true);
  const [exporting, setExporting] = useState(false);
  const [importing, setImporting] = useState(false);
  const [showEmailForm, setShowEmailForm] = useState(false);
  const [selectedMark, setSelectedMark] = useState(null);
  const user = authService.getCurrentUser();

  useEffect(() => {
    fetchMarks();
  }, []);

  const fetchMarks = async () => {
    try {
      if (user?.id) {
        const data = await marksService.getStudentMarks(user.id);
        setMarks(data);
        const avg = await marksService.getAverageMarks(user.id);
        setAverageMarks(avg || 0);
      }
    } catch (error) {
      toast.error('Failed to fetch marks');
    } finally {
      setLoading(false);
    }
  };

  const handlePdfExport = async () => {
    setExporting(true);
    try {
      const response = await api.get(`/export/marks/${user.id}`);
      const blob = new Blob([response.data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `marks_${user.id}.pdf`);
      document.body.appendChild(link);
      link.click();
      toast.success('Marks report exported as PDF');
    } catch (error) {
      toast.error('Failed to export PDF');
    } finally {
      setExporting(false);
    }
  };

  const handleEmailNotification = async () => {
    if (!selectedMark) {
      toast.warning('Please select a mark to send notification');
      return;
    }
    try {
      await api.post(`/email/send-marks-notification/${user.id}?marks=${selectedMark.percentage}&grade=${selectedMark.grade}&subject=${selectedMark.subject}`);
      toast.success('Marks notification sent via email');
      setShowEmailForm(false);
      setSelectedMark(null);
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
      const response = await fetch('http://localhost:8081/api/import/marks', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: formData
      });
      
      const result = await response.json();
      if (response.ok) {
        toast.success(`Import successful: ${result.success} records added`);
        fetchMarks();
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
      const response = await api.get('/import/marks/template');
      const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'marks_template.xlsx');
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      toast.error('Failed to download template');
    }
  };

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="marks-container">
      <h2>📈 Marks & Grades</h2>
      
      <div className="marks-actions">
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
          <h3>Send Marks Notification</h3>
          <div className="form-group">
            <label>Select Mark to Notify:</label>
            <select value={selectedMark?.id || ''} onChange={(e) => setSelectedMark(marks.find(m => m.id == e.target.value))}>
              <option value="">-- Select --</option>
              {marks.map(mark => (
                <option key={mark.id} value={mark.id}>
                  {mark.subject} ({mark.examType}) - Grade {mark.grade}
                </option>
              ))}
            </select>
          </div>
          <div className="form-actions">
            <button onClick={handleEmailNotification} className="btn-success">Send</button>
            <button onClick={() => setShowEmailForm(false)} className="btn-cancel">Cancel</button>
          </div>
        </div>
      )}

      <div className="marks-stats">
        <div className="stat-card">
          <h3>Average Percentage</h3>
          <p className="percentage">{averageMarks.toFixed(2)}%</p>
        </div>
      </div>

      <div className="marks-list">
        <h3>Marks Records</h3>
        {marks.length === 0 ? (
          <p>No marks found</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Subject</th>
                <th>Exam Type</th>
                <th>Marks</th>
                <th>Percentage</th>
                <th>Grade</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {marks.map(mark => (
                <tr key={mark.id}>
                  <td>{mark.subject}</td>
                  <td>{mark.examType}</td>
                  <td>{mark.marks}/{mark.totalMarks}</td>
                  <td>{mark.percentage.toFixed(2)}%</td>
                  <td className={`grade grade-${mark.grade}`}>{mark.grade}</td>
                  <td>{new Date(mark.examDate).toLocaleDateString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default Marks;
