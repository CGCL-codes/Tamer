    protected boolean verifyTimestamp(TimeStampToken tst, SignerInformation si, byte[] documentDigest) throws SignatureProviderException, IOException, SignatureVerifyException {
        boolean timeStampVerified = false;
        try {
            byte signatureDigest[] = SHA1Util.digest(si.getSignature());
            if (tst != null) {
                CertStore certs = tst.getCertificatesAndCRLs("Collection", "BC");
                if (certs != null) {
                    Collection certificates = certs.getCertificates(tst.getSID());
                    if (certificates != null && certificates.size() > 0) {
                        X509Certificate timeStampCertificate = getTimeStampCertificates(certificates)[0];
                        try {
                            tst.validate(timeStampCertificate, "BC");
                            timeStampVerified = true;
                            TimeStampTokenInfo tsTokenInfo = tst.getTimeStampInfo();
                            byte[] hashTimeStamp = tsTokenInfo.getMessageImprintDigest();
                            timeStampVerified = timeStampVerified && hashTimeStamp.length == signatureDigest.length;
                            for (int i = 0; i < signatureDigest.length && timeStampVerified; i++) {
                                timeStampVerified = timeStampVerified && (hashTimeStamp[i] == signatureDigest[i]);
                            }
                        } catch (Exception e) {
                            throw new SignatureVerifyException(e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new SignatureVerifyException(e);
        }
        return timeStampVerified;
    }
