    public static void write(File file, Reader reader) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(write(file));
        try {
            IO.flow(reader, out);
            out.flush();
        } finally {
            out.close();
        }
    }
