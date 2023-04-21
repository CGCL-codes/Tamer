    private URLConnection sendServer() {
        String data = null;
        URL url = null;
        try {
            OutputStreamWriter writer = null;
            url = new URL(urlServer + message.getRequest());
            URLConnection conn = url.openConnection();
            if (sessionId != null && !sessionId.equals("")) {
                conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
            }
            conn.setDoOutput(true);
            data = message.getRequestData();
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            return conn;
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
        return null;
    }
