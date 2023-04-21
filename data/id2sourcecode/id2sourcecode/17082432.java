    protected void send(File fileToUpload, String uploadName, AVList params) throws IOException, NullPointerException {
        if (null == fileToUpload || !fileToUpload.exists()) throw new FileNotFoundException();
        if (null == url) {
            String message = Logging.getMessage("nullValue.URLIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        HttpURLConnection conn = null;
        FileInputStream fis = null;
        DataOutputStream dos = null;
        int bytesRead, bytesAvailable, bufferSize;
        try {
            conn = (HttpURLConnection) this.url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            this.writeRequestProperties(conn);
            dos = new DataOutputStream(conn.getOutputStream());
            this.writeProperties(dos, params);
            this.writeContentDisposition(dos, uploadName);
            fis = new FileInputStream(fileToUpload);
            bytesAvailable = fis.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = ByteBuffer.allocate(bufferSize).array();
            bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bytesRead);
                this.totalBytesUploaded += (long) bytesRead;
                this.notifyProgress();
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }
            this.writeContentSeparator(dos);
            dos.flush();
            this.handleResponse(conn);
        } finally {
            WWIO.closeStream(fis, null);
            WWIO.closeStream(dos, null);
            this.disconnect(conn, this.url.toString());
        }
    }
