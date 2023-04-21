    protected URLConnection doConnectWithUpload(URLConnection connection, String contentType, int contentLength, InputStream content) throws IOException {
        log("uploading " + contentLength + " bytes of type " + contentType, Project.MSG_VERBOSE);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Length", String.valueOf(contentLength));
        connection.setRequestProperty("Content-Type", contentType);
        connection.connect();
        OutputStream toServer = connection.getOutputStream();
        int buffersize = blockSize * 1024;
        if (contentLength < buffersize) buffersize = contentLength;
        byte[] buffer = new byte[buffersize];
        int remaining = contentLength;
        while (remaining > 0) {
            int read = content.read(buffer);
            log("block of " + read, Project.MSG_DEBUG);
            toServer.write(buffer, 0, read);
            remaining -= read;
            if (verbose) {
                showProgressChar('^');
            }
        }
        if (verbose) {
            showProgressChar('\n');
        }
        log("upload completed", Project.MSG_DEBUG);
        return connection;
    }
