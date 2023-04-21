    public static final byte[] getDataSource(InputStream is) {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayByte = null;
        try {
            byte[] bytes = new byte[8192];
            bytes = new byte[is.available()];
            int read;
            while ((read = is.read(bytes)) >= 0) {
                byteArrayOutputStream.write(bytes, 0, read);
            }
            arrayByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                    byteArrayOutputStream = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return arrayByte;
    }
