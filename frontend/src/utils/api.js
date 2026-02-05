const API_URL = 'http://localhost:8081/api';

// Helper function to make API requests with automatic token injection
const api = {
  async request(endpoint, options = {}) {
    const url = `${API_URL}${endpoint}`;
    const token = localStorage.getItem('token');
    
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers,
    };
    
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
    
    const response = await fetch(url, {
      ...options,
      headers,
    });
    
    if (!response.ok) {
      const error = new Error(`HTTP ${response.status}: ${response.statusText}`);
      error.response = { status: response.status, data: await response.json().catch(() => ({})) };
      throw error;
    }
    
    const data = await response.json();
    return { data, status: response.status };
  },

  get(endpoint, config = {}) {
    return this.request(endpoint, { ...config, method: 'GET' });
  },

  post(endpoint, data, config = {}) {
    return this.request(endpoint, {
      ...config,
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  put(endpoint, data, config = {}) {
    return this.request(endpoint, {
      ...config,
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  delete(endpoint, config = {}) {
    return this.request(endpoint, { ...config, method: 'DELETE' });
  },
};

export default api;
