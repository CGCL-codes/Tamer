    public PrivateKey read(final File file, final char[] password) throws IOException, CryptException {
        byte[] data = IOHelper.read(new FileInputStream(file).getChannel());
        data = decryptKey(data, password);
        return decode(data);
    }
