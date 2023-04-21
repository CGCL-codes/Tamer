    private byte[] stream2String(InputStream scriptStream) throws ConfigurationException, IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (byte b; (b = (byte) scriptStream.read()) != -1; ) buf.write(b);
        return buf.toByteArray();
    }
