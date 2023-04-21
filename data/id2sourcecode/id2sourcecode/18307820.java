    public static String parseTextFile(File file, Map contextMap) throws Exception {
        StringWriter writer = new StringWriter();
        FileReader reader = new FileReader(file);
        parse(reader, writer, contextMap);
        return writer.toString();
    }
