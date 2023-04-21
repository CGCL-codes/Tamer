    public static BinaryReader forUrl(String s) throws IOException {
        URL url = new URL(s);
        InputStream istr = url.openStream();
        return new BinaryReader(istr);
    }
