    public static void copyReaders(Reader in, Writer out) throws IOException {
        if (in != null && out != null) {
            char buffer[] = new char[1024];
            int length = 0;
            while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
        }
    }
