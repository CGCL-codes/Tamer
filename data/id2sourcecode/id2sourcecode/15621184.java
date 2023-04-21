    public static String getPage(String url, String post) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (post != null) {
            connection.connect();
            connection.setDoOutput(true);
            PrintStream out = new PrintStream(connection.getOutputStream());
            out.println(post);
            out.close();
        } else {
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.0.13) Gecko/2009073021 Firefox/3.0.13");
            connection.setRequestMethod("GET");
            connection.connect();
        }
        String response;
        BufferedReader source = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer buf = new StringBuffer();
        while ((response = source.readLine()) != null) {
            buf.append(response);
        }
        source.close();
        return buf.toString();
    }
