    private byte[] loadClass(String clazz) {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/" + clazz.replace('.', '/') + ".class");
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] byteArray = new byte[1024];
            int readBytes = -1;
            while ((readBytes = resourceAsStream.read(byteArray, 0, byteArray.length)) != -1) out.write(byteArray, 0, readBytes);
            resourceAsStream.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
