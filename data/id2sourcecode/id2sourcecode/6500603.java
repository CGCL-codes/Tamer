    public static byte[] readContent(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = 0;
            byte[] buffer = new byte[8096];
            while ((read = is.read(buffer)) > 0) {
                baos.write(buffer, 0, read);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }
