    private void createSignatureAppearance(MyPkcs11 mySign, Session session, String reason, Key signCertKeyObject, X509Certificate[] certs, PdfStamperOSP stamper, boolean signatureVisible, Calendar cal) throws IOException, DocumentException, NoSuchAlgorithmException, TokenException, ExceptionConverter, NoSuchProviderException {
        logger.info("[createSignatureAppearance.in]:: ");
        byte[] signatureBytes = new byte[128];
        PdfSignatureAppearanceOSP sap = stamper.getSignatureAppearance();
        sap.setCrypto(null, certs, null, PdfSignatureAppearance.WINCER_SIGNED);
        sap.setReason(reason);
        if (signatureVisible) {
            if ("countersigner".equals(typeSignatureSelected)) {
                sap.setCertified(0);
                sap.setVisibleSignature(fieldName);
            } else {
                sap.setCertified(0);
                if ((fieldName != null) && (!"".equals(fieldName))) {
                    sap.setVisibleSignature(fieldName);
                } else {
                    sap.setVisibleSignature(new com.lowagie.text.Rectangle(340, 600, 560, 700), 1, null);
                }
            }
        }
        sap.setExternalDigest(new byte[128], new byte[20], "RSA");
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.FT, PdfName.SIG);
        dic.put(PdfName.FILTER, new PdfName("Adobe.PPKLite"));
        dic.put(PdfName.SUBFILTER, new PdfName("adbe.pkcs7.detached"));
        if (cal != null) {
            dic.put(PdfName.M, new PdfDate(cal));
        } else {
            dic.put(PdfName.M, new PdfNull());
        }
        dic.put(PdfName.NAME, new PdfString(PdfPKCS7.getSubjectFields((X509Certificate) certs[0]).getField("CN")));
        dic.put(PdfName.REASON, new PdfString(reason));
        sap.setCryptoDictionary(dic);
        HashMap exc = new HashMap();
        exc.put(PdfName.CONTENTS, new Integer(0x5002));
        sap.preClose(exc);
        byte[] content = IOUtils.streamToByteArray(sap.getRangeStream());
        byte[] hash = MessageDigest.getInstance("2.16.840.1.101.3.4.2.1", "BC").digest(content);
        ASN1EncodableVector signedAttributes = buildSignedAttributes(hash, cal);
        byte[] bytesForSecondHash = IOUtils.toByteArray(new DERSet(signedAttributes));
        byte[] secondHash = MessageDigest.getInstance("2.16.840.1.101.3.4.2.1").digest(bytesForSecondHash);
        signatureBytes = mySign.sign(session, secondHash, signCertKeyObject);
        byte[] encodedPkcs7 = null;
        try {
            DERConstructedSet digestAlgorithms = new DERConstructedSet();
            ASN1EncodableVector algos = new ASN1EncodableVector();
            algos.add(new DERObjectIdentifier("2.16.840.1.101.3.4.2.1"));
            algos.add(new DERNull());
            digestAlgorithms.addObject(new DERSequence(algos));
            ASN1EncodableVector ev = new ASN1EncodableVector();
            ev.add(new DERObjectIdentifier("1.2.840.113549.1.7.1"));
            DERSequence contentinfo = new DERSequence(ev);
            ASN1EncodableVector v = new ASN1EncodableVector();
            for (int c = 0; c < certs.length; c++) {
                ASN1InputStream tempstream = new ASN1InputStream(new ByteArrayInputStream(certs[c].getEncoded()));
                v.add(tempstream.readObject());
            }
            DERSet dercertificates = new DERSet(v);
            ASN1EncodableVector signerinfo = new ASN1EncodableVector();
            signerinfo.add(new DERInteger(1));
            v = new ASN1EncodableVector();
            v.add(CertUtil.getIssuer(certs[0]));
            v.add(new DERInteger(certs[0].getSerialNumber()));
            signerinfo.add(new DERSequence(v));
            v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier("2.16.840.1.101.3.4.2.1"));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            signerinfo.add(new DERTaggedObject(false, 0, new DERSet(signedAttributes)));
            v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier("1.2.840.113549.1.1.1"));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            signerinfo.add(new DEROctetString(signatureBytes));
            if (serverTimestamp != null && !"".equals(serverTimestamp.toString())) {
                byte[] timestampHash = MessageDigest.getInstance("2.16.840.1.101.3.4.2.1", "BC").digest(signatureBytes);
                ASN1EncodableVector unsignedAttributes = buildUnsignedAttributes(timestampHash, serverTimestamp, usernameTimestamp, passwordTimestamp);
                if (unsignedAttributes != null) {
                    signerinfo.add(new DERTaggedObject(false, 1, new DERSet(unsignedAttributes)));
                }
            }
            ASN1EncodableVector body = new ASN1EncodableVector();
            body.add(new DERInteger(1));
            body.add(digestAlgorithms);
            body.add(contentinfo);
            body.add(new DERTaggedObject(false, 0, dercertificates));
            body.add(new DERSet(new DERSequence(signerinfo)));
            ASN1EncodableVector whole = new ASN1EncodableVector();
            whole.add(new DERObjectIdentifier("1.2.840.113549.1.7.2"));
            whole.add(new DERTaggedObject(0, new DERSequence(body)));
            encodedPkcs7 = IOUtils.toByteArray(new DERSequence(whole));
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        PdfDictionary dic2 = new PdfDictionary();
        byte out[] = new byte[0x5000 / 2];
        System.arraycopy(encodedPkcs7, 0, out, 0, encodedPkcs7.length);
        dic2.put(PdfName.CONTENTS, new PdfString(out).setHexWriting(true));
        sap.close(dic2);
        logger.info("[createSignatureAppearance.retorna]:: ");
    }
