    protected SPARQLResultSet runQuery(String query) throws IOException, XMLStreamException {
        try {
            String urlStr = sparqlEndpoint + "?query=" + URLEncoder.encode(query, "UTF-8");
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("Accept", "application/sparql-results+xml");
            if (authHeader != null) {
                urlConn.setRequestProperty("Authorization", authHeader);
            }
            return new SPARQLResultSet(urlConn.getInputStream());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported by this JVM");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL - have you set the correct " + "SPARQL endpoint?", e);
        }
    }
