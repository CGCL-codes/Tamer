    public BlowfishEasy(byte[] passw) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nse) {
            throw new UnsupportedOperationException();
        }
        md.update(passw, 0, passw.length);
        byte[] bufOut = new byte[passw.length * 10];
        try {
            md.digest(bufOut, 0, bufOut.length);
        } catch (DigestException ex) {
            ex.printStackTrace();
        }
        this.bfc = new BlowfishCBC(bufOut, 0, bufOut.length, 0);
    }
