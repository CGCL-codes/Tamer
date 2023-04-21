    public void signPdf(String src, String dest, boolean detached) throws IOException, DocumentException, GeneralSecurityException, CMSException {
        String path = properties.getProperty("PRIVATE");
        String keystore_password = properties.getProperty("PASSWORD");
        String key_password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
        ks.load(new FileInputStream(path), keystore_password.toCharArray());
        String alias = (String) ks.aliases().nextElement();
        PrivateKey key = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        PdfReader reader = new PdfReader(src);
        PdfStamper stp = PdfStamper.createSignature(reader, new FileOutputStream(dest), '\0');
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, null);
        sap.setSignDate(new GregorianCalendar());
        sap.setCrypto(null, chain, null, null);
        sap.setAcro6Layers(true);
        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
        PdfSignature dic;
        if (detached) dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED); else dic = new PdfSignature(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_SHA1);
        dic.setDate(new PdfDate(sap.getSignDate()));
        dic.setName(PdfPKCS7.getSubjectFields((X509Certificate) chain[0]).getField("CN"));
        dic.setReason("Signed with BC");
        dic.setLocation("Foobar");
        sap.setCryptoDictionary(dic);
        int csize = 4000;
        HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
        exc.put(PdfName.CONTENTS, new Integer(csize * 2 + 2));
        sap.preClose(exc);
        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        generator.addSigner(key, (X509Certificate) chain[0], CMSSignedDataGenerator.DIGEST_SHA1);
        ArrayList<Certificate> list = new ArrayList<Certificate>();
        for (int i = 0; i < chain.length; i++) {
            list.add(chain[i]);
        }
        CertStore chainStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(list), "BC");
        generator.addCertificatesAndCRLs(chainStore);
        CMSSignedData signedData;
        if (detached) {
            CMSProcessable content = new CMSProcessableRange(sap);
            signedData = generator.generate(content, false, "BC");
        } else {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            InputStream s = sap.getRangeStream();
            int read = 0;
            byte[] buff = new byte[8192];
            while ((read = s.read(buff, 0, 8192)) > 0) {
                md.update(buff, 0, read);
            }
            CMSProcessable content = new CMSProcessableByteArray(md.digest());
            signedData = generator.generate(content, true, "BC");
        }
        byte[] pk = signedData.getEncoded();
        byte[] outc = new byte[csize];
        PdfDictionary dic2 = new PdfDictionary();
        System.arraycopy(pk, 0, outc, 0, pk.length);
        dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
        sap.close(dic2);
    }
