    @SuppressWarnings("unchecked")
    private Element apiCall(String methodName, Map<String, Object> partialQuery) {
        Map<String, Object> query = null;
        if (partialQuery == null) {
            query = new HashMap<String, Object>();
        } else {
            query = new HashMap<String, Object>(partialQuery);
        }
        query.put("format", "xml");
        query.put("apiKey", apiKey);
        StringBuffer sb = new StringBuffer();
        for (Iterator<?> it = query.entrySet().iterator(); it.hasNext(); ) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            try {
                Map.Entry e = (Map.Entry) it.next();
                if (e.getValue() != null) {
                    sb.append(URLEncoder.encode(e.getKey().toString(), "UTF-8"));
                    sb.append('=');
                    sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unexpected encoding error", e);
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
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(conn.getInputStream());
            Element response = (Element) doc.getFirstChild();
            if (!response.getAttribute("stat").equals("ok")) {
                throw new RuntimeException("Unexpected API error");
            }
            return response;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unexpected URL error", e);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IO error", e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unexpected XML error", e);
        } catch (SAXException e) {
            throw new RuntimeException("Unexpected XML error", e);
        }
    }
