    public static void main(String[] args) {
        String tekst = "matnovak_2412matnovak_etl";
        MessageDigest m = null;
        byte[] data = tekst.getBytes();
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Kreiranje_baza_inicjalizacija.class.getName()).log(Level.SEVERE, null, ex);
        }
        m.update(data, 0, data.length);
        System.out.println("MD5: " + new BigInteger(1, m.digest()).toString(16));
        BigInteger i = new BigInteger(1, m.digest());
        System.out.println("MD5: " + String.format("%1$032X", i));
        kljuc_att_izvora("10,1,dnevnik2,datum");
    }
