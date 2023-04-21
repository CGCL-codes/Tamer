    public byte[] test1EmptyInImageOut() {
        InputStream ins = AttachmentTestImpl.class.getResourceAsStream(IMAGE);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        int size = 1024;
        byte[] buf = new byte[size];
        try {
            while ((len = ins.read(buf, 0, size)) != -1) bos.write(buf, 0, len);
            ins.close();
        } catch (IOException e) {
        }
        return bos.toByteArray();
    }
