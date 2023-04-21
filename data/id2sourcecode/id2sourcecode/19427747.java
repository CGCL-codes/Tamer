    public static Certificate[] verifySignature(InputStream signature, InputStream signatureBlock) throws IOException, GeneralSecurityException {
        BerInputStream bis = new BerInputStream(signatureBlock);
        ContentInfo info = (ContentInfo) ContentInfo.ASN1.decode(bis);
        SignedData signedData = info.getSignedData();
        if (signedData == null) {
            throw new IOException(Messages.getString("security.173"));
        }
        Collection encCerts = signedData.getCertificates();
        if (encCerts.isEmpty()) {
            return null;
        }
        X509Certificate[] certs = new X509Certificate[encCerts.size()];
        int i = 0;
        for (Iterator it = encCerts.iterator(); it.hasNext(); ) {
            certs[i++] = new X509CertImpl((org.apache.harmony.security.x509.Certificate) it.next());
        }
        List sigInfos = signedData.getSignerInfos();
        SignerInfo sigInfo;
        if (!sigInfos.isEmpty()) {
            sigInfo = (SignerInfo) sigInfos.get(0);
        } else {
            return null;
        }
        X500Principal issuer = sigInfo.getIssuer();
        BigInteger snum = sigInfo.getSerialNumber();
        int issuerSertIndex = 0;
        for (i = 0; i < certs.length; i++) {
            if (issuer.equals(certs[i].getIssuerDN()) && snum.equals(certs[i].getSerialNumber())) {
                issuerSertIndex = i;
                break;
            }
        }
        if (i == certs.length) {
            return null;
        }
        if (certs[issuerSertIndex].hasUnsupportedCriticalExtension()) {
            throw new SecurityException(Messages.getString("security.174"));
        }
        Signature sig = null;
        String da = sigInfo.getdigestAlgorithm();
        String dea = sigInfo.getDigestEncryptionAlgorithm();
        String alg = null;
        if (da != null && dea != null) {
            alg = da + "with" + dea;
            try {
                sig = Signature.getInstance(alg);
            } catch (NoSuchAlgorithmException e) {
            }
        }
        if (sig == null) {
            alg = da;
            if (alg == null) {
                return null;
            }
            try {
                sig = Signature.getInstance(alg);
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }
        sig.initVerify(certs[issuerSertIndex]);
        List atr = sigInfo.getAuthenticatedAttributes();
        byte[] sfBytes = InputStreamHelper.readFullyAndClose(signature);
        if (atr == null) {
            sig.update(sfBytes);
        } else {
            sig.update(sigInfo.getEncodedAuthenticatedAttributes());
            byte[] existingDigest = null;
            for (Iterator it = atr.iterator(); it.hasNext(); ) {
                AttributeTypeAndValue a = (AttributeTypeAndValue) it.next();
                if (Arrays.equals(a.getType().getOid(), MESSAGE_DIGEST_OID)) {
                }
            }
            if (existingDigest != null) {
                MessageDigest md = MessageDigest.getInstance(sigInfo.getDigestAlgorithm());
                byte[] computedDigest = md.digest(sfBytes);
                if (!Arrays.equals(existingDigest, computedDigest)) {
                    throw new SecurityException(Messages.getString("security.175"));
                }
            }
        }
        if (!sig.verify(sigInfo.getEncryptedDigest())) {
            throw new SecurityException(Messages.getString("security.176"));
        }
        return createChain(certs[issuerSertIndex], certs);
    }
