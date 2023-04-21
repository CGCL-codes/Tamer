    public static String transformTree(String urlString, String nexml) throws Exception {
        nexml = replaceXmlChars(nexml);
        String data = URLEncoder.encode("nexml", "UTF-8") + "=" + URLEncoder.encode(nexml, "UTF-8");
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        wr.close();
        rd.close();
        String s = sb.toString();
        return s;
    }
