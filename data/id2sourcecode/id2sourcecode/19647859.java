    protected static Document doPost(URL url, Map<String, String> params) throws Exception {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            OutputStream out = conn.getOutputStream();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!first) out.write('&'); else first = false;
                out.write(URLEncoder.encode(entry.getKey(), charset).getBytes("US-ASCII"));
                out.write('=');
                out.write(URLEncoder.encode(entry.getValue(), charset).getBytes("US-ASCII"));
            }
            out.flush();
            out.close();
            conn.connect();
            conn.getResponseCode();
            logger.fine("Request result: " + conn.getResponseMessage());
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.log(Level.WARNING, "Can't send POST request to " + url + ". Response: " + conn.getResponseCode() + ' ' + conn.getResponseMessage());
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());
            conn.disconnect();
            NodeList list = doc.getElementsByTagName("error");
            if (list.getLength() != 0) throw new Exception(list.item(0).getTextContent());
            return doc;
        } catch (Exception err) {
            logger.log(Level.WARNING, "Can't send POST request to " + url, err);
            throw err;
        }
    }
