    public void sign(PrivateKey key, X509Certificate cert, String sigAlg, String alias) throws IOException, GeneralSecurityException {
        ManifestDigester digester;
        Manifest sf;
        try {
            if (mf_ == null) {
                lock();
            }
            sf = new Manifest(dAlgs_, dAlgs_, 0);
            digester = new ManifestDigester(sf);
            digester.digest(mf_);
            signSF(key, cert, sigAlg, alias, sf);
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("Digest or signature algorithm not available!");
        }
    }
