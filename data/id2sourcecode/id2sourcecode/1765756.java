    private void sendRequest() throws IOException {
        try {
            String stUrl = server + "?ServiceName=" + service + "&ClientVersion=4.0";
            if (customService != null) {
                stUrl += "&CustomService=" + customService;
            }
            URL url = new URL(stUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (user != null && password != null) {
                sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
                String userPwd = encoder.encode((user + ":" + password).getBytes());
                conn.setRequestProperty("Authentication", "Basic " + userPwd);
            }
            conn.setDoOutput(true);
            BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(Xml.getString(request).getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw (e);
        }
    }
