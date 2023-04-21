    public String readString(int len) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int n = 0; n < len; n++) baos.write(readUnsignedByte());
        return new String(baos.toByteArray());
    }
