    public BigInteger hash(byte[] input) {
        ++hashCounter;
        this.md5.reset();
        this.md5.update(input);
        byte[] result = this.md5.digest();
        BigInteger erg = new BigInteger(result);
        return erg;
    }
