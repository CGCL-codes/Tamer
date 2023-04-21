    public static byte[] getInputStreamToByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte b[] = new byte[1024];
        int c = 0;
        while ((c = inputStream.read(b)) > 0) bo.write(b, 0, c);
        bo.flush();
        return bo.toByteArray();
    }
