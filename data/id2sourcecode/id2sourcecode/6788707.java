    public static void main(String[] args) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
            keyGen.initialize(new ECGenParameterSpec("c2pnb163v1"));
            KeyPair keyPair1 = keyGen.generateKeyPair();
            KeyPair keyPair2 = keyGen.generateKeyPair();
            DG14File file1 = new DG14File((ECPublicKey) keyPair1.getPublic());
            HashMap<Integer, ECPublicKey> map = new HashMap<Integer, ECPublicKey>();
            map.put(new Integer(10), (ECPublicKey) keyPair1.getPublic());
            map.put(new Integer(20), (ECPublicKey) keyPair2.getPublic());
            System.out.println("File 1 : " + file1);
            DG14File file1parsed = new DG14File(new ByteArrayInputStream(file1.getEncoded()));
            System.out.println("File 1p: " + file1parsed);
            boolean res1 = Arrays.equals(file1.getEncoded(), file1parsed.getEncoded());
            System.out.println("res1: " + res1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
