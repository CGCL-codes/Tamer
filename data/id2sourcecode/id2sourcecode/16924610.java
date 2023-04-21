    public void signPdfWinCer(String src, String dest, boolean sign) throws IOException, DocumentException, GeneralSecurityException {
        String path = properties.getProperty("PRIVATE");
        String keystore_password = properties.getProperty("PASSWORD");
        String key_password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
        ks.load(new FileInputStream(path), keystore_password.toCharArray());
        String alias = (String) ks.aliases().nextElement();
        PrivateKey key = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
        appearance.setReason("External hash example");
        appearance.setLocation("Foobar");
        appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, "sig");
        appearance.setExternalDigest(null, new byte[20], null);
        appearance.preClose();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte buf[] = new byte[8192];
        int n;
        InputStream inp = appearance.getRangeStream();
        while ((n = inp.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
        byte hash[] = messageDigest.digest();
        PdfSigGenericPKCS sg = appearance.getSigStandard();
        PdfLiteral slit = (PdfLiteral) sg.get(PdfName.CONTENTS);
        byte[] outc = new byte[(slit.getPosLength() - 2) / 2];
        PdfPKCS7 sig = sg.getSigner();
        if (sign) {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(key);
            signature.update(hash);
            sig.setExternalDigest(signature.sign(), hash, "RSA");
        } else sig.setExternalDigest(null, hash, null);
        PdfDictionary dic = new PdfDictionary();
        byte[] ssig = sig.getEncodedPKCS7();
        System.arraycopy(ssig, 0, outc, 0, ssig.length);
        dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
        appearance.close(dic);
    }
