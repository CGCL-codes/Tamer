    public static byte[] InputStreamToArray(InputStream is) throws IOException {
        byte b[] = new byte[8192];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            int read = is.read(b);
            if (read < 1) break;
            out.write(b, 0, read);
        }
        out.close();
        return out.toByteArray();
    }
