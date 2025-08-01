import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Disk Scanner APIs
export const scanDirectory = (scanRequest) => {
  return api.post('/disk/scan', scanRequest);
};

export const findDuplicates = () => {
  return api.get('/disk/duplicates');
};

export const getLargeFiles = (sizeThreshold = 104857600) => {
  return api.get(`/disk/large-files?sizeThreshold=${sizeThreshold}`);
};

export const getDiskStatistics = () => {
  return api.get('/disk/statistics');
};

// Partition APIs
export const scanPartitions = () => {
  return api.get('/partition/scan');
};

export const listPartitions = () => {
  return api.get('/partition/list');
};

export const getPartition = (id) => {
  return api.get(`/partition/${id}`);
};

export const getPartitionHealth = () => {
  return api.get('/partition/health');
};

export const resizePartition = (id, newSize) => {
  return api.post(`/partition/${id}/resize?newSize=${newSize}`);
};

export const extendPartition = (id, additionalSpace) => {
  return api.post(`/partition/${id}/extend?additionalSpace=${additionalSpace}`);
};

export default api;
