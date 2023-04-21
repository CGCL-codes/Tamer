    public static String koduj(String tekst) {
        final byte[] dataToHash = tekst.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Wystąpił błąd wynikający z braku pakietu " + "odpowiedzialnego za tworzenie sumy kontrolnej!");
            e.printStackTrace();
        }
        md.update(dataToHash, 0, dataToHash.length);
        final byte[] b = md.digest();
        String hex = "";
        int msb;
        int lsb = 0;
        int i;
        for (i = 0; i < b.length; i++) {
            msb = (b[i] & 0x000000FF) / 16;
            lsb = (b[i] & 0x000000FF) % 16;
            hex = hex + hexChars[msb] + hexChars[lsb];
        }
        return hex;
    }
