    private final byte[] read(final InputStream inputStream) {
        byte[] arrayByte = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[8192];
        try {
            bytes = new byte[inputStream.available()];
            int read;
            while ((read = inputStream.read(bytes)) >= 0) {
                byteArrayOutputStream.write(bytes, 0, read);
            }
            arrayByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
        return arrayByte;
    }
