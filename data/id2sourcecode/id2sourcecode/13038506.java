    @Override
    protected ByteBuffer doInBackground() throws Exception {
        setDownloadState(DownloadState.CONNECTING);
        HttpURLConnection connection = createConnection();
        if (postParameters != null) {
            ByteBuffer postData = Charset.forName("UTF-8").encode(encodeParameters(postParameters));
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.addRequestProperty("Content-Length", String.valueOf(postData.remaining()));
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            WritableByteChannel out = Channels.newChannel(connection.getOutputStream());
            out.write(postData);
            out.close();
        }
        contentLength = connection.getContentLength();
        responseHeaders = connection.getHeaderFields();
        setDownloadState(DownloadState.DOWNLOADING);
        ReadableByteChannel in = Channels.newChannel(connection.getInputStream());
        ByteBufferOutputStream buffer = new ByteBufferOutputStream((int) (contentLength > 0 ? contentLength : 32 * 1024));
        try {
            while (!isCancelled() && ((buffer.transferFrom(in)) >= 0)) {
                firePropertyChange(DOWNLOAD_PROGRESS, null, buffer.position());
                if (isContentLengthKnown()) {
                    setProgress((int) ((buffer.position() * 100) / contentLength));
                }
            }
        } catch (IOException e) {
            if (isContentLengthKnown()) throw e;
        } finally {
            in.close();
            setDownloadState(DownloadState.DONE);
        }
        return buffer.getByteBuffer();
    }
