    private String generaHash(byte[] datos) throws Exception {
        MessageDigest dig = MessageDigest.getInstance(ConstantesRDS.HASH_ALGORITMO);
        return new String(Hex.encodeHex(dig.digest(datos)));
    }
