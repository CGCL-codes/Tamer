    public static void writeFile(File f, Writer out) throws IOException {
        Reader rdr = new FileReader(f);
        char[] buf = new char[2048];
        int r;
        try {
            while ((r = rdr.read(buf)) > 0) out.write(buf, 0, r);
        } finally {
            rdr.close();
        }
    }
