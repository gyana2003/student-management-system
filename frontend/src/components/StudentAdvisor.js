import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import advisorService from '../utils/advisorService';
import authService from '../utils/authService';
import '../styles/StudentAdvisor.css';

function StudentAdvisor() {
  const [sessions, setSessions] = useState([]);
  const [question, setQuestion] = useState('');
  const [category, setCategory] = useState('academic');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const user = authService.getCurrentUser();

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      if (user?.id) {
        const data = await advisorService.getStudentSessions(user.id);
        setSessions(data);
      }
    } catch (error) {
      toast.error('Failed to fetch advisor sessions');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!question.trim()) {
      toast.error('Please enter a question');
      return;
    }

    setSubmitting(true);
    try {
      if (user?.id) {
        await advisorService.createSession(user.id, question, category);
        toast.success('Session created! Waiting for AI advice...');
        setQuestion('');
        fetchSessions();
      }
    } catch (error) {
      toast.error('Failed to create advisory session');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="advisor-container">
      <h2>🤖 AI Student Advisor</h2>
      
      <div className="advisor-form">
        <h3>Ask for Guidance</h3>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Category</label>
            <select value={category} onChange={(e) => setCategory(e.target.value)}>
              <option value="academic">Academic</option>
              <option value="career">Career</option>
              <option value="personal">Personal</option>
              <option value="technical">Technical</option>
            </select>
          </div>
          <div className="form-group">
            <label>Your Question</label>
            <textarea
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              placeholder="Ask your question here..."
              rows="4"
            />
          </div>
          <button type="submit" disabled={submitting}>
            {submitting ? 'Getting Advice...' : 'Get Advice'}
          </button>
        </form>
      </div>

      <div className="sessions-list">
        <h3>Previous Sessions</h3>
        {sessions.length === 0 ? (
          <p>No sessions yet. Start by asking a question!</p>
        ) : (
          sessions.map(session => (
            <div key={session.id} className="session-card">
              <div className="session-header">
                <span className={`category ${session.category}`}>{session.category}</span>
                <span className="date">{new Date(session.createdAt).toLocaleDateString()}</span>
              </div>
              <div className="session-question">
                <strong>Q:</strong> {session.question}
              </div>
              <div className="session-advice">
                <strong>A:</strong> {session.advice}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default StudentAdvisor;
