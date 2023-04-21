    private Reader doCgiFormPost(String urlLoc, Map<String, String> parameters) throws IOException {
        URL url = new URL(urlLoc);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        String paramString = "";
        for (Entry<String, String> entry : parameters.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            paramString = paramString + paramName + "=" + paramValue + "&";
        }
        out.print(paramString);
        out.close();
        InputStreamReader in = new InputStreamReader(connection.getInputStream());
        return in;
    }
