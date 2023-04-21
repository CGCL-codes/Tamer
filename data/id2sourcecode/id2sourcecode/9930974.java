    private static byte[] getZippedData(HttpURLConnection conn) throws IOException {
        int contentLength = conn.getContentLength();
        ApplicationProperties ap = ApplicationProperties.getSingletonInstance();
        CountingZipInputStream in = new CountingZipInputStream(new BufferedInputStream(conn.getInputStream(), ap.getDefaultDataBuffer()));
        if (in == null) {
            throw new IOException(ErrorMessages.IO_STREAM_ERROR + "(INPUT STREAM)");
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int read = -1;
        byte[] buffer = new byte[40960];
        while ((read = in.read(buffer)) != -1) {
            bout.write(buffer, 0, read);
            if (contentLength > 0) {
                EventDispatcher.getSingletonInstance().fireDownloadEvent(DownloadEvent.DOWNLOADING, (int) in.getBytesRead(), contentLength);
            }
        }
        in.close();
        return bout.toByteArray();
    }
