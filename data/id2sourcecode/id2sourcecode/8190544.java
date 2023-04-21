    private String fingerPrint(final Key key) throws CryptException {
        final MD2 hash = new MD2();
        return hash.digest(key.getEncoded(), new HexConverter());
    }
