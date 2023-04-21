    private CMSSignedData _getSignaturePkcs7(KeyStore kstOpen, String strAliasKpr, PrivateKey pkyPrivate, File fleOpenData, String strCmsSigGenDigest) throws Exception {
        java.security.cert.Certificate[] crtsChain = kstOpen.getCertificateChain(strAliasKpr);
        ArrayList certList = new ArrayList();
        for (int i = 0; i < crtsChain.length; i++) certList.add(crtsChain[i]);
        CertStore cseCerts = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), KTLAbs.f_s_strProviderKstBC);
        X509Certificate crt = (X509Certificate) kstOpen.getCertificate(strAliasKpr);
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        gen.addSigner(pkyPrivate, crt, strCmsSigGenDigest);
        gen.addCertificatesAndCRLs(cseCerts);
        InputStream ism = new FileInputStream(fleOpenData);
        byte[] bytsData = KTLKprOpenSigDetOCmsAbs._s_streamToByteArray(ism);
        CMSProcessable preData = new CMSProcessableByteArray(bytsData);
        CMSSignedData sdaSignature = gen.generate(preData, this._blnDataEncapsulated, KTLAbs.f_s_strProviderKstBC);
        return sdaSignature;
    }
