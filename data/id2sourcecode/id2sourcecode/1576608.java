    public boolean selfTest() {
        if (valid == null) {
            Sha160 md = new Sha160();
            md.update((byte) 0x61);
            md.update((byte) 0x62);
            md.update((byte) 0x63);
            String result = Util.toString(md.digest());
            valid = new Boolean(DIGEST0.equals(result));
        }
        return valid.booleanValue();
    }
