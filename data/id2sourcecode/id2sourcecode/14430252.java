    private void writeFileBytes(File file, OutputStream out) throws Exception {
        byte[] buffer = new byte[2048];
        FileInputStream fileStream = null;
        int charsRead;
        int size;
        try {
            fileStream = new FileInputStream(file);
            charsRead = 0;
            size = new Long(file.length()).intValue();
            int readCount = 0;
            while ((charsRead < size) && (readCount != -1)) {
                readCount = fileStream.read(buffer);
                charsRead += readCount;
                out.write(buffer, 0, readCount);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
            }
        }
    }
