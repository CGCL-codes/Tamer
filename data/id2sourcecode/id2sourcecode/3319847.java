    public Document parse(URL url, String charsetName) throws IOException, ParserConfigurationException {
        URLConnection conn = url.openConnection();
        String encoding = conn.getContentEncoding();
        InputStream in = conn.getInputStream();
        if (encoding != null) if (encoding.equalsIgnoreCase("gzip")) in = new GZIPInputStream(in); else System.err.println("Unknown encoding");
        return parse(in, charsetName);
    }
