    public SecretKey read(final File file) throws IOException, CryptException {
        final byte[] data = IOHelper.read(new FileInputStream(file).getChannel());
        try {
            return new SecretKeySpec(data, algorithm);
        } catch (Exception e) {
            throw new CryptException("Invalid key format.", e);
        }
    }
