    public String md5_funk(String tekst) {
        MessageDigest m = null;
        byte[] data = tekst.getBytes();
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Greška u kreiranju md5 sažetka");
        }
        m.update(data, 0, data.length);
        return new BigInteger(1, m.digest()).toString(16);
    }
