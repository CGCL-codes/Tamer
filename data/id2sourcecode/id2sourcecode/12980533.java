    public String htmlContent(String urlStr, String charset) {
        StringBuffer html = new StringBuffer();
        BufferedReader reader = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
            con.setInstanceFollowRedirects(false);
            con.connect();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
            String line;
            while ((line = reader.readLine()) != null) {
                html.append(line).append("\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html.toString();
    }
