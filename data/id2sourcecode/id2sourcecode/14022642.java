    private static String resolve(String structure, String representation) throws IOException {
        String encodedStructure = URLEncoder.encode(structure, "UTF-8");
        URL url = new URL("http://cactus.nci.nih.gov/chemical/structure/" + encodedStructure.replace("+", "%20") + "/" + representation);
        HttpParams parameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parameters, 10 * 1000);
        HttpConnectionParams.setSoTimeout(parameters, 2 * 1000);
        HttpClient client = new DefaultHttpClient(parameters);
        try {
            HttpGet request = new HttpGet(url.toURI());
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            switch(status.getStatusCode()) {
                case HttpStatus.SC_OK:
                    break;
                case HttpStatus.SC_NOT_FOUND:
                    throw new FileNotFoundException(structure);
                default:
                    throw new IOException(status.getReasonPhrase());
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream(10 * 1024);
            try {
                HttpEntity responseBody = response.getEntity();
                try {
                    responseBody.writeTo(os);
                } finally {
                    os.flush();
                }
                String encoding = EntityUtils.getContentCharSet(responseBody);
                if (encoding == null) {
                    encoding = "UTF-8";
                }
                return os.toString(encoding);
            } finally {
                os.close();
            }
        } catch (URISyntaxException use) {
            throw new IOException(use);
        } finally {
            ClientConnectionManager connectionManager = client.getConnectionManager();
            if (connectionManager != null) {
                connectionManager.shutdown();
            }
        }
    }
