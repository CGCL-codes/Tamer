    public boolean selfTest() {
        if (valid == null) {
            String d = Util.toString(new Whirlpool().digest());
            valid = Boolean.valueOf(DIGEST0.equals(d));
        }
        return valid.booleanValue();
    }
