    public static String readFile(String filename) {
        Writer writer = new StringWriter();
        Reader reader = null;
        try {
            reader = new FileReader(filename);
            try {
                pipe(reader, writer);
            } finally {
                if (reader != null) reader.close();
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return writer.toString();
    }
