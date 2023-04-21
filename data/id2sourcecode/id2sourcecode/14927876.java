    public static byte[] convertBase64StringToString(byte base64[]) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        Base64InputStream bis = new Base64InputStream(bais);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte array[] = new byte[2048];
        int read;
        while ((read = bis.read(array)) > -1) {
            bos.write(array, 0, read);
        }
        return bos.toByteArray();
    }
