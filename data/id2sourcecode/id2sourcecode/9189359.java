    public static String readCharacters(final File file) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
        StringWriter sw = new StringWriter();
        Writer writer = new BufferedWriter(sw);
        _io(reader, writer);
        String characters = sw.toString();
        return characters;
    }
