    public static void send(InputStream in, Writer out, String encoding) throws IOException {
        try {
            Reader reader = new InputStreamReader(in, encoding);
            char[] buf = new char[8192];
            for (; ; ) {
                int read = reader.read(buf);
                if (read <= 0) break;
                out.write(buf, 0, read);
            }
        } finally {
            out.close();
            in.close();
        }
    }
