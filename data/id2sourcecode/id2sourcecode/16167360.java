    public boolean validateMessage(byte[] signature, ProtocolData pd, EndpointReference epr, String[] aliasCanditates) {
        try {
            IDawareInputStream bbis = ((IDawareInputStream) (protocolDataToInputStream.get(pd)));
            byte[][] signedParts = bbis.getPartsByteArrays();
            if (signedParts == null) {
                Log.error("Message validation failed because the referred sections cound not be extracted!");
                return false;
            } else {
                protocolDataToInputStream.remove(pd);
            }
            Certificate cert = null;
            DeviceReference dRef = null;
            ServiceReference sRef = null;
            if ((dRef = DeviceServiceRegistry.getDeviceReference(epr, false)) != null) {
                try {
                    cert = (Certificate) dRef.getDevice().getCertificate();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            } else if ((sRef = DeviceServiceRegistry.getServiceReference(epr, pd.getCommunicationManagerId(), false)) != null) {
                try {
                    cert = (Certificate) sRef.getService().getCertificate();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; cert == null && aliasCanditates != null && i < aliasCanditates.length; i++) {
                cert = (Certificate) getCertificate(aliasCanditates[i]);
            }
            if (cert == null) {
                cert = (Certificate) getCertificate(epr.getAddress().toString());
                if (dRef != null) {
                    dRef.getDevice().setCertificate(cert);
                } else if (sRef != null) {
                    sRef.getService().setCertificate(cert);
                }
            }
            if (cert == null) {
                Log.error("Security: device/service uuid '" + epr.getAddress() + "' not found in the specified keystore!");
                return false;
            }
            MessageDigest digest = MessageDigest.getInstance("sha1");
            byte[][] digests = new byte[signedParts.length][];
            for (int i = 0; i < signedParts.length; i++) {
                digests[i] = digest.digest(signedParts[i]);
            }
            byte[] signedInfo = generateSignedInfo(digests, bbis.getIds());
            signedInfo = digest.digest(signedInfo);
            PublicKey pk = cert.getPublicKey();
            Signature s = Signature.getInstance("SHA1withRSA");
            s.initVerify(pk);
            s.update(signedInfo);
            if (s.verify(signature)) {
                Log.info("Discovery-Message validated!");
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Log.warn("Discovery-Message could not be validated!");
        return false;
    }
