    public static void main(String[] args) {
        if ((args.length != 2) && (args.length != 3)) {
            printUsage();
            System.exit(1);
        }
        try {
            Module pkcs11Module = Module.getInstance(args[0]);
            pkcs11Module.initialize(null);
            Token token = Util.selectToken(pkcs11Module, output_, input_);
            if (token == null) {
                output_.println("We have no token to proceed. Finished.");
                output_.flush();
                System.exit(0);
            }
            Mechanism[] mechanisms = token.getMechanismList();
            Hashtable supportedMechanisms = new Hashtable(mechanisms.length);
            for (int i = 0; i < mechanisms.length; i++) {
                supportedMechanisms.put(mechanisms[i], mechanisms[i]);
            }
            MechanismInfo signatureMechanismInfo;
            if (supportedMechanisms.contains(Mechanism.RSA_PKCS)) {
                signatureMechanismInfo = token.getMechanismInfo(Mechanism.RSA_PKCS);
            } else {
                signatureMechanismInfo = null;
                output_.println("The token does not support mechanism RSA_PKCS. Going to exit.");
                System.exit(0);
            }
            if ((signatureMechanismInfo == null) || !signatureMechanismInfo.isSign()) {
                output_.println("The token does not support signing with mechanism RSA_PKCS. Going to exit.");
                System.exit(0);
            }
            Session session = Util.openAuthorizedSession(token, Token.SessionReadWriteBehavior.RO_SESSION, output_, input_);
            output_.println("################################################################################");
            output_.println("find private signature key");
            RSAPrivateKey templateSignatureKey = new RSAPrivateKey();
            templateSignatureKey.getSign().setBooleanValue(Boolean.TRUE);
            KeyAndCertificate selectedSignatureKeyAndCertificate = Util.selectKeyAndCertificate(session, templateSignatureKey, output_, input_);
            if (selectedSignatureKeyAndCertificate == null) {
                output_.println("We have no signature key to proceed. Finished.");
                output_.flush();
                System.exit(0);
            }
            Key signatureKey = selectedSignatureKeyAndCertificate.getKey();
            output_.println("################################################################################");
            output_.println("################################################################################");
            output_.println("signing data from file: " + args[1]);
            InputStream dataInputStream = new FileInputStream(args[1]);
            MessageDigest digestEngine = MessageDigest.getInstance("SHA-1");
            Mechanism signatureMechanism = Mechanism.RSA_PKCS;
            session.signInit(signatureMechanism, signatureKey);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = dataInputStream.read(dataBuffer)) >= 0) {
                digestEngine.update(dataBuffer, 0, bytesRead);
            }
            byte[] digest = digestEngine.digest();
            DigestInfo digestInfoObject = new DigestInfo(AlgorithmID.sha1, digest);
            byte[] digestInfo = digestInfoObject.toByteArray();
            byte[] signatureValue = session.sign(digestInfo);
            output_.println("The siganture value is: " + new BigInteger(1, signatureValue).toString(16));
            if (args.length == 3) {
                output_.println("Writing signature to file: " + args[2]);
                OutputStream signatureOutput = new FileOutputStream(args[2]);
                signatureOutput.write(signatureValue);
                signatureOutput.flush();
                signatureOutput.close();
            }
            output_.println("################################################################################");
            if ((signatureMechanismInfo == null) || !signatureMechanismInfo.isVerify()) {
                output_.println("The token does not support verification with mechanism RSA_PKCS. Going to exit.");
                System.exit(0);
            }
            boolean verifyInSoftware;
            output_.println("################################################################################");
            output_.println("find public verification key");
            RSAPublicKey templateVerificationKey = new RSAPublicKey();
            templateVerificationKey.getVerify().setBooleanValue(Boolean.TRUE);
            templateVerificationKey.getId().setByteArrayValue(signatureKey.getId().getByteArrayValue());
            session.findObjectsInit(templateVerificationKey);
            Object[] foundVerificationKeyObjects = session.findObjects(1);
            RSAPublicKey verificationKey = null;
            if (foundVerificationKeyObjects.length > 0) {
                verificationKey = (RSAPublicKey) foundVerificationKeyObjects[0];
                output_.println("________________________________________________________________________________");
                output_.println(verificationKey);
                output_.println("________________________________________________________________________________");
                verifyInSoftware = false;
            } else {
                if (selectedSignatureKeyAndCertificate.getCertificate() != null) {
                    output_.println("No matching public key found! Will verify in software.");
                } else {
                    output_.println("No matching public key found and no certificate found! Going to exit.");
                    System.exit(0);
                }
                verifyInSoftware = true;
            }
            session.findObjectsFinal();
            output_.println("################################################################################");
            output_.println("################################################################################");
            if (verifyInSoftware) {
                output_.println("verifying signature in software");
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                byte[] encodedCertificate = selectedSignatureKeyAndCertificate.getCertificate().getValue().getByteArrayValue();
                X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(encodedCertificate));
                Signature signatureEngine = Signature.getInstance("SHA1withRSA");
                signatureEngine.initVerify(certificate.getPublicKey());
                dataInputStream = new FileInputStream(args[1]);
                while ((bytesRead = dataInputStream.read(dataBuffer)) >= 0) {
                    signatureEngine.update(dataBuffer, 0, bytesRead);
                }
                try {
                    if (signatureEngine.verify(signatureValue)) {
                        output_.println("Verified the signature successfully");
                    } else {
                        output_.println("Signature Invalid.");
                    }
                } catch (SignatureException ex) {
                    output_.println("Verification FAILED: " + ex.getMessage());
                }
            } else {
                output_.println("verifying signature on token");
                dataInputStream = new FileInputStream(args[1]);
                while ((bytesRead = dataInputStream.read(dataBuffer)) >= 0) {
                    digestEngine.update(dataBuffer, 0, bytesRead);
                }
                digest = digestEngine.digest();
                digestInfoObject = new DigestInfo(AlgorithmID.sha1, digest);
                digestInfo = digestInfoObject.toByteArray();
                Mechanism verificationMechanism = Mechanism.RSA_PKCS;
                session.verifyInit(verificationMechanism, verificationKey);
                try {
                    session.verify(digestInfo, signatureValue);
                    output_.println("Verified the signature successfully");
                } catch (TokenException ex) {
                    output_.println("Verification FAILED: " + ex.getMessage());
                }
            }
            output_.println("################################################################################");
            session.closeSession();
            pkcs11Module.finalize(null);
        } catch (Throwable thr) {
            thr.printStackTrace();
        } finally {
            output_.close();
        }
    }
