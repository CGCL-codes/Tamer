    public void downloadCert(String label, String pintype, String pin, String basicpin, X509Certificate cert) throws ObjectAlreadyExistsException, OperationNotSupportedException, TokenException {
        Session session = null;
        Token token = (Token) tokens.get(pintype);
        Token basetoken = (Token) tokens.get(IToken.PINTYPE_BASIC);
        if (token == null) {
            throw new OperationNotSupportedException("PIN type not supported");
        }
        try {
            java.security.PublicKey publicKey = cert.getPublicKey();
            session = token.openSession(SessionType.SERIAL_SESSION, SessionReadWriteBehavior.RW_SESSION, null, null);
            if (!session.getSessionInfo().getState().toString().equals(State.RW_USER_FUNCTIONS.toString())) {
                session.login(UserType.USER, pin.toCharArray());
            }
            Object searchTemplate = null;
            if (publicKey.getAlgorithm().equalsIgnoreCase("RSA")) {
                java.security.interfaces.RSAPublicKey rsaPublicKey = (java.security.interfaces.RSAPublicKey) publicKey;
                RSAPrivateKey rsaPrivateKeySearchTemplate = new RSAPrivateKey();
                byte[] modulus = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(rsaPublicKey.getModulus());
                rsaPrivateKeySearchTemplate.getModulus().setByteArrayValue(modulus);
                searchTemplate = rsaPrivateKeySearchTemplate;
            }
            byte[] objectID = null;
            if (searchTemplate != null) {
                session.findObjectsInit(searchTemplate);
                Object[] foundKeyObjects = session.findObjects(1);
                if (foundKeyObjects.length > 0) {
                    Key foundKey = (Key) foundKeyObjects[0];
                    objectID = foundKey.getId().getByteArrayValue();
                }
                session.findObjectsFinal();
            }
            session.closeSession();
            session = basetoken.openSession(SessionType.SERIAL_SESSION, SessionReadWriteBehavior.RW_SESSION, null, null);
            if (!session.getSessionInfo().getState().toString().equals(State.RW_USER_FUNCTIONS.toString())) {
                session.login(UserType.USER, basicpin.toCharArray());
            }
            session.findObjectsInit(new X509PublicKeyCertificate());
            Object[] objects = session.findObjects(MAXNUMOBJECTS);
            for (int i = 0; i < objects.length; i++) {
                X509PublicKeyCertificate tokencert = (X509PublicKeyCertificate) objects[i];
                if (label.equals(new String(tokencert.getLabel().getCharArrayValue()))) {
                    throw new ObjectAlreadyExistsException("Certificate with label : " + label + " already exists on the token");
                }
            }
            session.findObjectsFinal();
            byte[] newObjectID = null;
            if (objectID == null) {
                if (publicKey instanceof java.security.interfaces.RSAPublicKey) {
                    newObjectID = ((java.security.interfaces.RSAPublicKey) publicKey).getModulus().toByteArray();
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");
                    newObjectID = digest.digest(newObjectID);
                } else {
                    newObjectID = CertUtils.getFingerprintAsString(cert).getBytes();
                }
            } else {
                newObjectID = objectID;
            }
            DERInteger dERInteger = new DERInteger(cert.getSerialNumber());
            byte[] serialNumber = dERInteger.getDEREncoded();
            try {
                X509PublicKeyCertificate certObject = new X509PublicKeyCertificate();
                certObject.getToken().setBooleanValue(Boolean.TRUE);
                certObject.getPrivate().setBooleanValue(Boolean.FALSE);
                certObject.getLabel().setCharArrayValue(label.toCharArray());
                certObject.getId().setByteArrayValue(newObjectID);
                certObject.getSubject().setByteArrayValue(cert.getSubjectDN().getName().getBytes());
                certObject.getIssuer().setByteArrayValue(cert.getIssuerDN().getName().getBytes());
                certObject.getSerialNumber().setByteArrayValue(serialNumber);
                certObject.getValue().setByteArrayValue(cert.getEncoded());
                session.createObject(certObject);
            } catch (CertificateEncodingException e) {
                throw new TokenException("Error in certificate encoding");
            }
        } catch (PKCS11Exception e) {
            throw new TokenException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new TokenException(e.getMessage(), e);
        } finally {
            if (session != null) {
                session.closeSession();
            }
        }
        clearCertificateCache();
    }
