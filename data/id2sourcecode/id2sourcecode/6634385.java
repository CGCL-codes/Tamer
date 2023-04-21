    public static void parseURL(String urlString, DefaultHandler handler, Map<String, String> properties) throws MalformedURLException {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw e;
        }
        HttpURLConnection urlconn = null;
        try {
            urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.setInstanceFollowRedirects(false);
            if (properties != null) {
                for (Entry<String, String> property : properties.entrySet()) {
                    urlconn.setRequestProperty(property.getKey(), property.getValue());
                }
            }
            urlconn.connect();
            XMLUtils.parse(urlconn.getInputStream(), handler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
    }
