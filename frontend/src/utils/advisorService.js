import api from './api';

const advisorService = {
  createSession: async (studentId, question, category) => {
    const response = await api.post(`/advisor/student/${studentId}/session`, {
      question,
      category
    });
    return response.data;
  },

  getStudentSessions: async (studentId) => {
    const response = await api.get(`/advisor/student/${studentId}/sessions`);
    return response.data;
  },

  getSession: async (sessionId) => {
    const response = await api.get(`/advisor/session/${sessionId}`);
    return response.data;
  },

  deleteSession: async (sessionId) => {
    await api.delete(`/advisor/session/${sessionId}`);
  }
};

export default advisorService;
