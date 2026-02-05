import api from './api';

const attendanceService = {
  getStudentAttendance: async (studentId) => {
    const response = await api.get(`/attendance/student/${studentId}`);
    return response.data;
  },

  getAttendancePercentage: async (studentId) => {
    const response = await api.get(`/attendance/student/${studentId}/percentage`);
    return response.data;
  },

  addAttendance: async (studentId, attendanceData) => {
    const response = await api.post(`/attendance/student/${studentId}`, attendanceData);
    return response.data;
  },

  updateAttendance: async (attendanceId, attendanceData) => {
    const response = await api.put(`/attendance/${attendanceId}`, attendanceData);
    return response.data;
  },

  deleteAttendance: async (attendanceId) => {
    await api.delete(`/attendance/${attendanceId}`);
  }
};

export default attendanceService;
