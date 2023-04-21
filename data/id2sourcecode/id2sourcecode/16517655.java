    private static InputStream doPost(Map<String, String> postData) throws Exception {
        String data = null;
        for (String key : postData.keySet()) {
            if (data == null) data = ""; else data += "&";
            data += URLEncoder.encode(key, "UTF-8") + "=";
            data += URLEncoder.encode(postData.get(key), "UTF-8");
        }
        URL url = new URL("http://www.clanbnu.net/bnubot/exception.php");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        wr.close();
        return conn.getInputStream();
    }
