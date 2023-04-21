    private void writeContentsOn(InputStream is, StringWriter sw) throws Exception {
        while (is.available() > 0) {
            sw.write(is.read());
        }
    }
