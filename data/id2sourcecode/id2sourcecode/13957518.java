    public static List<String> getHTMLSourcePage(String url) throws Exception {
        List<String> lines = new ArrayList<String>();
        URL fileURL = new URL(url);
        URLConnection urlConnection = fileURL.openConnection();
        InputStream httpStream = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpStream, "ISO-8859-1"));
        String ligne;
        while ((ligne = br.readLine()) != null) {
            lines.add(ligne);
        }
        br.close();
        httpStream.close();
        return lines;
    }
