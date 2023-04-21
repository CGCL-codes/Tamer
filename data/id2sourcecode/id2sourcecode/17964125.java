    public boolean selfTest() {
        if (valid == null) {
            valid = new Boolean(DIGEST0.equals(Util.toString(new Tiger().digest())));
        }
        return valid.booleanValue();
    }
