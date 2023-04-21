    private String processFile(String url) throws IOException, MalformedURLException, UnknownHostException {
        String fileString = null;
        NameSelector myNameSelector = new NameSelector(this.sourceLocationNode, initMapHolder);
        if (!myNameSelector.isIncluded(this.srcFile)) {
            return fileString;
        }
        URL myURL = new URL(url);
        HttpURLConnection urlCon = (HttpURLConnection) myURL.openConnection();
        if (!(urlCon instanceof HttpURLConnection || urlCon instanceof HttpsURLConnection)) {
            errEntry.setThrowable(new Exception("Unknown protocol: expected http or https"));
            errEntry.setAppContext("processFile()");
            errEntry.setAppMessage("Unknown protocol: " + myURL.toString());
            logger.logError(errEntry);
            return "";
        }
        sendRequestHeader(urlCon);
        int bufferSize = urlCon.getContentLength();
        if (bufferSize == -1) {
            bufferSize = 5120;
        }
        StringBuffer myBuffer = new StringBuffer(bufferSize);
        if (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String encoding = urlCon.getContentEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), encoding));
            String line = in.readLine();
            while (line != null) {
                myBuffer.append(line);
                line = in.readLine();
            }
            in.close();
            fileString = myBuffer.toString();
            if (fileString.equals("")) {
                throw new java.io.EOFException("Empty response file: " + url);
            }
        } else {
            throw new java.io.IOException("Bad response code: " + urlCon.getResponseCode());
        }
        return fileString;
    }
