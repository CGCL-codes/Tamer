    protected void send(String stringToUpload, String fileName, AVList params) throws IOException {
        if (WWUtil.isEmpty(stringToUpload)) {
            String message = Logging.getMessage("nullValue.StringIsNull");
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
            byte[] buffer = stringToUpload.getBytes("UTF-8");
            dos.write(buffer, 0, buffer.length);
            this.totalBytesUploaded += (long) stringToUpload.length();
            this.notifyProgress();
            this.writeContentSeparator(dos);
            dos.flush();
            this.handleResponse(conn);
        } finally {
            WWIO.closeStream(dos, null);
            this.disconnect(conn, this.url.toString());
        }
    }
