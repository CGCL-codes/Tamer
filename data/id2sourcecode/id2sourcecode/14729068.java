    @SuppressWarnings("unchecked")
    public void run() {
        BigInteger P = new BigInteger("ef9a661517dbd96296e34994a9e7ef86c527d776c33eaabfcd8df59837205c074111ec5a1fd1dcb7e54b2f370f84f57121cc9caadbeff4859eac1540ce4d2afe979d9e47c7be440cd43eb542f4e54ad312b7b8d78ecff8906251322d230f3ac16a732d070a4151006546fda4f73f319a52670cb01fcfb601680a2287ebcb2223", 16);
        BigInteger G = new BigInteger("fb649c87d572d0b4bf5052644cab31a75613acc7b4414704c391995e70b09b16a29dade2079d551c9300f3f1ed747448977853c4583f84ec795a305959741a7792be5ef721c6a2ee4b743e252b487d9bc9e9a3393da5fc7a48223fe3d8d86c7fafc543b9462e4024038ac5c6ae2f10793354e163318bff8e03db54dad0044", 16);
        ElGamalParameters parms = new ElGamalParameters(P, G);
        byte[] bt = new byte[16];
        for (int cnt = 0; cnt < 16; cnt++) {
            bt[cnt] = (byte) cnt;
        }
        System.out.println("S: " + BytesToHex.bytesToHex(bt));
        byte[] tbytes = BytesToHex.hexToBytes("000102030405060708090A0B0C0D0E0F");
        if (!Arrays.equals(tbytes, bt)) {
            System.out.println("FAILED TO MATCH BYTES! " + BytesToHex.bytesToHex(tbytes));
        }
        Rand = new SecureRandom();
        PfyshCodecSettings set = new PfyshCodecSettings();
        set.setCertainty(Certainty);
        set.setFileKeeperDir("codecdir");
        set.setDeleteWipePasses(2);
        set.setPrivateKeySize(ElGamalKeySize);
        set.setRSACertainty(Certainty);
        set.setRSAPublicExponent(BigInteger.valueOf(311));
        set.setRSASize(RSAKeySize);
        PfyshCodec codec = new PfyshCodec(parms, set);
        Codec = codec;
        MyNode = new MyNodeInfo();
        NodeHello myhello = new NodeHello();
        myhello.setConnectionLocation("127.0.0.1");
        myhello.setHelloNumber(0);
        myhello.setSignature(new byte[32]);
        MyNode.setNode(myhello);
        Codec.setMyNodeInfo(MyNode);
        Codec.SignMyNodeInfo(MyNode);
        PfyshNodePublicKeys pub = (PfyshNodePublicKeys) MyNode.getNode().getPublicKey();
        System.out.println("Codec keys generated!");
        Codec.GenerateGroupKeys(MyNode, MyNode.getLevels()[4], this);
        byte[] testbytes = new byte[72];
        for (int cnt = 0; cnt < testbytes.length; cnt++) {
            testbytes[cnt] = (byte) cnt;
        }
        PfyshNodePrivateKeys priv = (PfyshNodePrivateKeys) MyNode.getPrivateKey();
        try {
            byte[] encbytes = BCUtils.EncryptHeader(testbytes, BCUtils.genK((ElGamalPublicKeyParameters) pub.getEncryptionKey(), Rand));
            System.out.println(">>> " + BytesToHex.bytesToHex(encbytes));
            byte[] decbytes = BCUtils.DecryptHeader(encbytes, priv.getDecryptionKey());
            if (!Arrays.equals(testbytes, decbytes)) {
                System.out.println("Header encrypt decrypt failed!");
                System.exit(0);
            }
        } catch (InvalidCipherTextException e1) {
            e1.printStackTrace();
        }
        GroupPrivateKeys = new LinkedList<ElGamalPrivateKeyParameters>();
        GroupPublicKeys = new LinkedList<ElGamalPublicKeyParameters>();
        System.out.println("ElGamal Parameters generated!");
        for (int cnt = 0; cnt < NumberKeys; cnt++) {
            ElGamalKeyGenerationParameters genparms = new ElGamalKeyGenerationParameters(Rand, ((ElGamalPublicKeyParameters) pub.getEncryptionKey()).getParameters());
            ElGamalKeyPairGenerator keygen = new ElGamalKeyPairGenerator();
            keygen.init(genparms);
            AsymmetricCipherKeyPair keypair = keygen.generateKeyPair();
            GroupPrivateKeys.add((ElGamalPrivateKeyParameters) keypair.getPrivate());
            GroupPublicKeys.add((ElGamalPublicKeyParameters) keypair.getPublic());
        }
        try {
            boolean pass = true;
            byte[] encbytes = BCUtils.EncryptHeader(testbytes, BCUtils.genK(GroupPublicKeys.get(0), Rand));
            byte[] decbytes = BCUtils.DecryptHeader(encbytes, GroupPrivateKeys.get(0));
            if (!Arrays.equals(decbytes, testbytes)) {
                System.out.println("Failed to decoded header!");
                System.exit(0);
            }
            File f = getTestFile();
            File encfile = File.createTempFile("blah", ".enc");
            Object key = BCUtils.getSymmetricKey(Rand);
            BCUtils.EncryptFile(f, encfile, key, false);
            File decfile = File.createTempFile("blah2", ".dec");
            BCUtils.DecryptFile(encfile, decfile, key, false);
            if (DiffFiles.diffFiles(decfile, f)) {
                System.out.println("Simple file encrypt decrypt passed!");
            } else {
                System.out.println("Simple file encrypt decrypt FAILED!");
                System.exit(0);
            }
            NodeHello hello = new NodeHello();
            NodeInfo info = new NodeInfo();
            info.setHello(hello);
            EncodedTransfer encxfer = new EncodedTransfer();
            encxfer.setHops(3);
            encxfer.setLayerDepth(2);
            encxfer.setPayload(null);
            EncodedTransferFromNode xfer = new EncodedTransferFromNode();
            xfer.setSourceNode(hello);
            xfer.setSourceNodeInfo(info);
            xfer.setTransfer(encxfer);
            xfer.getTransfer().setPayload(f);
            Codec.Decode(xfer, MyNode.getPrivateKey(), (List) GroupPrivateKeys, this);
            if (DecodeFailed != xfer) {
                pass = false;
                System.out.println("Bad decode test failed!!");
            }
            ResetAll();
            pass = DownloadTest();
            if (!pass) {
                System.out.println("DOWNLOAD FAILED!");
                System.exit(0);
            }
            ResetAll();
            pass = UploadTest();
            if (!pass) {
                System.out.println("UPLOAD FAILED!");
                System.exit(0);
            }
            ResetAll();
            pass = SearchRequestTest();
            if (!pass) {
                System.out.println("SEARCH FAILED!");
                System.exit(0);
            }
            ResetAll();
            pass = SearchStoreTest();
            if (!pass) {
                System.out.println("SEARCH STORE FAILED!");
                System.exit(0);
            }
            pass = SearchSpecTest();
            if (!pass) {
                System.out.println("SEARCH SPEC FAILED!");
                System.exit(0);
            }
            pass = TestSearchSpecHash();
            if (!pass) {
                System.out.println("SEARCH SPEC HASH FAILED!");
                System.exit(0);
            }
            pass = TestLevels();
            if (!pass) {
                System.out.println("LEVEL CHECK FAILED!");
                System.exit(0);
            }
            long id = (Long) Codec.GenerateFullID("Test search string");
            System.out.println("Search string fullid: " + id);
            pass = TestSignatures();
            if (!pass) {
                System.out.println("SIGNATURES FAILED!");
                System.exit(0);
            }
            PfyshLevel l0 = (PfyshLevel) Codec.getLevel(0x0123456789abcdefL, 4);
            PfyshLevel l1 = (PfyshLevel) Codec.getLevel(0x0113456789abcdefL, 4);
            System.out.println("L0: " + Long.toHexString((Long) l0.getID()));
            System.out.println("L1: " + Long.toHexString((Long) l1.getID()));
            List<Level> ls = Codec.getOtherLevels(l0, l1);
            if (ls.size() != 2) {
                System.out.println("1) Other levels failed! " + ls.size());
                for (int cnt = 0; cnt < ls.size(); cnt++) {
                    System.out.println(">> " + Long.toHexString((Long) ls.get(cnt).getID()));
                }
                System.exit(0);
            }
            if (ls.get(0).equals(ls.get(1))) {
                System.out.println("3) Other levels failed!");
                System.exit(0);
            }
            for (int cnt = 0; cnt < 2; cnt++) {
                PfyshLevel lt = (PfyshLevel) ls.get(cnt);
                if (!(lt.getID().equals(0x0130000000000000L) || lt.getID().equals(0x0100000000000000L))) {
                    System.out.println("2) Other levels failed!");
                    System.exit(0);
                }
            }
            if (pass) {
                System.out.println("Other levels passed!");
            }
            pass = TestGroupKeys();
            if (!pass) {
                System.out.println("TEST GROUP KEYS FAILED!");
                System.exit(0);
            }
            if (pass) {
                System.out.println(" --  ALL PASSED  --");
            } else {
                System.out.println(" -- TEST FAILED --");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
