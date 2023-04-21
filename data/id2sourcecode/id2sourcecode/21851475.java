    public byte[] transform(InputStream inputStream, boolean instrumentClass) throws ByteCodeTransformException {
        assert null != inputStream;
        ByteArrayOutputStream originalByteArrayOutputStream = new ByteArrayOutputStream();
        try {
            while (inputStream.available() > 0) {
                originalByteArrayOutputStream.write(inputStream.read());
            }
            return transform(originalByteArrayOutputStream.toByteArray(), instrumentClass);
        } catch (IOException e) {
            throw new ByteCodeTransformException(e);
        } finally {
            try {
                originalByteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
