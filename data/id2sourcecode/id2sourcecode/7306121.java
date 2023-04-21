    @Override
    public Alignment parse(URL url) throws AlignmentParserException, IllegalArgumentException, IOException {
        if (url != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            INRIAParser parser = new INRIAParser(reader, ont1, ont2);
            return parser.parse(level);
        } else {
            throw new IllegalArgumentException("URL is null.");
        }
    }
