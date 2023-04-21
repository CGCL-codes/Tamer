    private static void copy(InputStream is, OutputStream os) throws IOException {
        int l = 0;
        for (byte[] b = new byte[1024]; (l = is.read(b)) > 0; ) os.write(b, 0, l);
    }
