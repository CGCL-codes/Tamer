    public void sign(PrivateKey key, X509Certificate cert, String sigAlg, String alias, Collection names) throws IOException, GeneralSecurityException {
        ManifestDigester digester;
        Manifest sf;
        if (mf_ == null) {
            lock();
        }
        sf = new Manifest(dAlgs_, dAlgs_, 0);
        digester = new ManifestDigester(sf);
        digester.digest(names, mf_);
        signSF(key, cert, sigAlg, alias, sf);
    }
