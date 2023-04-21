    public boolean selfTest() {
        if (valid == null) {
            valid = new Boolean(DIGEST0.equals(Util.toString(new MD2().digest())));
        }
        return valid.booleanValue();
    }
