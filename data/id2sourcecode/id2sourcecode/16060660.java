    protected String _doGet(String url) throws IOException {
        if (userId == -1) throw new IOException("User not logged in");
        url = SwingUtils.combineUrl(url, "portlet_id=" + namespace);
        URL urlGet = new URL(url);
        URLConnection connection = urlGet.openConnection();
        connection.setRequestProperty("Cookie", "JSESSIONID=" + swsessionId);
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = "";
        StringBuffer stringBuffer = new StringBuffer(1000);
        while ((inputLine = reader.readLine()) != null) {
            stringBuffer.append(inputLine);
        }
        reader.close();
        return stringBuffer.toString();
    }
