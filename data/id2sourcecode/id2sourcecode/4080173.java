    public static String toString(InputStream inputStream) throws IOException {
        StringWriter writer = new StringWriter();
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            char[] buffer = new char[4096];
            int readed = 0;
            while ((readed = reader.read(buffer, 0, buffer.length)) > 0) {
                writer.write(buffer, 0, readed);
            }
        } finally {
            close(inputStream);
        }
        return writer.toString();
    }
