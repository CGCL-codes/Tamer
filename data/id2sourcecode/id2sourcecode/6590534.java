    private boolean copy(final DFSFile from, final DFSFile to) {
        DFSInputStream dssis = null;
        DFSOutputStream dssos = null;
        long totalWritten = 0;
        try {
            byte[] readArray = new byte[blockSize];
            int rdSize = 0;
            dssis = new DFSInputStream(from);
            dssos = new DFSOutputStream(to);
            while (rdSize != -1 && !progressMonitor.isCanceled()) {
                rdSize = dssis.read(readArray);
                if (rdSize > 0) {
                    dssos.write(readArray, 0, rdSize);
                    totalWritten += rdSize;
                    progressCounter += rdSize;
                    progressMonitor.setProgress(progressCounter);
                }
            }
            explorer.closeStreams(dssis, dssos);
            if (progressMonitor.isCanceled()) {
                to.delete();
                return false;
            }
            return true;
        } catch (IOException e) {
            logger.debug(e);
            try {
                to.delete();
            } catch (DFSException ex) {
                logger.error(e);
            }
            progressCounter -= totalWritten;
            explorer.closeStreams(dssis, dssos);
            return false;
        }
    }
