    public static void sendPostRequest(String endpoint, String data, String outputfile, NodeCookie cookie) throws Exception {
        logger.debug("send request " + endpoint + "(" + data + ")");
        URL url = new URL(endpoint);
        cookie.addCookieToUrl(url);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        if (data != null) {
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();
        }
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(outputfile));
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            bw.write(line + "\n");
        }
        bw.close();
        reader.close();
        cookie.storeCookie();
        XmlToJson.applyStyle(outputfile, outputfile.replaceAll("xml", "json"), StyleDir + "asyncjob.xsl");
    }
