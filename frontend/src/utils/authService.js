import api from './api';

const authService = {
  login: async (username, password) => {
    const response = await api.post('/auth/login', { username, password });
    if (response.data && response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  },

  register: async (username, email, password, firstName, lastName) => {
    const response = await api.post('/auth/register', {
      username,
      email,
      password,
      firstName,
      lastName,
      role: 'ROLE_STUDENT'
    });
    if (response.data && response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    return JSON.parse(localStorage.getItem('user'));
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  },

  requestPasswordReset: async (email) => {
    const response = await api.post('/auth/forgot-password', { email });
    return response.data;
  },

  verifyResetCode: async (email, resetCode) => {
    const response = await api.post('/auth/verify-reset-code', { email, resetCode });
    return response.data;
  },

  resetPassword: async (email, resetCode, newPassword) => {
    const response = await api.post('/auth/reset-password', { email, resetCode, newPassword });
    return response.data;
  }
};

export default authService;
