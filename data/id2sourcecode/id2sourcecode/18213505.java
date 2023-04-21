    private boolean transferUp(final File lFile, final DFSFile uFile) {
        FileInputStream lis = null;
        DFSOutputStream dssos = null;
        long totalWritten = 0;
        try {
            byte[] readArray = new byte[blockSize];
            int rdSize = 0;
            lis = new FileInputStream(lFile);
            dssos = new DFSOutputStream(uFile);
            while (rdSize != -1 && !progressMonitor.isCanceled()) {
                rdSize = lis.read(readArray);
                if (rdSize > 0) {
                    dssos.write(readArray, 0, rdSize);
                    totalWritten += rdSize;
                    progressCounter += rdSize;
                    progressMonitor.setProgress(progressCounter);
                }
            }
            explorer.closeStreams(lis, dssos);
            if (progressMonitor.isCanceled()) {
                uFile.delete();
                return false;
            }
            return true;
        } catch (IOException e) {
            logger.debug(e);
            try {
                uFile.delete();
                progressCounter -= totalWritten;
            } catch (DFSException ex) {
                logger.debug(ex);
            }
            explorer.closeStreams(lis, dssos);
            return false;
        }
    }
