    private byte[] readFully(InputStream in) throws IOException {
        in = new BufferedInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c;
        while ((c = in.read()) != -1) out.write(c);
        return out.toByteArray();
    }
