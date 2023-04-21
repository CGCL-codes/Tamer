    private void writeStream(String s) throws IOException {
        if (writer != null) {
            writer.write(s);
        } else readwrite.writeBytes(s);
    }
