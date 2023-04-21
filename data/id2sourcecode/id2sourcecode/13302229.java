    public boolean selfTest() {
        return DIGEST0.equals(toString(new MD4().digest()));
    }
