    public static String generaHash(String cad) throws Exception {
        byte[] datos = cad.getBytes(FirmaUtil.CHARSET);
        MessageDigest dig = MessageDigest.getInstance("SHA-512");
        return new String(encodeHex(dig.digest(datos)));
    }
