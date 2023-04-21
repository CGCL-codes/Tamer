    public static String pullString(InputStream is) throws IOException {
        baos.reset();
        int data;
        while ((data = is.read()) != -1) baos.write(data);
        return new String(baos.toByteArray());
    }
