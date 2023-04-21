    private static byte[] readInputStreamFully(InputStream is) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        {
            int i = 0;
            while ((i = is.read()) != -1) b.write(i);
        }
        return b.toByteArray();
    }
