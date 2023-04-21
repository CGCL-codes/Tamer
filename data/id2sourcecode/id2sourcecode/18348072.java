    public static void stringToFile(String s, File f, String encoding) throws IOException {
        if (s == null) throw new IllegalArgumentException("string argument cannot be null");
        if (f == null) throw new IllegalArgumentException("file argument cannot be null");
        if (encoding == null) throw new IllegalArgumentException("encoding argument cannot be null");
        Reader reader = new BufferedReader(new StringReader(s));
        FileOutputStream fos = new FileOutputStream(f);
        Writer out;
        if (encoding == null) out = new BufferedWriter(new OutputStreamWriter(fos)); else out = new BufferedWriter(new OutputStreamWriter(fos, encoding));
        char buf[] = new char[1024 * 50];
        int read = -1;
        while ((read = reader.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
        reader.close();
        out.flush();
        out.close();
    }
