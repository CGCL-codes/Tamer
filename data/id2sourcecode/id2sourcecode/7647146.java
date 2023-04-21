    public Integer nThPrime(FileInputStream primeBuf, int n) {
        try {
            primeBuf.getChannel().position(SIZE_OF_INT * n);
            return readCurrentPrime(primeBuf);
        } catch (IOException e) {
            return null;
        }
    }
