    public static String shortenURI(String toShorten) {
        String shortURI = "ERROR";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://is.gd/api.php?longurl=" + toShorten);
        try {
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                response.getEntity().writeTo(ostream);
                Log.e("HTTP CLIENT", ostream.toString());
            } else {
                InputStream content = response.getEntity().getContent();
                shortURI = convertStreamToString(content);
                content.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shortURI;
    }
