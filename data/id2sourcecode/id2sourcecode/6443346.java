    public static String readString(InputStream is) throws IOException {
        Reader reader = null;
        Writer writer = null;
        try {
            reader = new InputStreamReader(is);
            writer = new StringWriter();
            copy(reader, writer);
            return writer.toString();
        } finally {
            close(reader);
            close(writer);
        }
    }
