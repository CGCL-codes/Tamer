    public static byte[] readToByteArray(InputStream stream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int read = stream.read();
            while (read != -1) {
                baos.write(read);
                read = stream.read();
            }
        } catch (IOException e) {
            throw new AppInfrastructureException(e);
        }
        return baos.toByteArray();
    }
