import api from './api';

const marksService = {
  getStudentMarks: async (studentId) => {
    const response = await api.get(`/marks/student/${studentId}`);
    return response.data;
  },

  getAverageMarks: async (studentId) => {
    const response = await api.get(`/marks/student/${studentId}/average`);
    return response.data;
  },

  getMarksBySubject: async (studentId, subject) => {
    const response = await api.get(`/marks/student/${studentId}/subject/${subject}`);
    return response.data;
  },

  addMarks: async (studentId, marksData) => {
    const response = await api.post(`/marks/student/${studentId}`, marksData);
    return response.data;
  },

  updateMarks: async (marksId, marksData) => {
    const response = await api.put(`/marks/${marksId}`, marksData);
    return response.data;
  },

  deleteMarks: async (marksId) => {
    await api.delete(`/marks/${marksId}`);
  }
};

export default marksService;
