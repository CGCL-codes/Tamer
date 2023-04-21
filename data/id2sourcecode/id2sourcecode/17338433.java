    public void writeTo(OutputStream o) throws IOException {
        byte[] b = new byte[len < FILE_LIMIT ? len : FILE_LIMIT];
        InputStream is = toBinaryStream();
        try {
            int n;
            while ((n = is.read(b, 0, b.length)) != -1) o.write(b, 0, n);
        } finally {
            o.close();
            is.close();
        }
    }
