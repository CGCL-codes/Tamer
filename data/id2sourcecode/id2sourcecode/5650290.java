    protected void copyData(Reader reader, Writer writer) throws IOException {
        StringBuffer word = new StringBuffer();
        while (true) {
            int i = reader.read();
            if (i < 0) {
                break;
            }
            char c = (char) i;
            if (Character.isJavaIdentifierPart(c) || c == '.' || c == '-') {
                word.append(c);
            } else {
                writeUpdatedWord(writer, word.toString());
                word.setLength(0);
                writer.write(c);
            }
        }
        writeUpdatedWord(writer, word.toString());
    }
