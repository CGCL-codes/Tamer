    public T read(final File file) throws IOException, CryptException {
        byte[] data = IOHelper.read(new FileInputStream(file).getChannel());
        if (PemHelper.isPem(data)) {
            data = PemHelper.decode(data);
        }
        return decode(data);
    }
