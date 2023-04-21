    public String read(Reader reader) throws RuntimeIoException {
        StringWriter writer = new StringWriter();
        transfer(reader, writer, false);
        return writer.toString();
    }
