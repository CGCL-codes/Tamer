    public void writeEncryptedRSASamples(RSAPrivateKey pk, RSAPublicKey pubk) {
        Cipher rsa;
        int blksize;
        byte[] plainData;
        try {
            blksize = pubk.getModulus().toString(16).length() / 2 - 2;
            System.out.println("  Writing sample with RSA public key, blocksize=" + blksize);
            plainData = new String("This is a test encrypted with the public key\n " + "and decrypted with the private key!\n by jeg 3/25/98\n" + "This is the final line just to get a long string out").getBytes();
            rsa = Cipher.getInstance("RSA/ECB/NoPadding");
            rsa.init(Cipher.ENCRYPT_MODE, pubk);
            encryptAndWrite(new File(dirpathname, "sample.pub"), rsa, plainData, blksize);
            blksize = pk.getModulus().toString(16).length() / 2 - 2;
            System.out.println("  Writing sample with RSA private key, blocksize=" + blksize);
            plainData = new String("This is a test encrypted with the private key\n " + "and decrypted with the public key!\n by jeg 3/25/98\n" + "This is the final line just to get a long string out").getBytes();
            rsa = Cipher.getInstance("RSA/ECB/NoPadding");
            rsa.init(Cipher.ENCRYPT_MODE, pk);
            encryptAndWrite(new File(dirpathname, "sample.pri"), rsa, plainData, blksize);
        } catch (Exception e) {
            System.out.println("in writeEncryptedRSASamples: " + e);
        }
    }
