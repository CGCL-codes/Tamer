    public void generateRSAKeyDisk() {
        KeyPair keyPair;
        System.out.println("Generating a RSA key disk for " + loginName + " with password " + password);
        keyPair = generateKeyPair(algorithm, numbits);
        RSAPrivateKey pk = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey pubk = (RSAPublicKey) keyPair.getPublic();
        writeEncryptedRSASamples(pk, pubk);
        writeX509Certificate(pk, pubk);
        try {
            EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(pk);
            encryptedPrivateKeyInfo.encrypt(password, AlgorithmID.pbeWithMD5AndDES_CBC, null);
            System.out.println("  Writing private key");
            OutputStream theKeyOutStream = new FileOutputStream(new File(dirpathname, "private"));
            encryptedPrivateKeyInfo.writeTo(theKeyOutStream);
            theKeyOutStream.close();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException: " + ex.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
