    @Override
    public Date verifyToken(byte[] timeStampToken, byte[] tsDigestInput) throws TimeStampTokenVerificationException {
        TimeStampToken tsToken;
        try {
            ContentInfo tsContentInfo = ContentInfo.getInstance(new ASN1InputStream(timeStampToken).readObject());
            tsToken = new TimeStampToken(tsContentInfo);
        } catch (IOException ex) {
            throw new TimeStampTokenStructureException("Error parsing encoded token", ex);
        } catch (TSPException ex) {
            throw new TimeStampTokenStructureException("Invalid token", ex);
        }
        X509Certificate tsaCert = null;
        try {
            Iterator certsIt = tsToken.getCertificates().getMatches(tsToken.getSID()).iterator();
            if (certsIt.hasNext()) {
                tsaCert = this.x509CertificateConverter.getCertificate((X509CertificateHolder) certsIt.next());
            }
            ValidationData vData = this.certificateValidationProvider.validate(x509CertSelectorConverter.getCertSelector(tsToken.getSID()), tsToken.getTimeStampInfo().getGenTime(), null == tsaCert ? null : Collections.singletonList(tsaCert));
            tsaCert = vData.getCerts().get(0);
        } catch (CertificateException ex) {
            throw new TimeStampTokenVerificationException(ex.getMessage(), ex);
        } catch (XAdES4jException ex) {
            throw new TimeStampTokenTSACertException("cannot validate TSA certificate", ex);
        }
        try {
            tsToken.validate(this.signerInfoVerifierBuilder.build(tsaCert));
        } catch (TSPValidationException ex) {
            throw new TimeStampTokenSignatureException("Invalid token signature or certificate", ex);
        } catch (Exception ex) {
            throw new TimeStampTokenVerificationException("Error when verifying the token signature", ex);
        }
        org.bouncycastle.tsp.TimeStampTokenInfo tsTokenInfo = tsToken.getTimeStampInfo();
        try {
            String digestAlgUri = uriForDigest(tsTokenInfo.getMessageImprintAlgOID());
            MessageDigest md = messageDigestProvider.getEngine(digestAlgUri);
            if (!Arrays.equals(md.digest(tsDigestInput), tsTokenInfo.getMessageImprintDigest())) {
                throw new TimeStampTokenDigestException();
            }
        } catch (UnsupportedAlgorithmException ex) {
            throw new TimeStampTokenVerificationException("The token's digest algorithm is not supported", ex);
        }
        return tsTokenInfo.getGenTime();
    }
