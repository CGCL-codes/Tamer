    public void writeData(ResourceFile rf, InputStream in) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("writeData: ResourceFile=" + rf);
        }
        File dir = getFsRootFile(rf, true);
        String fn = getStorageFileName(rf);
        if (log.isDebugEnabled()) {
            log.debug("Filename: " + fn);
        }
        File f = new File(dir, fn);
        if (log.isDebugEnabled()) {
            if (f.exists()) {
                log.debug("File '" + f + "' already exists; owerwriting");
            } else {
                log.debug("File '" + f + "' does not exist; creating new file");
            }
        }
        log.debug("Opening FileOutputStream");
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(f, false));
            in = new BufferedInputStream(in);
            if (log.isDebugEnabled()) {
                log.debug("Writing data to file...");
            }
            byte[] readBuf = new byte[4096];
            int read;
            do {
                read = in.read(readBuf);
                if (read > 0) {
                    os.write(readBuf, 0, read);
                }
            } while (read > 0);
            os.flush();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (Exception ex) {
                    log.error("Catched an exception while closing OutputStream", ex);
                }
            }
        }
    }
