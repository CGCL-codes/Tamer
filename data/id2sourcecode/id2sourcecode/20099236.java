    public static Document parse(URL url) throws DocumentException, IOException {
        URLConnection urlConnection;
        DataInputStream inStream;
        urlConnection = url.openConnection();
        ((HttpURLConnection) urlConnection).setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(false);
        urlConnection.setUseCaches(false);
        inStream = new DataInputStream(urlConnection.getInputStream());
        byte[] bytes = new byte[1024];
        int read;
        StringBuilder builder = new StringBuilder();
        while ((read = inStream.read(bytes)) >= 0) {
            String readed = new String(bytes, 0, read, "UTF-8");
            builder.append(readed);
        }
        SAXReader reader = new SAXReader();
        XmlUtils.createIgnoreErrorHandler(reader);
        Document dom = reader.read(new StringReader(builder.toString()));
        inStream.close();
        return dom;
    }
