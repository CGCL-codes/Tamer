    public static void copy(InputStream in, OutputStream out) throws IOException {
        int data;
        while ((data = in.read()) >= 0) out.write(data);
    }
