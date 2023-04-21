    public static String getRequestContent(String urlText, String method) throws Exception {
        URL url = new URL(urlText);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setUseCaches(false);
        urlcon.setDoInput(true);
        urlcon.setDoOutput(true);
        urlcon.setRequestMethod(method);
        BufferedReader reader = null;
        String line = null;
        try {
            urlcon.connect();
            reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
            line = reader.readLine();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
            try {
                urlcon.disconnect();
            } catch (Exception e) {
            }
        }
        return line;
    }
