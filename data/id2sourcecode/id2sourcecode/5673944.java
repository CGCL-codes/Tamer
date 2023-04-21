    public static byte[] readFully(InputStream is) throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int read = 0;
        while ((read = is.read(buffer)) > 1) result.write(buffer, 0, read);
        return result.toByteArray();
    }
