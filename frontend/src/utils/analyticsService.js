import api from './api';

const analyticsService = {
  getStudentAnalytics: async (studentId) => {
    const response = await api.get(`/analytics/student/${studentId}`);
    return response.data;
  },

  getAllAnalytics: async () => {
    const response = await api.get('/analytics/all');
    return response.data;
  }
};

export default analyticsService;
