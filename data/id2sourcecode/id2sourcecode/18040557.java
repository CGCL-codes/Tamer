    public boolean selfTest() {
        if (valid == null) {
            valid = new Boolean(DIGEST0.equals(Util.toString(new Haval().digest())));
        }
        return valid.booleanValue();
    }
