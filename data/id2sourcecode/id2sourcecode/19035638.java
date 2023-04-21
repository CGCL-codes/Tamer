    private void copyIStoOS(InputStream is, OutputStream os, int bufSize) {
        byte[] buffer = new byte[bufSize];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) os.write(buffer, 0, len);
        } catch (IOException ioe) {
            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "Compiler: CopyIStoOS: " + ioe);
        }
    }
