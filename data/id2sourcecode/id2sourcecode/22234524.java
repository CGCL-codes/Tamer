    public static void testTranslateTutorial() throws UnsupportedEncodingException, MalformedURLException, IOException {
        String cont = getXml("gl");
        String data = URLEncoder.encode("tut", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8");
        data += "&" + URLEncoder.encode("doc", "UTF-8") + "=" + URLEncoder.encode(cont, "UTF-8");
        URL url = new URL("http://localhost:9090/skillworld/rest/tutorial/TranslateTutorial?" + data);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        System.out.println(conn.toString());
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        wr.close();
        rd.close();
    }
