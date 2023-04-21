    public static void timestamp(PdfSignatureAppearance sap, TSAClient tsa, String signatureName) throws Exception {
        int contentEstimated = tsa.getTokenSizeEstimate();
        sap.setVisibleSignature(new Rectangle(0, 0, 0, 0), 1, signatureName);
        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ETSI_RFC3161);
        dic.put(PdfName.TYPE, PdfName.DOCTIMESTAMP);
        sap.setCryptoDictionary(dic);
        HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
        exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
        sap.preClose(exc);
        InputStream data = sap.getRangeStream();
        MessageDigest messageDigest = MessageDigest.getInstance(tsa.getDigestAlgorithm());
        byte[] buf = new byte[4096];
        int n;
        while ((n = data.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
        byte[] tsImprint = messageDigest.digest();
        byte[] tsToken = tsa.getTimeStampToken(tsImprint);
        if (contentEstimated + 2 < tsToken.length) throw new Exception("Not enough space");
        byte[] paddedSig = new byte[contentEstimated];
        System.arraycopy(tsToken, 0, paddedSig, 0, tsToken.length);
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        sap.close(dic2);
    }
