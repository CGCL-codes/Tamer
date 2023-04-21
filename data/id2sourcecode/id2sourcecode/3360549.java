    private Element apiCall(String methodName, Map<String, String> partialQuery) throws RpxException {
        Map<String, String> query = new HashMap<String, String>(partialQuery);
        query.put("format", "xml");
        query.put("apiKey", apiKey);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : query.entrySet()) {
            if (sb.length() > 0) sb.append('&');
            try {
                sb.append(URLEncoder.encode(e.getKey().toString(), "UTF-8"));
                sb.append('=');
                sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                logger.error("unexpected encoding error: " + ex.getMessage(), ex);
            }
        }
        String data = sb.toString();
        try {
            URL url = new URL(baseUrl + "/api/v2/" + methodName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            osw.write(data);
            osw.close();
            Document doc = new SAXBuilder().build(conn.getInputStream());
            Element response = doc.getRootElement();
            if (!response.getAttributeValue("stat").equals("ok")) {
                throw new RpxException("Unexpected API error");
            }
            return response;
        } catch (IOException e) {
            throw new RpxException("i/o exception: " + e.getMessage(), e);
        } catch (JDOMException e) {
            throw new RpxException("jdom exception: " + e.getMessage(), e);
        }
    }
