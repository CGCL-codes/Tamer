    private void readerToWriter(Reader reader, Writer writer) throws IOException {
        int ch;
        while ((ch = reader.read()) != -1) {
            if (ch == 0xFFFF) {
                LOG.info("Stripping out 0xFFFF from save file");
            } else if (ch == 8) {
                LOG.info("Stripping out 0x8 from save file");
            } else {
                writer.write(ch);
            }
        }
    }
