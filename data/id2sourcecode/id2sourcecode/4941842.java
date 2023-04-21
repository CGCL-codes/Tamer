    public LogFileInfo prepareForReading() throws Exception {
        URL url = new URL(PROTOCOL, host, port, file);
        httpConnection = (HttpURLConnection) url.openConnection();
        currFileSize = httpConnection.getContentLength();
        currFileDate = httpConnection.getLastModified();
        bufReader = new BufferedReader(new InputStreamReader(url.openStream(), getEncoding()));
        return new LogFileInfo(currFileSize, currFileDate);
    }
