    protected void readToBuffer() throws IOException {
        urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setRequestProperty("User-Agent", useragent);
        urlCon.setRequestProperty("Range", "bytes=" + fpos + "-" + SMath.min(length() - 1, fpos + bufsize - 1));
        urlCon.connect();
        boolean isError = false;
        int rc = urlCon.getResponseCode();
        int length = -1;
        if (rc != HttpURLConnection.HTTP_OK && rc != HttpURLConnection.HTTP_PARTIAL) {
            isError = true;
        } else {
            InputStream is = urlCon.getInputStream();
            length = is.read(data);
            if (length > bufsize || (length < bufsize && savedLength >= 0 && fpos + length != savedLength)) {
                isError = true;
            }
            startIdx = fpos;
            endIdx = fpos + length;
        }
        if (isError) {
            throw new IOException("HTTP read failed with HTTP response " + rc + ". Read " + length + " bytes, requested " + bufsize + " bytes.");
        }
    }
