    public byte[] read(InputStream input) throws ResourceException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            while (input.available() > 0) bytes.write(input.read());
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
