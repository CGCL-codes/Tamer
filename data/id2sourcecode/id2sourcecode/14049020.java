    private void writeReader(Reader reader) throws IOException {
        this.out.write('"');
        char[] buf = new char[4096];
        for (int read = reader.read(buf); read >= 0; read = reader.read(buf)) writeChars(buf, read);
        reader.close();
        this.out.write('"');
    }
