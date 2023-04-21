    private static byte[] calculateHash(final byte[] dataToHash) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Wystąpił błąd wynikający z braku pakietu " + "odpowiedzialnego za tworzenie sumy kontrolnej!");
            e.printStackTrace();
        }
        md.update(dataToHash, 0, dataToHash.length);
        return (md.digest());
    }
