    private void copyFromTo(InputStream in, JarOutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int n;
        while ((n = in.read(buffer)) != -1) if (n > 0) out.write(buffer, 0, n);
    }
