    protected Element doXMLPost(Element xml, URL url, Map<String, String> header) throws Exception {
        String encoding = "UTF-8";
        if (xmlOutputter == null) {
            xmlOutputter = new XMLOutputter();
        }
        String postString = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n" + xmlOutputter.outputString(xml);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Content-Type", "text/xml; charset=" + encoding);
        if (cookies != null && useCookies()) {
            for (String cookie : cookies) {
                connection.setRequestProperty("Cookie", cookie);
            }
        }
        if (header != null) {
            Set<String> keys = header.keySet();
            for (String key : keys) {
                connection.addRequestProperty(key, header.get(key));
            }
        }
        connection.connect();
        OutputStream stream = connection.getOutputStream();
        stream.write(postString.getBytes(encoding));
        stream.flush();
        Document document = null;
        InputStream inputStream = connection.getInputStream();
        if (sloppyXML()) {
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader("gzip".equals(connection.getContentEncoding()) ? new GZIPInputStream(inputStream) : inputStream));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.trim().length() > 0) {
                    buffer.append(line.trim());
                    buffer.append('\n');
                }
            }
            document = new SAXBuilder().build(new StringReader(buffer.toString().trim()));
        } else {
            document = new SAXBuilder().build("gzip".equals(connection.getContentEncoding()) ? new GZIPInputStream(inputStream) : inputStream);
        }
        stream.close();
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        if (useCookies() && headerFields != null) {
            cookies = headerFields.get("Set-Cookie");
        } else {
            cookies = null;
        }
        connection.disconnect();
        return document.getRootElement();
    }
