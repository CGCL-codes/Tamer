    public final byte getRandomValueFor(int[] tuple) {
        for (int i = 0; i < tuple.length; i++) bytes[i] = (byte) (tuple[i] & 0x000000FF);
        byte[] digest = md.digest(bytes);
        byte resultat = digest[0];
        for (int i = 1; i < digest.length; i++) resultat = (byte) (resultat ^ digest[i]);
        return resultat;
    }
