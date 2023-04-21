    protected static String get(String urlValue, Map data) throws IOException {
        BufferedReader input;
        StringBuffer value = new StringBuffer(urlValue);
        if (data.size() > 0) {
            if (value.indexOf("?") == -1) {
                value.append("?");
            } else {
                value.append("&");
            }
        }
        Iterator i = data.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            value.append(entry.getKey()).append('=');
            value.append(URLEncoder.encode((String) entry.getValue(), "utf-8"));
            if (i.hasNext()) {
                value.append("&");
            }
        }
        URL url = new URL(urlValue);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer output = new StringBuffer();
        String line;
        while (null != (line = input.readLine())) {
            output.append(line).append('\n');
        }
        input.close();
        return output.toString();
    }
