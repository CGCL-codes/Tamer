    protected static String readHTML(String link) throws IOException {
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException ex) {
            System.err.println("URL mal formada!");
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer newData = new StringBuffer();
        String s = "";
        while (null != (s = br.readLine())) {
            newData.append(s);
        }
        br.close();
        return newData.toString();
    }
