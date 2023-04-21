    public static void generate(String args[]) throws Throwable {
        if (args.length < 1) throw new IllegalArgumentException("missing second argument specifying output name");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
        generator.initialize(1024);
        KeyPair pair = generator.generateKeyPair();
        File privateKey = new File("pri" + args[1] + ".key");
        FileOutputStream fos1 = new FileOutputStream(privateKey);
        PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(pair.getPrivate().getEncoded());
        fos1.write(Base32.encode(spec1.getEncoded()).getBytes());
        fos1.close();
        File publicKey = new File("pub" + args[1] + ".key");
        FileOutputStream fos2 = new FileOutputStream(publicKey);
        X509EncodedKeySpec spec2 = new X509EncodedKeySpec(pair.getPublic().getEncoded());
        fos2.write(Base32.encode(spec2.getEncoded()).getBytes());
        fos2.close();
    }
