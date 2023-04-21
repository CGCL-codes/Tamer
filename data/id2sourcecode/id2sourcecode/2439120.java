    public static void main(String[] args) {
        SessionFactory factory;
        Session session;
        Atr atr = null;
        ;
        ApduCmd cmd = null;
        CardResponse response = null;
        byte[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        String MF = "3F00";
        try {
            Security.addProvider(new BouncyCastleProvider());
            factory = SessionFactory.getInstance();
            session = factory.createSession("default");
            System.out.println("Power on");
            atr = session.open();
            System.out.println(atr.toString());
            String cerID = "C111";
            String BsoID = "B1";
            String pubKeyObjID = "A1";
            String seID = "32";
            String PIN = "12345678";
            String pinID = "45";
            SiemensSigner s = new SiemensSigner();
            byte[] content = { 'l', 'a', 'r', 'a' };
            MessageDigest hash = MessageDigest.getInstance("SHA1");
            hash.update(content);
            byte[] digest = hash.digest();
            RSAPublicKeySpec pubKeySpec = s.generateBSO(PIN, pinID, BsoID, pubKeyObjID, session);
            if (pubKeySpec == null) return;
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            X509Certificate cert = s.generateX509Certificate(PIN, pinID, pubKey, BsoID, seID, session);
            FileOutputStream efoss = new FileOutputStream("card.cer");
            efoss.write(cert.getEncoded());
            efoss.close();
            s.storeX509Certificate(cerID, cert, session);
            s.sign(PIN, pinID, seID, BsoID, digest, session);
            System.out.println("Power off");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
