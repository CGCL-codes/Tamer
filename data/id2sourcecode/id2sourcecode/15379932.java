    private byte[] getStoredBytes(File file) throws Exception {
        FileInputStream stream = new FileInputStream(file);
        ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = stream.read(buf)) > -1) arrayStream.write(buf, 0, len);
        stream.close();
        buf = arrayStream.toByteArray();
        arrayStream.close();
        return buf;
    }
