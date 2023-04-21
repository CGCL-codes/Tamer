    public static void copy(InputStream input, StringWriter writer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        char[] charBuffer = new char[BUFFER_SIZE];
        try {
            int read;
            while ((read = reader.read(charBuffer)) != -1) {
                writer.write(charBuffer, 0, read);
            }
        } finally {
            close(writer);
            close(reader);
            close(input);
        }
    }
