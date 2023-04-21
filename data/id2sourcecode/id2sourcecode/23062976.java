    protected void writeFileToStream(OutputStream outStream, File tmpFile) throws IOException {
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(tmpFile);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int read = -1;
            while ((read = inStream.read(buffer)) > -1) {
                outStream.write(buffer, 0, read);
            }
        } finally {
            try {
                if (outStream != null) outStream.close();
            } catch (IOException e) {
                log.warn("Error closing outputstream", e);
            }
            try {
                if (inStream != null) inStream.close();
            } catch (IOException e) {
                log.warn("Error closing inputstream", e);
            }
        }
    }
