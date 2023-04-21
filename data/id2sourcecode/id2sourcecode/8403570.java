    public static byte[] slurpContents(InputStream in, boolean close) throws IOException {
        byte[] result = null;
        ByteArrayOutputStream slurpBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) slurpBuffer.write(buffer, 0, bytesRead);
        result = slurpBuffer.toByteArray();
        if (close) try {
            in.close();
        } catch (IOException ioe) {
        }
        return result;
    }
