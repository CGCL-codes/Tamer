    public void writeValue(Reader reader) {
        writeSeparatorIfNeeded();
        try {
            this.out.write('"');
            char[] buf = new char[4096];
            for (int read = reader.read(buf); read >= 0; read = reader.read(buf)) this.out.write(new String(buf, 0, read).replace("\"", "\"\""));
            this.out.write('"');
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }
