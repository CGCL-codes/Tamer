    public void TestMyNodeInfo() throws IOException {
        long fullid = Random.nextLong();
        Level[] l = new Level[31];
        for (int cnt = 0; cnt < 31; cnt++) {
            l[cnt] = new PfyshLevel(fullid, cnt);
        }
        AsymmetricCipherKeyPair keypair = genAsyncKeys();
        RSAKeyGenerationParameters rsaparms = new RSAKeyGenerationParameters(BigInteger.valueOf(311L), Random, 32, 10);
        RSAKeyPairGenerator rsagen = new RSAKeyPairGenerator();
        rsagen.init(rsaparms);
        AsymmetricCipherKeyPair rsapair = rsagen.generateKeyPair();
        PfyshNodePublicKeys ppk = new PfyshNodePublicKeys();
        ppk.setEncryptionKey(keypair.getPublic());
        ppk.setVerificationKey((RSAKeyParameters) rsapair.getPublic());
        PfyshNodePrivateKeys privk = new PfyshNodePrivateKeys();
        privk.setDecryptionKey((ElGamalPrivateKeyParameters) keypair.getPrivate());
        privk.setSignatureKey((RSAPrivateCrtKeyParameters) rsapair.getPrivate());
        byte[] bt = new byte[10];
        Random.nextBytes(bt);
        NodeHello nh = new NodeHello();
        nh.setConnectionLocation("over here man!");
        nh.setHelloNumber(23L);
        nh.setPublicKey(ppk);
        nh.setSignature(bt);
        MyNodeInfo my = new MyNodeInfo();
        my.setLevels(l);
        my.setPrivateKey(privk);
        my.setNode(nh);
        File f = Keeper.getKeeperFile("searchspec");
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        Utilities.writeMyNodeInfo(raf, my);
        raf.close();
        raf = new RandomAccessFile(f, "rw");
        MyNodeInfo my2 = Utilities.readMyNodeInfo(raf);
        raf.close();
        for (int cnt = 0; cnt < my.getLevels().length; cnt++) {
            if (!my.getLevels()[cnt].equals(my2.getLevels()[cnt])) {
                throw new RuntimeException("0 MyNodeInfo fail!");
            }
        }
        PfyshNodePrivateKeys myprivk = (PfyshNodePrivateKeys) my.getPrivateKey();
        PfyshNodePrivateKeys myprivk2 = (PfyshNodePrivateKeys) my2.getPrivateKey();
        ElGamalPrivateKeyParameters pr = (ElGamalPrivateKeyParameters) myprivk.getDecryptionKey();
        ElGamalPrivateKeyParameters pr2 = (ElGamalPrivateKeyParameters) myprivk2.getDecryptionKey();
        if (!pr.getX().equals(pr2.getX())) {
            throw new RuntimeException("4 GroupKeyInfo failed!");
        }
        if (pr.getParameters().getL() != pr2.getParameters().getL()) {
            throw new RuntimeException("5 GroupKeyInfo failed!");
        }
        if (!pr.getParameters().getG().equals(pr2.getParameters().getG())) {
            throw new RuntimeException("6 GroupKeyInfo failed!");
        }
        if (!pr.getParameters().getP().equals(pr2.getParameters().getP())) {
            throw new RuntimeException("7 GroupKeyInfo failed!");
        }
        RSAPrivateCrtKeyParameters rsa = myprivk.getSignatureKey();
        RSAPrivateCrtKeyParameters rsa2 = myprivk2.getSignatureKey();
        if (!rsa.getDP().equals(rsa2.getDP())) {
            throw new RuntimeException("blah");
        }
        if (!rsa.getDQ().equals(rsa2.getDQ())) {
            throw new RuntimeException("blah");
        }
        if (!rsa.getExponent().equals(rsa2.getExponent())) {
            throw new RuntimeException("blah");
        }
        if (!rsa.getModulus().equals(rsa2.getModulus())) {
            throw new RuntimeException("blah");
        }
        if (!rsa.getP().equals(rsa2.getP())) {
            throw new RuntimeException("blah");
        }
        if (!rsa.getPublicExponent().equals(rsa2.getPublicExponent())) {
            throw new RuntimeException("blah");
        }
        if (!rsa.getQ().equals(rsa2.getQ())) {
            throw new RuntimeException("blah");
        }
        if (!rsa.getQInv().equals(rsa2.getQInv())) {
            throw new RuntimeException("blah");
        }
        NodeHello n = my.getNode();
        NodeHello n2 = my2.getNode();
        if (!n.getConnectionLocation().equals(n2.getConnectionLocation())) {
            throw new RuntimeException("3.2 GroupKeyInfo failed!");
        }
        if (n.getHelloNumber() != n2.getHelloNumber()) {
            throw new RuntimeException("3.3 GroupKeyInfo failed!");
        }
        PfyshNodePublicKeys pnpk = (PfyshNodePublicKeys) n.getPublicKey();
        PfyshNodePublicKeys pnpk2 = (PfyshNodePublicKeys) n2.getPublicKey();
        RSAKeyParameters rkp = pnpk.getVerificationKey();
        RSAKeyParameters rkp2 = pnpk2.getVerificationKey();
        if (!rkp.getExponent().equals(rkp2.getExponent())) {
            throw new RuntimeException("3.3.1 GroupKeyInfo failed!");
        }
        if (!rkp.getModulus().equals(rkp2.getModulus())) {
            throw new RuntimeException("3.3.2 GroupKeyInfo failed!");
        }
        ElGamalPublicKeyParameters np = (ElGamalPublicKeyParameters) pnpk.getEncryptionKey();
        ElGamalPublicKeyParameters np2 = (ElGamalPublicKeyParameters) pnpk2.getEncryptionKey();
        if (!np.getY().equals(np2.getY())) {
            throw new RuntimeException("3.4 GroupKeyInfo failed!");
        }
        if (!np.getParameters().getG().equals(np2.getParameters().getG())) {
            throw new RuntimeException("3.5 GroupKeyInfo failed!");
        }
        if (!np.getParameters().getP().equals(np2.getParameters().getP())) {
            throw new RuntimeException("3.5 GroupKeyInfo failed!");
        }
        if (np.getParameters().getL() != np2.getParameters().getL()) {
            throw new RuntimeException("3.6 GroupKeyInfo failed!");
        }
        byte[] nb = (byte[]) n.getSignature();
        byte[] nb2 = (byte[]) n2.getSignature();
        if (!Arrays.equals(nb, nb2)) {
            throw new RuntimeException("3.7 GroupKeyInfo failed!");
        }
        System.out.println("TestMyNodeInfo PASS.");
    }
