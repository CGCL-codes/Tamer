    public static void main(String[] argv) throws Exception {
        KeyFactory factory = KeyFactoryImpl.getInstance();
        KeyPair dsa = factory.generateDSA(512);
        KeyPair rsa = factory.generateRSA(1024);
        SecureRandom r = new SecureRandom();
        String key = "374A2BD6D85F5FAA0B6FE06D2F4D8C240DAD83EA345EC1922553F40FEDCC3071DBF946FB454FF56BA7FAAD4A1C4163F57D220CAE6628BA767BE8C9E8B38A6AD91CC990A3AC230981A3D2CBC7E0355178871C50D5AAFB800FAC8965D1F9A9C2DD10DDE8A5CA01005991388068AFED7B0F0A097C38756FC48B38D5A390F43F9097B722CCE5506445A3412713293E99D414AB65D80FA9376DD4D8542B77A70A0A0DE4E16E8A5E718F05F6AD08A7EFA2CA0F5F93F3D90F5B6D0B81D133E6B59DE889";
        for (int i = 0; i < 30; i++) {
            KeyPair dhA = KeyFactoryImpl.getInstance().generateDHPublic(3);
            long total = 0;
            long time = System.nanoTime();
            KeyPair dhB = KeyFactoryImpl.getInstance().generateDHPublic(3);
            DHParameters dhParameters = DHParameters.getParams(3);
            PublicKey restoreApub = KeyFactoryImpl.getInstance().generateDHPublic(Helpers.hexStringToByteArray(key), dhParameters.getP().toByteArray(), dhParameters.getG().toByteArray());
            time = System.nanoTime();
            byte[] dhsN = factory.generateDHSecret(restoreApub, dhB.getPrivate());
            time = System.nanoTime() - time;
            System.out.print(time + "\n");
        }
        SecretKey desKey = factory.generateDES(112);
        SecretKey sha1Key = factory.generateSHA1(160);
        SecretKey md5Key = factory.generateMD5(112);
        byte[] ciphertext;
        DESCrypto desAlgo = new DESCrypto(desKey);
        ciphertext = desAlgo.encrypt("test".getBytes());
        RSACrypto rsaAlgo = new RSACrypto(rsa.getPublic(), rsa.getPrivate());
        System.out.println(new String(rsaAlgo.decrypt(ciphertext)));
        SecretKey aesKey = factory.generateAES(128);
        AESCrypto aesAlgo = new AESCrypto(aesKey);
        ciphertext = aesAlgo.encrypt("test".getBytes());
        System.out.println("length " + ciphertext.length);
        System.out.println(new String(aesAlgo.decrypt(ciphertext, aesAlgo.getIV())));
        HMACSHA1Digest sha1Algo = new HMACSHA1Digest(sha1Key);
        byte[] sig = sha1Algo.digest("test".getBytes());
        if (sha1Algo.verify(sig, "test".getBytes())) System.out.println("HMAC verified successfully"); else System.out.println("verified unsuccessfully");
        DSASignature dsaAlgo = new DSASignature();
        byte[] data = new byte[416];
        sig = dsaAlgo.sign(data, dsa.getPrivate());
        if (dsaAlgo.verify(sig, data, dsa.getPublic())) System.out.println("DSA verified successfully"); else System.out.println("DSA verified unsuccessfully");
        RSASignature rsaSigAlgo = new RSASignature();
        sig = rsaSigAlgo.sign("test".getBytes(), rsa.getPrivate());
        if (rsaSigAlgo.verify(sig, "test2".getBytes(), rsa.getPublic())) System.out.println("RSA verified successfully"); else System.out.println("RSA verified unsuccessfully");
        HMACMD5Digest md5Algo = new HMACMD5Digest(md5Key);
        sig = md5Algo.digest("test".getBytes());
        if (md5Algo.verify(sig, "testdas".getBytes())) System.out.println("HMACMD5 verified successfully"); else System.out.println("HMACMD5 verified unsuccessfully");
        SHA1Digest sha1md = new SHA1Digest();
        byte[] md = sha1md.digest("test".getBytes());
        if (sha1md.verify(md, "test".getBytes())) System.out.println("SHA1 matches");
        File dsaPubFile = new File("/home/dieman/workspace/cutehip/dsa.pub");
        File rsaPubFile = new File("/home/dieman/workspace/cutehip/rsa.pub");
        File dsaPrivFile = new File("/home/dieman/workspace/cutehip/dsa.priv");
        File rsaPrivFile = new File("/home/dieman/workspace/cutehip/rsa.priv");
        factory.saveToFile(dsa.getPublic(), dsaPubFile);
        factory.saveToFile(rsa.getPublic(), rsaPubFile);
        factory.saveToFile(dsa.getPrivate(), dsaPrivFile);
        factory.saveToFile(rsa.getPrivate(), rsaPrivFile);
        Key dsaPubLoad = factory.loadFromFile(dsaPubFile, KeyFactoryImpl.DSA_KEY, true);
        Key rsaPubLoad = factory.loadFromFile(rsaPubFile, KeyFactoryImpl.RSA_KEY, true);
        Key dsaPrivLoad = factory.loadFromFile(dsaPrivFile, KeyFactoryImpl.DSA_KEY, false);
        Key rsaPrivLoad = factory.loadFromFile(rsaPrivFile, KeyFactoryImpl.RSA_KEY, false);
        HostIdentity hi = new HostIdentity(rsa.getPublic().getEncoded(), HostIdentity.RSASHA1, (short) 0, (byte) 3);
        HostIdentityTag hit = new HostIdentityTag(hi);
        System.out.println("HIT as IPv6 address : " + hit.getAsAddress().getHostAddress());
        byte[] i = new byte[8];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(i);
        Puzzle puzzle = new Puzzle(i, null, hit.getAsBytes(), hit.getAsBytes(), 40);
        if (Puzzle.solve(puzzle, 1000 * 30)) {
            System.out.println("Puzzle soved");
            if (Arrays.equals(i, puzzle.getRandom())) {
                if (Puzzle.verify(puzzle)) System.out.println("Puzzle verified");
            } else {
                System.out.println("Random values differ");
            }
        }
    }
