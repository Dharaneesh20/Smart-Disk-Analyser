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
  FormControlLabel,
  Checkbox,
} from '@mui/material';
import FolderIcon from '@mui/icons-material/Folder';
import SearchIcon from '@mui/icons-material/Search';
import { scanDirectory } from '../services/api';

function DiskScanner() {
  const [path, setPath] = useState('');
  const [includeHidden, setIncludeHidden] = useState(false);
  const [maxDepth, setMaxDepth] = useState(10);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [files, setFiles] = useState([]);

  const handleScan = async () => {
    if (!path.trim()) {
      setError('Please enter a valid path');
      return;
    }

    setLoading(true);
    setError(null);
    setSuccess(null);
    setFiles([]);

    try {
      const response = await scanDirectory({
        path,
        includeHidden,
        maxDepth: parseInt(maxDepth),
        fileExtensions: null,
      });

      if (response.data.success) {
        setSuccess(`Successfully scanned ${response.data.filesScanned} files`);
        setFiles(response.data.files.slice(0, 100)); // Show first 100
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to scan directory');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        <FolderIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
        Disk Scanner
      </Typography>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Scan Configuration
          </Typography>

          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}>
            <TextField
              label="Directory Path"
              value={path}
              onChange={(e) => setPath(e.target.value)}
              fullWidth
              placeholder="e.g., C:\Users\YourName\Documents or /home/user/documents"
              helperText="Enter the full path to the directory you want to scan"
            />

            <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
              <TextField
                label="Max Depth"
                type="number"
                value={maxDepth}
                onChange={(e) => setMaxDepth(e.target.value)}
                sx={{ width: 200 }}
                helperText="Maximum subdirectory depth"
              />

              <FormControlLabel
                control={
                  <Checkbox
                    checked={includeHidden}
                    onChange={(e) => setIncludeHidden(e.target.checked)}
                  />
                }
                label="Include hidden files"
              />
            </Box>

            <Button
              variant="contained"
              startIcon={loading ? <CircularProgress size={20} /> : <SearchIcon />}
              onClick={handleScan}
              disabled={loading}
              sx={{ width: 'fit-content' }}
            >
              {loading ? 'Scanning...' : 'Start Scan'}
            </Button>
          </Box>

          {error && (
            <Alert severity="error" sx={{ mt: 2 }}>
              {error}
            </Alert>
          )}

          {success && (
            <Alert severity="success" sx={{ mt: 2 }}>
              {success}
            </Alert>
          )}
        </CardContent>
      </Card>

      {files.length > 0 && (
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Scanned Files (showing first 100)
            </Typography>

            <TableContainer component={Paper} sx={{ mt: 2, maxHeight: 600 }}>
              <Table stickyHeader>
                <TableHead>
                  <TableRow>
                    <TableCell>File Name</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Size</TableCell>
                    <TableCell>Path</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {files.map((file) => (
                    <TableRow key={file.id} hover>
                      <TableCell>{file.fileName}</TableCell>
                      <TableCell>{file.fileType}</TableCell>
                      <TableCell>{file.fileSizeFormatted}</TableCell>
                      <TableCell sx={{ maxWidth: 300, overflow: 'hidden', textOverflow: 'ellipsis' }}>
                        {file.filePath}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      )}
    </Box>
  );
}

export default DiskScanner;
