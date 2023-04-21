    private static String resolve(String citation) throws IOException {
        URL url = new URL("http://aye.comp.nus.edu.sg/parsCit/parsCit.cgi");
        HttpParams parameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parameters, 10 * 1000);
        HttpConnectionParams.setSoTimeout(parameters, 10 * 1000);
        HttpClient client = new DefaultHttpClient(parameters);
        try {
            HttpPost request = new HttpPost(url.toURI());
            MultipartEntity requestBody = new MultipartEntity();
            requestBody.addPart("demo", new StringBody("3"));
            requestBody.addPart("textlines", new StringBody(citation));
            requestBody.addPart("bib3", new StringBody("on"));
            request.setEntity(requestBody);
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            switch(status.getStatusCode()) {
                case HttpStatus.SC_OK:
                    break;
                default:
                    throw new IOException(status.getReasonPhrase());
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
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
