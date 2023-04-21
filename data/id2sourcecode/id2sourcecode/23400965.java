    @Override
    public <T extends WireMessage> T unserialize(InputStream in, Class<T> value) throws IOException {
        DataInputStream dais = new DataInputStream(in);
        int size = dais.readInt();
        int read = 0;
        byte[] buf = new byte[64 * 1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (size > 0) {
            read = dais.read(buf, 0, buf.length);
            if (read > 0) {
                size = size - read;
                baos.write(buf, 0, read);
            }
        }
        byte[] result = baos.toByteArray();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, 0, result.length, value);
    }
