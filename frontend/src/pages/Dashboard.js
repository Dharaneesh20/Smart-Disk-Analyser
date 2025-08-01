import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import StorageIcon from '@mui/icons-material/Storage';
import FolderIcon from '@mui/icons-material/Folder';
import FileCopyIcon from '@mui/icons-material/FileCopy';
import WarningIcon from '@mui/icons-material/Warning';
import { getDiskStatistics, getPartitionHealth } from '../services/api';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8', '#82CA9D'];

function Dashboard() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [statistics, setStatistics] = useState(null);
  const [partitions, setPartitions] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError(null);

    try {
      const [statsResponse, partitionsResponse] = await Promise.all([
        getDiskStatistics(),
        getPartitionHealth(),
      ]);

      if (statsResponse.data.success) {
        setStatistics(statsResponse.data.statistics);
      }

      if (partitionsResponse.data.success) {
        setPartitions(partitionsResponse.data.partitions);
      }
    } catch (err) {
      setError(err.message || 'Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mt: 2 }}>
        {error}
      </Alert>
    );
  }

  // Prepare data for charts
  const fileTypeData = statistics?.fileTypeDistribution
    ? Object.entries(statistics.fileTypeDistribution).map(([name, value]) => ({
        name,
        value,
      }))
    : [];

  const sizeByTypeData = statistics?.sizeByFileType
    ? Object.entries(statistics.sizeByFileType).map(([name, value]) => ({
        name,
        size: (value / (1024 * 1024 * 1024)).toFixed(2), // Convert to GB
      }))
    : [];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>

      {/* Summary Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Total Files
                  </Typography>
                  <Typography variant="h5">{statistics?.totalFiles || 0}</Typography>
                </Box>
                <FolderIcon sx={{ fontSize: 40, color: 'primary.main' }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Total Size
                  </Typography>
                  <Typography variant="h5">
                    {statistics?.totalSizeFormatted || '0 B'}
                  </Typography>
                </Box>
                <StorageIcon sx={{ fontSize: 40, color: 'info.main' }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Duplicate Files
                  </Typography>
                  <Typography variant="h5">{statistics?.duplicateFiles || 0}</Typography>
                </Box>
                <FileCopyIcon sx={{ fontSize: 40, color: 'warning.main' }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="textSecondary" gutterBottom>
                    Wasted Space
                  </Typography>
                  <Typography variant="h5">
                    {statistics?.duplicateSizeFormatted || '0 B'}
                  </Typography>
                </Box>
                <WarningIcon sx={{ fontSize: 40, color: 'error.main' }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Charts */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                File Type Distribution
              </Typography>
              {fileTypeData.length > 0 ? (
                <ResponsiveContainer width="100%" height={300}>
                  <PieChart>
                    <Pie
                      data={fileTypeData}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      label={({ name, percent }) =>
                        `${name}: ${(percent * 100).toFixed(0)}%`
                      }
                      outerRadius={80}
                      fill="#8884d8"
                      dataKey="value"
                    >
                      {fileTypeData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
                    <Tooltip />
                  </PieChart>
                </ResponsiveContainer>
              ) : (
                <Typography color="textSecondary" align="center" sx={{ py: 4 }}>
                  No data available. Run a disk scan first.
                </Typography>
              )}
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Storage by File Type (GB)
              </Typography>
              {sizeByTypeData.length > 0 ? (
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={sizeByTypeData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="size" fill="#8884d8" />
                  </BarChart>
                </ResponsiveContainer>
              ) : (
                <Typography color="textSecondary" align="center" sx={{ py: 4 }}>
                  No data available. Run a disk scan first.
                </Typography>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Partition Health */}
      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Partition Health
          </Typography>
          <Grid container spacing={2}>
            {partitions.length > 0 ? (
              partitions.map((partition) => (
                <Grid item xs={12} sm={6} md={4} key={partition.id}>
                  <Card variant="outlined">
                    <CardContent>
                      <Typography variant="h6">{partition.name}</Typography>
                      <Typography color="textSecondary" gutterBottom>
                        {partition.fileSystem}
                      </Typography>
                      <Box sx={{ mt: 2 }}>
                        <Typography variant="body2">
                          Total: {partition.totalSpaceFormatted}
                        </Typography>
                        <Typography variant="body2">
                          Used: {partition.usedSpaceFormatted}
                        </Typography>
                        <Typography variant="body2">
                          Free: {partition.freeSpaceFormatted}
                        </Typography>
                        <Box
                          sx={{
                            mt: 1,
                            display: 'flex',
                            alignItems: 'center',
                            gap: 1,
                          }}
                        >
                          <Typography
                            variant="body2"
                            fontWeight="bold"
                            color={
                              partition.healthStatus === 'CRITICAL'
                                ? 'error.main'
                                : partition.healthStatus === 'WARNING'
                                ? 'warning.main'
                                : 'success.main'
                            }
                          >
                            {partition.healthStatus}
                          </Typography>
                          <Typography variant="body2">
                            ({partition.usagePercentage?.toFixed(1)}% used)
                          </Typography>
                        </Box>
                      </Box>
                    </CardContent>
                  </Card>
                </Grid>
              ))
            ) : (
              <Grid item xs={12}>
                <Typography color="textSecondary" align="center" sx={{ py: 2 }}>
                  No partitions scanned yet.
                </Typography>
              </Grid>
            )}
          </Grid>
        </CardContent>
      </Card>
    </Box>
  );
}

export default Dashboard;
