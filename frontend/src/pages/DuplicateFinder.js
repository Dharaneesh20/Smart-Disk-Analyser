import React, { useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  CircularProgress,
  Alert,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
} from '@mui/material';
import FileCopyIcon from '@mui/icons-material/FileCopy';
import SearchIcon from '@mui/icons-material/Search';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { findDuplicates } from '../services/api';

function DuplicateFinder() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [duplicateGroups, setDuplicateGroups] = useState([]);
  const [totalWastedSpace, setTotalWastedSpace] = useState(0);

  const handleFindDuplicates = async () => {
    setLoading(true);
    setError(null);
    setDuplicateGroups([]);

    try {
      const response = await findDuplicates();

      if (response.data.success) {
        setDuplicateGroups(response.data.duplicates);
        
        // Calculate total wasted space
        let wastedSpace = 0;
        response.data.duplicates.forEach(group => {
          if (group.length > 1) {
            const fileSize = group[0].fileSize;
            wastedSpace += fileSize * (group.length - 1);
          }
        });
        setTotalWastedSpace(wastedSpace);
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to find duplicates');
    } finally {
      setLoading(false);
    }
  };

  const formatBytes = (bytes) => {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        <FileCopyIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
        Duplicate File Finder
      </Typography>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="body1" paragraph>
            Find duplicate files based on MD5 hash comparison. This helps you identify and remove redundant files to free up disk space.
          </Typography>

          <Button
            variant="contained"
            startIcon={loading ? <CircularProgress size={20} /> : <SearchIcon />}
            onClick={handleFindDuplicates}
            disabled={loading}
          >
            {loading ? 'Searching...' : 'Find Duplicates'}
          </Button>

          {error && (
            <Alert severity="error" sx={{ mt: 2 }}>
              {error}
            </Alert>
          )}

          {duplicateGroups.length > 0 && (
            <Alert severity="info" sx={{ mt: 2 }}>
              Found {duplicateGroups.length} duplicate groups. 
              Potential space savings: {formatBytes(totalWastedSpace)}
            </Alert>
          )}
        </CardContent>
      </Card>

      {duplicateGroups.length > 0 && (
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Duplicate Groups
            </Typography>

            {duplicateGroups.map((group, groupIndex) => (
              <Accordion key={groupIndex} sx={{ mb: 1 }}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, width: '100%' }}>
                    <Chip label={`${group.length} copies`} color="warning" size="small" />
                    <Typography>
                      {group[0]?.fileName} ({group[0]?.fileSizeFormatted})
                    </Typography>
                  </Box>
                </AccordionSummary>
                <AccordionDetails>
                  <TableContainer component={Paper}>
                    <Table size="small">
                      <TableHead>
                        <TableRow>
                          <TableCell>File Name</TableCell>
                          <TableCell>Size</TableCell>
                          <TableCell>Path</TableCell>
                          <TableCell>Last Modified</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {group.map((file) => (
                          <TableRow key={file.id} hover>
                            <TableCell>{file.fileName}</TableCell>
                            <TableCell>{file.fileSizeFormatted}</TableCell>
                            <TableCell sx={{ maxWidth: 300, overflow: 'hidden', textOverflow: 'ellipsis' }}>
                              {file.filePath}
                            </TableCell>
                            <TableCell>{file.lastModified}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                  
                  <Alert severity="warning" sx={{ mt: 2 }}>
                    Review carefully before deleting. Keep one copy and remove the rest to save {formatBytes(group[0]?.fileSize * (group.length - 1))}.
                  </Alert>
                </AccordionDetails>
              </Accordion>
            ))}
          </CardContent>
        </Card>
      )}

      {duplicateGroups.length === 0 && !loading && !error && (
        <Card>
          <CardContent>
            <Typography variant="body1" color="textSecondary" align="center">
              No duplicate files found. Run a disk scan first, then search for duplicates.
            </Typography>
          </CardContent>
        </Card>
      )}
    </Box>
  );
}

export default DuplicateFinder;
