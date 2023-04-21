    public static void pipe(Reader reader, Writer writer) {
        char[] buf = new char[256];
        int len = 0;
        try {
            while ((len = reader.read(buf)) > 0) writer.write(buf, 0, len);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
