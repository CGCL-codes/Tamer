    public static String getRequestContent(String urlText) throws Exception {
        URL url = new URL(urlText);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setRequestMethod("POST");
        urlcon.setUseCaches(false);
        urlcon.setDoOutput(true);
        PrintStream ps = new PrintStream(urlcon.getOutputStream());
        ps.close();
        urlcon.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "UTF-8"));
        String body = "";
        String line = null;
        while ((line = reader.readLine()) != null) {
            body += line;
        }
        reader.close();
        urlcon.disconnect();
        return body;
    }
