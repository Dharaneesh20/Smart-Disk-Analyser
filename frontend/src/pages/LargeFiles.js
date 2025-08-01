import React, { useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
} from '@mui/material';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import SearchIcon from '@mui/icons-material/Search';
import { getLargeFiles } from '../services/api';

function LargeFiles() {
  const [sizeThreshold, setSizeThreshold] = useState('100');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [files, setFiles] = useState([]);

  const handleSearch = async () => {
    setLoading(true);
    setError(null);
    setFiles([]);

    const thresholdBytes = parseInt(sizeThreshold) * 1024 * 1024; // Convert MB to bytes

    try {
      const response = await getLargeFiles(thresholdBytes);

      if (response.data.success) {
        setFiles(response.data.files);
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch large files');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        <InsertDriveFileIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
        Large Files
      </Typography>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="body1" paragraph>
            Find files larger than a specific size threshold. These files often consume significant disk space and may be candidates for cleanup or archival.
          </Typography>

          <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
            <TextField
              label="Size Threshold (MB)"
              type="number"
              value={sizeThreshold}
              onChange={(e) => setSizeThreshold(e.target.value)}
              sx={{ width: 200 }}
              helperText="Minimum file size in MB"
            />

            <Button
              variant="contained"
              startIcon={loading ? <CircularProgress size={20} /> : <SearchIcon />}
              onClick={handleSearch}
              disabled={loading}
            >
              {loading ? 'Searching...' : 'Find Large Files'}
            </Button>
          </Box>

          {error && (
            <Alert severity="error" sx={{ mt: 2 }}>
              {error}
            </Alert>
          )}

          {files.length > 0 && (
            <Alert severity="info" sx={{ mt: 2 }}>
              Found {files.length} files larger than {sizeThreshold} MB
            </Alert>
          )}
        </CardContent>
      </Card>

      {files.length > 0 && (
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Large Files Found
            </Typography>

            <TableContainer component={Paper} sx={{ mt: 2, maxHeight: 600 }}>
              <Table stickyHeader>
                <TableHead>
                  <TableRow>
                    <TableCell>File Name</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Size</TableCell>
                    <TableCell>Path</TableCell>
                    <TableCell>Last Modified</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {files.map((file) => (
                    <TableRow key={file.id} hover>
                      <TableCell>{file.fileName}</TableCell>
                      <TableCell>
                        <Chip label={file.fileType} size="small" />
                      </TableCell>
                      <TableCell>
                        <Typography fontWeight="bold">{file.fileSizeFormatted}</Typography>
                      </TableCell>
                      <TableCell sx={{ maxWidth: 300, overflow: 'hidden', textOverflow: 'ellipsis' }}>
                        {file.filePath}
                      </TableCell>
                      <TableCell>{file.lastModified}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      )}

      {files.length === 0 && !loading && !error && (
        <Card>
          <CardContent>
            <Typography variant="body1" color="textSecondary" align="center">
              No large files found. Run a disk scan first, then search for large files.
            </Typography>
          </CardContent>
        </Card>
      )}
    </Box>
  );
}

export default LargeFiles;
