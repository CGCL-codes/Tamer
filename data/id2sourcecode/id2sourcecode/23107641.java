    public void signPdf(String src, String dest, boolean withTS, boolean withOCSP) throws IOException, DocumentException, GeneralSecurityException {
        String keystore = properties.getProperty("PRIVATE");
        String password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
        ks.load(new FileInputStream(keystore), password.toCharArray());
        String alias = (String) ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        PdfReader reader = new PdfReader(src);
        FileOutputStream fout = new FileOutputStream(dest);
        PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, "Signature");
        sap.setCrypto(null, chain, null, PdfSignatureAppearance.SELF_SIGNED);
        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
        dic.setReason(sap.getReason());
        dic.setLocation(sap.getLocation());
        dic.setContact(sap.getContact());
        dic.setDate(new PdfDate(sap.getSignDate()));
        sap.setCryptoDictionary(dic);
        int contentEstimated = 15000;
        HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
        exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
        sap.preClose(exc);
        InputStream data = sap.getRangeStream();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte buf[] = new byte[8192];
        int n;
        while ((n = data.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
        byte hash[] = messageDigest.digest();
        Calendar cal = Calendar.getInstance();
        TSAClient tsc = null;
        if (withTS) {
            String tsa_url = properties.getProperty("TSA");
            String tsa_login = properties.getProperty("TSA_LOGIN");
            String tsa_passw = properties.getProperty("TSA_PASSWORD");
            tsc = new TSAClientBouncyCastle(tsa_url, tsa_login, tsa_passw);
        }
        byte[] ocsp = null;
        if (withOCSP) {
            String url = PdfPKCS7.getOCSPURL((X509Certificate) chain[0]);
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            FileInputStream is = new FileInputStream(properties.getProperty("ROOTCERT"));
            X509Certificate root = (X509Certificate) cf.generateCertificate(is);
            ocsp = new OcspClientBouncyCastle().getEncoded((X509Certificate) chain[0], root, url);
        }
        PdfPKCS7 sgn = new PdfPKCS7(pk, chain, null, "SHA1", null, false);
        byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp);
        sgn.update(sh, 0, sh.length);
        byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, tsc, ocsp);
        if (contentEstimated + 2 < encodedSig.length) throw new DocumentException("Not enough space");
        byte[] paddedSig = new byte[contentEstimated];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        sap.close(dic2);
    }
