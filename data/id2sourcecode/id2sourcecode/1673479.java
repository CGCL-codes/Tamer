    protected Object pollServer(String command, String... parameters) throws ConnectException {
        Object returnValue = "";
        String url = null;
        URL webUrl = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            url = getEncodedUrl(command, parameters);
            doLog("URL:" + url);
            webUrl = new URL(url);
            connection = (HttpURLConnection) webUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            connection.setRequestMethod("POST");
            Object content = connection.getContent();
            if (connection.getContentType() != null) {
                if (connection.getContentType().equals("application/pdf")) {
                    returnValue = (InputStream) content;
                } else if (connection.getContentType().equals("text/html")) {
                    InputStream inputStream = (InputStream) content;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    int readByte;
                    while ((readByte = inputStream.read()) > -1) {
                        output.write(readByte);
                    }
                    returnValue = new String(output.toByteArray());
                    output.close();
                    inputStream.close();
                }
            }
        } catch (MalformedURLException e) {
            LOG.error(e.getMessage() + "->" + url);
        } catch (UnknownServiceException e) {
            doLog(e.getMessage());
        } catch (IOException e) {
            LOG.error(e.getMessage() + " " + url);
            throw new ConnectException(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return returnValue;
    }
