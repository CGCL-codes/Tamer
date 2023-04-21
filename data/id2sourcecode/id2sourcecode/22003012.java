    public byte[] getDocumentHash(InputStream document, File preparedDoc, Certificate certificate, SignMetadata signMetadata, String documentId) {
        logger.debug("getDocumentHash(%s, %s, %s)", document, preparedDoc, certificate);
        try {
            PdfReader pdfReader = new PdfReader(document);
            FileOutputStream fout = new FileOutputStream(preparedDoc);
            PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, fout, '\0', null, true);
            if (getPDFXConformance(pdfReader) == PdfWriter.PDFA1B) pdfStamper.getWriter().setPDFXConformance(PdfWriter.PDFA1B);
            PdfSignatureAppearance sap = pdfStamper.getSignatureAppearance();
            Certificate[] certs = new Certificate[1];
            certs[0] = certificate;
            sap.setCrypto(null, certs, null, PdfSignatureAppearance.WINCER_SIGNED);
            sap.setVisibleSignature(signMetadata.getSignField());
            sap.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            addSignMetadata(certificate, sap, signMetadata);
            HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
            exc.put(PdfName.CONTENTS, Integer.valueOf(ENCODED_SIGNATURE_LENGTH + 2));
            sap.preClose(exc);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte buf[] = new byte[8192];
            int n;
            InputStream inp = sap.getRangeStream();
            while ((n = inp.read(buf)) > 0) {
                messageDigest.update(buf, 0, n);
            }
            byte[] hash = messageDigest.digest();
            PdfDictionary dic2 = new PdfDictionary();
            dic2.put(PdfName.CONTENTS, new PdfString(PdfSignerUtils.getPlaceHolderArr(ENCODED_SIGNATURE_LENGTH)).setHexWriting(true));
            sap.close(dic2);
            return hash;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(String.format("Error calculating document '%s' sign field '%s' hash", documentId, signMetadata.getSignField()));
        }
    }
