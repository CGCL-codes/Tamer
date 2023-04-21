    private static KeyPair getKeyPair(String filePath) {
        File filePrivateKey = new File(filePath + "_PRIVATE");
        File filePublicKey = new File(filePath + "_PUBLIC");
        KeyPair keys = null;
        boolean existPrivateKeyFile = filePrivateKey.exists();
        boolean existPublicKeyFile = filePublicKey.exists();
        if (!existPrivateKeyFile && !existPublicKeyFile) {
            PrintWriter printFilePrivate = null;
            PrintWriter printFilePublic = null;
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keys = keyPairGenerator.generateKeyPair();
                printFilePrivate = new PrintWriter(new FileWriter(filePrivateKey));
                printFilePublic = new PrintWriter(new FileWriter(filePublicKey));
                printFilePrivate.println(new sun.misc.BASE64Encoder().encode(keys.getPrivate().getEncoded()));
                printFilePublic.println(new sun.misc.BASE64Encoder().encode(keys.getPublic().getEncoded()));
            } catch (NoSuchAlgorithmException e) {
                err.println("Algoritmo criptogr?fico inv?lido.");
            } catch (IOException e) {
                err.println("Erro de I/O (Gravar em arquivo).");
            } finally {
                if (printFilePrivate != null) {
                    printFilePrivate.close();
                }
                if (printFilePublic != null) {
                    printFilePublic.close();
                }
            }
        } else {
            if (filePrivateKey.length() <= 1 || filePublicKey.length() <= 1) {
                filePrivateKey.delete();
                filePublicKey.delete();
                return getKeyPair(filePath);
            }
            String skeyPrivate = getKeyInFile(filePath + "_PRIVATE");
            String skeyPublic = getKeyInFile(filePath + "_PUBLIC");
            if (skeyPublic == null || skeyPrivate == null) {
                throw new NullPointerException("O arquivo n?o possui nenhuma \"key\"");
            }
            try {
                byte[] decodePrivateKey = new sun.misc.BASE64Decoder().decodeBuffer(skeyPrivate);
                byte[] decodePublicKey = new sun.misc.BASE64Decoder().decodeBuffer(skeyPublic);
                PKCS8EncodedKeySpec specPrivateKey = new PKCS8EncodedKeySpec(decodePrivateKey);
                X509EncodedKeySpec specPublicKey = new X509EncodedKeySpec(decodePublicKey);
                KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
                PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(specPrivateKey);
                PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(specPublicKey);
                keys = new KeyPair(publicKey, privateKey);
            } catch (Exception e) {
                err.println(e.getMessage());
            }
        }
        return keys;
    }
