    public ContainmentProfile createContainmentProfile(InputStream inputStream) throws Exception {
        final byte[] buffer = new byte[1024];
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int read = 0; read >= 0; ) {
            baos.write(buffer, 0, read);
            read = inputStream.read(buffer);
        }
        inputStream = new ByteArrayInputStream(baos.toByteArray());
        try {
            final ContainmentProfile profile = buildFromSerDescriptor(inputStream);
            if (null != profile) {
                return profile;
            }
        } catch (Throwable e) {
            inputStream = new ByteArrayInputStream(baos.toByteArray());
        }
        return buildFromXMLDescriptor(inputStream);
    }
