    public void saveFile(InputStream in, String name, String fileKey, ProgressListener listener, int batchNo, long maxBatchSize, boolean isFinalBatch) throws IOException {
        if (listener == null) {
            listener = new ProgressListener(null);
        }
        int index = 0;
        File uploadedFile = new File(fileKey + "_" + index);
        while (uploadedFile.length() >= MAX_CHUNK_BYTES) {
            uploadedFile = new File(fileKey + "_" + ++index);
        }
        listener.resetCurrentSize((index * MAX_CHUNK_BYTES) + uploadedFile.length());
        int current_size = (int) uploadedFile.length();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(uploadedFile, uploadedFile.exists());
            byte[] buffer;
            if (MAX_CHUNK_BYTES - current_size < MAX_BUFFER_BYTES) {
                buffer = new byte[MAX_CHUNK_BYTES - current_size];
            } else {
                buffer = new byte[MAX_BUFFER_BYTES];
            }
            int readBytes = in.read(buffer);
            while (readBytes > -1) {
                fos.write(buffer, 0, readBytes);
                current_size = current_size + readBytes;
                listener.updateStatus(readBytes);
                if (current_size == MAX_CHUNK_BYTES) {
                    uploadedFile = new File(fileKey + "_" + ++index);
                    fos = new FileOutputStream(uploadedFile, uploadedFile.exists());
                    buffer = new byte[MAX_BUFFER_BYTES];
                } else if (current_size + MAX_BUFFER_BYTES > MAX_CHUNK_BYTES) {
                    buffer = new byte[MAX_CHUNK_BYTES - current_size];
                } else {
                    buffer = new byte[MAX_BUFFER_BYTES];
                }
                readBytes = in.read(buffer);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        logger.logIt("Wrote " + uploadedFile.getAbsolutePath() + " to server");
    }
