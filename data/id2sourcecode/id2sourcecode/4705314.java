    private static void internalStreamCopyWithProgress(final InputStream sourcestream, final OutputStream targetstream, String filename, int sourcelength, final IProgressMonitor monitor) throws SocketTimeoutException, IOException {
        try {
            monitor.beginTask("Downloading", sourcelength);
            final DownloadRateCalculator mydrc = fDRC;
            byte[] buffer = new byte[1024];
            int readlength = 0;
            int byteswritten = 0;
            mydrc.setSampleSize(sourcelength > 0 ? sourcelength / 1024 / 10 : 50);
            mydrc.start();
            int blockcount = 0;
            while ((readlength = sourcestream.read(buffer)) != EOF) {
                targetstream.write(buffer, 0, readlength);
                byteswritten += readlength;
                String currentrate = mydrc.getCurrentRateString(readlength);
                if (blockcount++ % 10 == 0) {
                    monitor.subTask("Downloading " + filename + " [" + byteswritten / 1024 + "k / " + sourcelength / 1024 + "k] at " + currentrate);
                }
                monitor.worked(readlength);
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
            }
        } finally {
            monitor.done();
        }
    }
