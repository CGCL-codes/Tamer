    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }
        try {
            Security.addProvider(new IAIK());
            Module pkcs11Module = Module.getInstance(args[0]);
            pkcs11Module.initialize(null);
            Slot[] slots = pkcs11Module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);
            if (slots.length == 0) {
                output_.println("No slot with present token found!");
                System.exit(0);
            }
            Slot selectedSlot = slots[0];
            Token token = selectedSlot.getToken();
            TokenInfo tokenInfo = token.getTokenInfo();
            output_.println("################################################################################");
            output_.println("Information of Token:");
            output_.println(tokenInfo);
            output_.println("################################################################################");
            Session session = Util.openAuthorizedSession(token, Token.SessionReadWriteBehavior.RW_SESSION, output_, input_);
            output_.println("################################################################################");
            output_.print("Generating new 1024 bit DSA parameters (in software)... ");
            output_.flush();
            AlgorithmParameterGenerator parameterGenerator = AlgorithmParameterGenerator.getInstance("DSA");
            parameterGenerator.init(1024);
            AlgorithmParameters parameters = parameterGenerator.generateParameters();
            DSAParameterSpec parameterSpec = (DSAParameterSpec) parameters.getParameterSpec(DSAParameterSpec.class);
            output_.println("Success");
            output_.println("P: " + Functions.toHexString(parameterSpec.getP().toByteArray()));
            output_.println("Q: " + Functions.toHexString(parameterSpec.getQ().toByteArray()));
            output_.println("G: " + Functions.toHexString(parameterSpec.getG().toByteArray()));
            output_.println("################################################################################");
            output_.println("################################################################################");
            output_.print("Generating new 1024 bit DSA key-pair... ");
            output_.flush();
            HashSet supportedMechanisms = new HashSet(Arrays.asList(token.getMechanismList()));
            MechanismInfo signatureMechanismInfo;
            if (supportedMechanisms.contains(Mechanism.DSA)) {
                signatureMechanismInfo = token.getMechanismInfo(Mechanism.DSA);
            } else {
                signatureMechanismInfo = null;
            }
            Mechanism keyPairGenerationMechanism = Mechanism.DSA_KEY_PAIR_GEN;
            DSAPublicKey dsaPublicKeyTemplate = new DSAPublicKey();
            DSAPrivateKey dsaPrivateKeyTemplate = new DSAPrivateKey();
            dsaPublicKeyTemplate.getPrime().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(parameterSpec.getP()));
            dsaPublicKeyTemplate.getSubprime().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(parameterSpec.getQ()));
            dsaPublicKeyTemplate.getBase().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(parameterSpec.getG()));
            dsaPublicKeyTemplate.getToken().setBooleanValue(Boolean.TRUE);
            byte[] id = new byte[20];
            new Random().nextBytes(id);
            dsaPublicKeyTemplate.getId().setByteArrayValue(id);
            dsaPrivateKeyTemplate.getSensitive().setBooleanValue(Boolean.TRUE);
            dsaPrivateKeyTemplate.getToken().setBooleanValue(Boolean.TRUE);
            dsaPrivateKeyTemplate.getPrivate().setBooleanValue(Boolean.TRUE);
            dsaPrivateKeyTemplate.getId().setByteArrayValue(id);
            if (signatureMechanismInfo != null) {
                dsaPublicKeyTemplate.getVerify().setBooleanValue(new Boolean(signatureMechanismInfo.isVerify()));
                dsaPublicKeyTemplate.getVerifyRecover().setBooleanValue(new Boolean(signatureMechanismInfo.isVerifyRecover()));
                dsaPublicKeyTemplate.getEncrypt().setBooleanValue(new Boolean(signatureMechanismInfo.isEncrypt()));
                dsaPublicKeyTemplate.getDerive().setBooleanValue(new Boolean(signatureMechanismInfo.isDerive()));
                dsaPublicKeyTemplate.getWrap().setBooleanValue(new Boolean(signatureMechanismInfo.isWrap()));
                dsaPrivateKeyTemplate.getSign().setBooleanValue(new Boolean(signatureMechanismInfo.isSign()));
                dsaPrivateKeyTemplate.getSignRecover().setBooleanValue(new Boolean(signatureMechanismInfo.isSignRecover()));
                dsaPrivateKeyTemplate.getDecrypt().setBooleanValue(new Boolean(signatureMechanismInfo.isDecrypt()));
                dsaPrivateKeyTemplate.getDerive().setBooleanValue(new Boolean(signatureMechanismInfo.isDerive()));
                dsaPrivateKeyTemplate.getUnwrap().setBooleanValue(new Boolean(signatureMechanismInfo.isUnwrap()));
            } else {
                dsaPrivateKeyTemplate.getSign().setBooleanValue(Boolean.TRUE);
                dsaPublicKeyTemplate.getVerify().setBooleanValue(Boolean.TRUE);
            }
            dsaPublicKeyTemplate.getKeyType().setPresent(false);
            dsaPublicKeyTemplate.getObjectClass().setPresent(false);
            dsaPrivateKeyTemplate.getKeyType().setPresent(false);
            dsaPrivateKeyTemplate.getObjectClass().setPresent(false);
            KeyPair generatedKeyPair = session.generateKeyPair(keyPairGenerationMechanism, dsaPublicKeyTemplate, dsaPrivateKeyTemplate);
            DSAPublicKey generatedDSAPublicKey = (DSAPublicKey) generatedKeyPair.getPublicKey();
            DSAPrivateKey generatedDSAPrivateKey = (DSAPrivateKey) generatedKeyPair.getPrivateKey();
            output_.println("Success");
            output_.println("The public key is");
            output_.println("_______________________________________________________________________________");
            output_.println(generatedDSAPublicKey);
            output_.println("_______________________________________________________________________________");
            output_.println("The private key is");
            output_.println("_______________________________________________________________________________");
            output_.println(generatedDSAPrivateKey);
            output_.println("_______________________________________________________________________________");
            output_.println("################################################################################");
            output_.println("Writing the public key of the generated key-pair to file: " + args[1]);
            DSAPublicKey exportableDsaPublicKey = generatedDSAPublicKey;
            BigInteger p = new BigInteger(1, exportableDsaPublicKey.getPrime().getByteArrayValue());
            BigInteger q = new BigInteger(1, exportableDsaPublicKey.getSubprime().getByteArrayValue());
            BigInteger g = new BigInteger(1, exportableDsaPublicKey.getBase().getByteArrayValue());
            BigInteger y = new BigInteger(1, exportableDsaPublicKey.getValue().getByteArrayValue());
            DSAPublicKeySpec rsaPublicKeySpec = new DSAPublicKeySpec(y, p, q, g);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            java.security.interfaces.DSAPublicKey javaDsaPublicKey = (java.security.interfaces.DSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
            X509EncodedKeySpec x509EncodedPublicKey = (X509EncodedKeySpec) keyFactory.getKeySpec(javaDsaPublicKey, X509EncodedKeySpec.class);
            FileOutputStream publicKeyFileStream = new FileOutputStream(args[1]);
            publicKeyFileStream.write(x509EncodedPublicKey.getEncoded());
            publicKeyFileStream.flush();
            publicKeyFileStream.close();
            output_.println("################################################################################");
            output_.println("################################################################################");
            output_.println("Trying to search for the public key of the generated key-pair by ID: " + Functions.toHexString(id));
            DSAPublicKey exportDsaPublicKeyTemplate = new DSAPublicKey();
            exportDsaPublicKeyTemplate.getId().setByteArrayValue(id);
            session.findObjectsInit(exportDsaPublicKeyTemplate);
            Object[] foundPublicKeys = session.findObjects(1);
            session.findObjectsFinal();
            if (foundPublicKeys.length != 1) {
                output_.println("Error: Cannot find the public key under the given ID!");
            } else {
                output_.println("Found public key!");
                output_.println("_______________________________________________________________________________");
                output_.println(foundPublicKeys[0]);
                output_.println("_______________________________________________________________________________");
            }
            output_.println("################################################################################");
            session.closeSession();
            pkcs11Module.finalize(null);
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
    }
