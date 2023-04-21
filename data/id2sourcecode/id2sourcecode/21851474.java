    public void transform(InputStream inputStream, OutputStream outputStream, boolean instrumentClass) throws ByteCodeTransformException {
        assert null != inputStream;
        assert null != outputStream;
        byte[] transformedByteArray = transform(inputStream, instrumentClass);
        if (null != transformedByteArray) {
            ByteArrayInputStream transformedByteArrayInputStream = new ByteArrayInputStream(transformedByteArray);
            try {
                while (transformedByteArrayInputStream.available() > 0) {
                    outputStream.write(transformedByteArrayInputStream.read());
                }
            } catch (IOException e) {
                throw new ByteCodeTransformException(e);
            } finally {
                try {
                    transformedByteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
