    protected void send(ByteBuffer bufferToUpload, String fileName, AVList params) throws IOException {
        if (null == bufferToUpload) {
            String message = Logging.getMessage("nullValue.ByteBufferIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (bufferToUpload.limit() == 0) {
            String message = Logging.getMessage("generic.BufferIsEmpty");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (null == url) {
            String message = Logging.getMessage("nullValue.URLIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (WWUtil.isEmpty(fileName)) {
            String message = Logging.getMessage("nullValue.FilenameIsNullOrEmpty");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        try {
            conn = (HttpURLConnection) this.url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            this.writeRequestProperties(conn);
            dos = new DataOutputStream(conn.getOutputStream());
            this.writeProperties(dos, params);
            this.writeContentDisposition(dos, fileName);
            int bytesAvailable = bufferToUpload.rewind().remaining();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = ByteBuffer.allocate(bufferSize).array();
            bufferToUpload.rewind();
            while (bufferToUpload.hasRemaining()) {
                int bytesToRead = Math.min(bufferToUpload.remaining(), maxBufferSize);
                bufferToUpload.get(buffer, 0, bytesToRead);
                dos.write(buffer, 0, bytesToRead);
                this.totalBytesUploaded += (long) bytesToRead;
                this.notifyProgress();
            }
            this.writeContentSeparator(dos);
            dos.flush();
            this.handleResponse(conn);
        } finally {
            WWIO.closeStream(dos, null);
            this.disconnect(conn, this.url.toString());
        }
    }
