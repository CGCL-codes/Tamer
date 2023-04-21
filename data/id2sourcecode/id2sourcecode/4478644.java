    public static KB parseKBFile(URL url) throws IOException, ParseException {
        KBParser parser = new KBParser(url.openStream());
        return parser.KB();
    }
