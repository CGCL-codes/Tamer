    public Document httpRequest(String method, String urlPath, InputStream body) {
        String server = getProperties().getProperty("server");
        String email = getProperties().getProperty("email");
        String password = getProperties().getProperty("password");
        HttpURLConnection connection;
        int httpResponseCode = 0;
        URL url = null;
        try {
            url = new URL(server + urlPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        logMessage(Logger.DEBUG, "HTTP Interaction", "-> " + method + ": " + url, "");
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
        } catch (IOException e1) {
            logMessage(Logger.ERROR, " Connection Error", "Unable to connect to server " + server, "");
            return null;
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedCredential = encoder.encode((email + ":" + password).getBytes());
        connection.setRequestProperty("Authorization", "BASIC " + encodedCredential);
        byte buffer[] = new byte[8192];
        int read = 0;
        if (body != null && method != "DELETE") {
            try {
                connection.setDoOutput(true);
                OutputStream output = connection.getOutputStream();
                while ((read = body.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
            } catch (IOException e2) {
                logMessage(Logger.ERROR, "Connection Error", "Unable to write to connection to server " + server, "");
                return null;
            }
        }
        try {
            connection.connect();
            httpResponseCode = connection.getResponseCode();
        } catch (IOException e2) {
            logMessage(Logger.ERROR, "Connection Error", "Unable to connect to server " + server, "");
            return null;
        }
        logMessage(Logger.DEBUG, "HTTP Interaction", "<- httpResponseCode=" + httpResponseCode + " from " + url, "");
        InputStream responseBodyStream = null;
        if (200 == httpResponseCode) {
            try {
                responseBodyStream = connection.getInputStream();
            } catch (IOException e) {
                logMessage(Logger.ERROR, "Connection Error", "Unable to read from server", "");
                return null;
            }
        } else {
            try {
                responseBodyStream = connection.getErrorStream();
                char charBuffer[] = new char[8192];
                StringBuilder errorXmlString = new StringBuilder();
                Reader in = new InputStreamReader(responseBodyStream, "UTF-8");
                do {
                    read = in.read(charBuffer, 0, buffer.length);
                    if (read > 0) {
                        errorXmlString.append(charBuffer, 0, read);
                    }
                } while (read >= 0);
                logMessage(Logger.ERROR, "Application Error", "The REST server reports an application error.", errorXmlString.toString());
                responseBodyStream = new StringBufferInputStream(errorXmlString.toString());
            } catch (IOException e) {
                logMessage(Logger.ERROR, "Connection Error", "Unable to read from server " + server, "");
                return null;
            }
        }
        DOMParser parser = new DOMParser();
        try {
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.setFeature("http://apache.org/xml/features/validation/schema", false);
            InputSource source = new InputSource(responseBodyStream);
            parser.parse(source);
        } catch (Exception e1) {
            logMessage(Logger.ERROR, "Application Error", "Unable to parse the reply from server " + server, "");
            return null;
        }
        Document dom = parser.getDocument();
        if (httpResponseCode != 200) {
            Element docEle = dom.getDocumentElement();
            DomPrinter.walk(docEle);
            httpError(httpResponseCode, url, docEle);
            dom = null;
        }
        return dom;
    }
