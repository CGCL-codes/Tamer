    public boolean selfTest() {
        if (valid == null) {
            String d = Util.toString(new Tiger().digest());
            valid = Boolean.valueOf(DIGEST0.equals(d));
        }
        return valid.booleanValue();
    }
