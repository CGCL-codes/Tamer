    public boolean selfTest() {
        if (valid == null) {
            valid = new Boolean(DIGEST0.equals(Util.toString(new Whirlpool2000().digest())));
        }
        return valid.booleanValue();
    }
