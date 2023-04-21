    public Object getNewsHTML() {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String body = null;
        try {
            String news_url = JetspeedResources.getString("aipo.news_url", "");
            URL url = new URL(news_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), ALEipConstants.DEF_CONTENT_ENCODING));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            body = sb.toString();
        } catch (Exception ex) {
            logger.error("Exception", ex);
            body = "";
        } finally {
            try {
                reader.close();
                conn.disconnect();
            } catch (Exception e) {
                logger.error("Exception", e);
                return "";
            }
        }
        return body;
    }
