    private void copyToTempDir(InputStream is, File tempWriteDir, String fileName) {
        File file = new File(tempWriteDir, fileName);
        final int bufferSize = 1000;
        BufferedOutputStream fout = null;
        BufferedInputStream fin = null;
        try {
            fout = new BufferedOutputStream(new FileOutputStream(file));
            fin = new BufferedInputStream(is);
            byte[] buffer = new byte[bufferSize];
            int readCount = 0;
            while ((readCount = fin.read(buffer)) != -1) {
                if (readCount < bufferSize) {
                    fout.write(buffer, 0, readCount);
                } else {
                    fout.write(buffer);
                }
            }
        } catch (IOException e) {
            logger.error("An unexpected exception occured while copying audio files", e);
        } finally {
            try {
                if (fout != null) {
                    fout.flush();
                    fout.close();
                }
            } catch (IOException e) {
                logger.error("An unexpected exception while closing stream", e);
            }
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                logger.error("An unexpected exception while closing stream", e);
            }
        }
    }
