    public static ByteArrayOutputStream readFully(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int numRead = 0;
        while ((numRead = in.read(buffer)) >= 0) baos.write(buffer, 0, numRead);
        return baos;
    }
