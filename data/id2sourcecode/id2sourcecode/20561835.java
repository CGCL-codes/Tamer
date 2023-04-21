    private void convertAsUtf8_(InputStream input, OutputStream output, PrintStream errorStream, String inputFilename, String encoding) throws IOException {
        InputStream utf8Input = convertEncoding(input, encoding, "UTF8");
        ByteArrayOutputStream utf8Output = new ByteArrayOutputStream(4096);
        convert_(utf8Input, utf8Output, errorStream, inputFilename, Configuration.UTF8);
        InputStreamReader reader = null;
        OutputStreamWriter writer = null;
        try {
            reader = new InputStreamReader(new ByteArrayInputStream(utf8Output.toByteArray()), "UTF8");
            writer = new OutputStreamWriter(output, encoding);
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
    }
