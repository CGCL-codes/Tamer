    public static byte[] blobToBytes(Blob blob) {
        byte[] byteData = null;
        try {
            InputStream is = blob.getBinaryStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            byte[] bytes = new byte[512];
            int readBytes;
            while ((readBytes = is.read(bytes)) > 0) {
                os.write(bytes, 0, readBytes);
            }
            byteData = os.toByteArray();
            is.close();
            os.close();
        } catch (Exception e) {
            throw new OopsException(e, "Error reading file data.");
        }
        return byteData;
    }
