    public HTMLFileParser(String url) throws Exception {
        BufferedReader br = null;
        InputStream httpStream = null;
        if (url.startsWith("http")) {
            URL fileURL = new URL(url);
            URLConnection urlConnection = fileURL.openConnection();
            httpStream = urlConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(httpStream, "ISO-8859-1"));
        } else {
            br = new BufferedReader(new FileReader(url));
        }
        StringBuffer sbAllDoc = new StringBuffer();
        String ligne = null;
        while ((ligne = br.readLine()) != null) {
            sbAllDoc.append(ligne + " ");
        }
        allDocInOneLine = sbAllDoc.toString();
    }
