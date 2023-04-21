    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            CAReferenceField previousHolderRef = new CAReferenceField("SE", "PASSRD1", "00008");
            HolderReferenceField holderRef = new HolderReferenceField("SE", "PASSRD1", "00009");
            String algorithmName = "SHA256WITHRSAANDMGF1";
            CVCertificate request = CertificateGenerator.createRequest(keyPair, algorithmName, holderRef);
            System.out.println(request.getAsText());
            CVCAuthenticatedRequest authRequest = CertificateGenerator.createAuthenticatedRequest(request, keyPair, algorithmName, previousHolderRef);
            System.out.println(authRequest.getAsText());
            FileHelper.writeFile(new File("C:/cv_certs/request1.cvcert"), authRequest.getDEREncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
