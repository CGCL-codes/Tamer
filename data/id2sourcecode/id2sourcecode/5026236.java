    public CMSSignedData addTimestamp(X509Certificate cert, CMSSignedData signedData) throws IOException, TSPException, SignatureTimestampException {
        Collection ss = signedData.getSignerInfos().getSigners();
        SignerInformation si = (SignerInformation) ss.iterator().next();
        byte digest[];
        try {
            digest = SHA1Util.digest(si.getSignature());
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureTimestampException(e);
        } catch (NoSuchProviderException e) {
            throw new SignatureTimestampException(e);
        }
        return addTimestamp(cert, signedData, digest, TSPAlgorithms.SHA1);
    }
