    public boolean createKeyStore(String sUserName, String sKeyName, String sKeySize, String sFileName, String sPassword) {
        try {
            RSA rsa = new RSA();
            rsa.openKeyStore(null, "");
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom rand = new SecureRandom((sUserName + sKeyName + sKeySize + sFileName + DateFormat.getDateTimeInstance().format(new Date())).getBytes());
            generator.initialize(new Integer(sKeySize), rand);
            KeyPair keyPair = generator.generateKeyPair();
            for (X509Certificate cert : m_trustManager.getAcceptedIssuers()) {
                String sCert = cert.getSubjectX500Principal().getName();
                int Index1 = sCert.indexOf("CN=") + 3;
                int Index2 = sCert.indexOf(",", Index1);
                sCert = sCert.substring(Index1, Index2);
                rsa.addToKeyStore(sCert, cert);
            }
            X509Certificate cert = rsa.createCertificate(sKeyName, keyPair.getPublic(), keyPair.getPrivate());
            rsa.addToKeyStore(sKeyName, cert, keyPair.getPrivate(), sPassword);
            rsa.store(sFileName);
            return ClientMain.getInstance().registerClient(m_sHost, m_sPort, sUserName, keyPair.getPublic().getEncoded(), sFileName, sPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
