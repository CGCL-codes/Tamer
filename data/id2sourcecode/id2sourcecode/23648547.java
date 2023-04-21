    protected static String post(String urlValue, Map data) throws IOException {
        BufferedReader input;
        URL url = new URL(urlValue);
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        StringBuffer content = new StringBuffer();
        Iterator i = data.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            content.append(entry.getKey()).append('=');
            content.append(URLEncoder.encode((String) entry.getValue(), "utf-8"));
            if (i.hasNext()) {
                content.append("&");
            }
        }
        out.writeBytes(content.toString());
        out.flush();
        out.close();
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer output = new StringBuffer();
        String line;
        while (null != (line = input.readLine())) {
            output.append(line).append('\n');
        }
        input.close();
        return output.toString();
    }
