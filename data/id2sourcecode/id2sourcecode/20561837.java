    private static InputStream convertEncoding(InputStream input, String fromEncoding, String toEncoding) throws IOException {
        InputStreamReader reader = null;
        OutputStreamWriter writer = null;
        ByteArrayOutputStream toStream = new ByteArrayOutputStream(4096);
        try {
            reader = new InputStreamReader(input, fromEncoding);
            writer = new OutputStreamWriter(toStream, toEncoding);
            copyIO(reader, writer);
            writer.flush();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignore) {
                }
            }
        }
        return new ByteArrayInputStream(toStream.toByteArray());
    }
