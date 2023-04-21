    @Override
    public Alignment parse(String string) throws AlignmentParserException, IllegalArgumentException, FileNotFoundException {
        if (string != null & !string.isEmpty()) {
            BufferedReader reader;
            try {
                URL url = new URL(string);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                INRIAParser parser = new INRIAParser(reader, ont1, ont2);
                return parser.parse(level);
            } catch (Exception e1) {
                File file = new File(string);
                reader = new BufferedReader(new FileReader(file));
                INRIAParser parser = new INRIAParser(reader, ont1, ont2);
                return parser.parse(level);
            }
        } else {
            throw new IllegalArgumentException("String is null or empty.");
        }
    }
