    public static void writeAll(Reader in, Writer out, char[] b) throws IOException {
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
