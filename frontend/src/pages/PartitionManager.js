import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  CircularProgress,
  Alert,
  Grid,
  LinearProgress,
} from '@mui/material';
import StorageIcon from '@mui/icons-material/Storage';
import RefreshIcon from '@mui/icons-material/Refresh';
import { scanPartitions } from '../services/api';

function PartitionManager() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [partitions, setPartitions] = useState([]);

  useEffect(() => {
    handleScan();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleScan = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await scanPartitions();

      if (response.data.success) {
        setPartitions(response.data.partitions);
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to scan partitions');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4">
          <StorageIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
          Partition Manager
        </Typography>

        <Button
          variant="contained"
          startIcon={loading ? <CircularProgress size={20} /> : <RefreshIcon />}
          onClick={handleScan}
          disabled={loading}
        >
          {loading ? 'Scanning...' : 'Refresh'}
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Grid container spacing={3}>
        {partitions.map((partition) => (
          <Grid item xs={12} md={6} key={partition.id}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                  <Typography variant="h5">{partition.name}</Typography>
                  <Typography
                    variant="body2"
                    sx={{
                      px: 2,
                      py: 0.5,
                      borderRadius: 1,
                      bgcolor:
                        partition.healthStatus === 'CRITICAL'
                          ? 'error.main'
                          : partition.healthStatus === 'WARNING'
                          ? 'warning.main'
                          : 'success.main',
                      color: 'white',
                      fontWeight: 'bold',
                    }}
                  >
                    {partition.healthStatus}
                  </Typography>
                </Box>

                <Typography variant="body2" color="textSecondary" gutterBottom>
                  File System: {partition.fileSystem}
                  {partition.isSystemPartition && ' • System Partition'}
                </Typography>

                <Box sx={{ mt: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2">
                      Used: {partition.usedSpaceFormatted}
                    </Typography>
                    <Typography variant="body2">
                      Free: {partition.freeSpaceFormatted}
                    </Typography>
                  </Box>

                  <LinearProgress
                    variant="determinate"
                    value={partition.usagePercentage}
                    sx={{
                      height: 10,
                      borderRadius: 5,
                      backgroundColor: 'grey.300',
                      '& .MuiLinearProgress-bar': {
                        backgroundColor:
                          partition.healthStatus === 'CRITICAL'
                            ? 'error.main'
                            : partition.healthStatus === 'WARNING'
                            ? 'warning.main'
                            : 'success.main',
                      },
                    }}
                  />

                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1 }}>
                    <Typography variant="body2" color="textSecondary">
                      0%
                    </Typography>
                    <Typography variant="body2" fontWeight="bold">
                      {partition.usagePercentage?.toFixed(1)}% used
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      100%
                    </Typography>
                  </Box>

                  <Typography variant="body2" color="textSecondary" sx={{ mt: 2 }}>
                    Total Capacity: {partition.totalSpaceFormatted}
                  </Typography>
                </Box>

                <Alert severity="info" sx={{ mt: 2 }}>
                  Note: Partition resizing and extending features are simulated. Use system tools for actual operations.
                </Alert>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {partitions.length === 0 && !loading && (
        <Card>
          <CardContent>
            <Typography variant="body1" color="textSecondary" align="center">
              No partitions found. Click "Refresh" to scan.
            </Typography>
          </CardContent>
        </Card>
      )}
    </Box>
  );
}

export default PartitionManager;
