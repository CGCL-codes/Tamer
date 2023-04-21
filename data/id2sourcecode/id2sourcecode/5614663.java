    public BigInteger hash(BigInteger... data) {
        ++hashCounter;
        this.md5.reset();
        for (int i = 0; i < data.length; i++) {
            this.md5.update(data[i].toByteArray());
        }
        byte[] erg = this.md5.digest();
        return new BigInteger(erg);
    }
