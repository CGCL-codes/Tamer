    public static byte[] copyToByteArray(File in) throws UtilException {
        Assert.notNull(in, "No input File specified");
        try {
            return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
        } catch (FileNotFoundException e) {
            throw new UtilException(e);
        }
    }
