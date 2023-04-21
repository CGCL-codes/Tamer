    public void TestGroupKeyInfo() throws IOException {
        AsymmetricCipherKeyPair keypair = genAsyncKeys();
        AsymmetricCipherKeyPair keypair2 = genAsyncKeys();
        byte[] bt = new byte[20];
        byte[] bt2 = new byte[32];
        Random.nextBytes(bt);
        Random.nextBytes(bt2);
        RSAKeyGenerationParameters rsaparms = new RSAKeyGenerationParameters(BigInteger.valueOf(311L), Random, 32, 10);
        RSAKeyPairGenerator rsagen = new RSAKeyPairGenerator();
        rsagen.init(rsaparms);
        AsymmetricCipherKeyPair rsapair = rsagen.generateKeyPair();
        PfyshNodePublicKeys ppk = new PfyshNodePublicKeys();
        ppk.setEncryptionKey(keypair2.getPublic());
        ppk.setVerificationKey((RSAKeyParameters) rsapair.getPublic());
        NodeHello nh = new NodeHello();
        nh.setConnectionLocation("over here man!");
        nh.setHelloNumber(23L);
        nh.setPublicKey(ppk);
        nh.setSignature(bt2);
        GroupKey gk = new GroupKey();
        gk.setLevel(new PfyshLevel(Random.nextLong(), 10));
        gk.setPrivateKey(keypair.getPrivate());
        gk.setPublicKey(keypair.getPublic());
        gk.setSignature(bt);
        gk.setSourceNode(nh);
        GroupKeyInfo gki = new GroupKeyInfo(gk, Random.nextLong(), Random.nextLong());
        gki.setEncounters(10);
        gki.setReceivedTime(Random.nextLong());
        File f = Keeper.getKeeperFile("groupkey");
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        Utilities.writeGroupKeyInfo(raf, gki);
        raf.close();
        raf = new RandomAccessFile(f, "rw");
        GroupKeyInfo gki2 = Utilities.readGroupKeyInfo(raf);
        raf.close();
        if (gki.getEncounters() != gki2.getEncounters()) {
            throw new RuntimeException("0 GroupKeyInfo failed!");
        }
        if (gki.getReceivedTime() != gki2.getReceivedTime()) {
            throw new RuntimeException("1 GroupKeyInfo failed!");
        }
        if (!((Long) gki.getSourceNodeID()).equals((Long) gki2.getSourceNodeID())) {
            throw new RuntimeException("2 GroupKeyInfo failed!");
        }
        if (!gki.getGroupKey().getLevel().equals(gki2.getGroupKey().getLevel())) {
            throw new RuntimeException("3 GroupKeyInfo failed!");
        }
        byte[] b = (byte[]) gki.getGroupKey().getSignature();
        byte[] b2 = (byte[]) gki2.getGroupKey().getSignature();
        if (!Arrays.equals(b, b2)) {
            throw new RuntimeException("3.1 GroupKeyInfo failed!");
        }
        NodeHello n = gki.getGroupKey().getSourceNode();
        NodeHello n2 = gki2.getGroupKey().getSourceNode();
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
        ElGamalPrivateKeyParameters pr = (ElGamalPrivateKeyParameters) gki.getGroupKey().getPrivateKey();
        ElGamalPrivateKeyParameters pr2 = (ElGamalPrivateKeyParameters) gki2.getGroupKey().getPrivateKey();
        ElGamalPublicKeyParameters p = (ElGamalPublicKeyParameters) gki.getGroupKey().getPublicKey();
        ElGamalPublicKeyParameters p2 = (ElGamalPublicKeyParameters) gki2.getGroupKey().getPublicKey();
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
        if (!p.getY().equals(p2.getY())) {
            throw new RuntimeException("8 GroupKeyInfo failed!");
        }
        if (!p.getParameters().getG().equals(p2.getParameters().getG())) {
            throw new RuntimeException("9 GroupKeyInfo failed!");
        }
        if (!p.getParameters().getP().equals(p2.getParameters().getP())) {
            throw new RuntimeException("10 GroupKeyInfo failed!");
        }
        if (p.getParameters().getL() != p2.getParameters().getL()) {
            throw new RuntimeException("11 GroupKeyInfo failed!");
        }
        System.out.println("TestGroupKeyInfo PASS.");
    }
