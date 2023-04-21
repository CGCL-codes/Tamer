    private String doRequest(String q, String host, String key) throws GeocoderException {
        URL url;
        URLConnection conn;
        String query = "";
        StringBuffer buffer = new StringBuffer();
        try {
            query = "q=" + URLEncoder.encode(q, "UTF-8") + "&output=js&key=" + URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("widget-unsupported-encoding", query, e);
            throw new GeocoderException(e);
        }
        try {
            url = new URL(host + query);
            conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                buffer.append(line);
            }
            rd.close();
        } catch (MalformedURLException e) {
            logger.error("widget-geocoder-malformed-url", host + query, e);
            throw new GeocoderException(e);
        } catch (IOException e) {
            logger.error("widget-geocoder-io-exception", host + query, e);
            throw new GeocoderException(e);
        }
        return buffer.toString();
    }
