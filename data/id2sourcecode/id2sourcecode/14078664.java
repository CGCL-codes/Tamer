    public byte[] getEncodedPKCS7(byte secondDigest[], Calendar signingTime, TSAClient tsaClient, byte[] ocsp) {
        try {
            if (externalDigest != null) {
                digest = externalDigest;
                if (RSAdata != null) RSAdata = externalRSAdata;
            } else if (externalRSAdata != null && RSAdata != null) {
                RSAdata = externalRSAdata;
                sig.update(RSAdata);
                digest = sig.sign();
            } else {
                if (RSAdata != null) {
                    RSAdata = messageDigest.digest();
                    sig.update(RSAdata);
                }
                digest = sig.sign();
            }
            ASN1EncodableVector digestAlgorithms = new ASN1EncodableVector();
            for (Object element : digestalgos) {
                ASN1EncodableVector algos = new ASN1EncodableVector();
                algos.add(new ASN1ObjectIdentifier((String) element));
                algos.add(DERNull.INSTANCE);
                digestAlgorithms.add(new DERSequence(algos));
            }
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(ID_PKCS7_DATA));
            if (RSAdata != null) v.add(new DERTaggedObject(0, new DEROctetString(RSAdata)));
            DERSequence contentinfo = new DERSequence(v);
            v = new ASN1EncodableVector();
            for (Object element : certs) {
                ASN1InputStream tempstream = new ASN1InputStream(new ByteArrayInputStream(((X509Certificate) element).getEncoded()));
                v.add(tempstream.readObject());
            }
            DERSet dercertificates = new DERSet(v);
            ASN1EncodableVector signerinfo = new ASN1EncodableVector();
            signerinfo.add(new ASN1Integer(signerversion));
            v = new ASN1EncodableVector();
            v.add(getIssuer(signCert.getTBSCertificate()));
            v.add(new ASN1Integer(signCert.getSerialNumber()));
            signerinfo.add(new DERSequence(v));
            v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(digestAlgorithm));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            if (secondDigest != null && signingTime != null) {
                signerinfo.add(new DERTaggedObject(false, 0, getAuthenticatedAttributeSet(secondDigest, signingTime, ocsp)));
            }
            v = new ASN1EncodableVector();
            v.add(new ASN1ObjectIdentifier(digestEncryptionAlgorithm));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            signerinfo.add(new DEROctetString(digest));
            if (tsaClient != null) {
                byte[] tsImprint = MessageDigest.getInstance(tsaClient.getDigestAlgorithm()).digest(digest);
                byte[] tsToken = tsaClient.getTimeStampToken(tsImprint);
                if (tsToken != null) {
                    ASN1EncodableVector unauthAttributes = buildUnauthenticatedAttributes(tsToken);
                    if (unauthAttributes != null) {
                        signerinfo.add(new DERTaggedObject(false, 1, new DERSet(unauthAttributes)));
                    }
                }
            }
            ASN1EncodableVector body = new ASN1EncodableVector();
            body.add(new ASN1Integer(version));
            body.add(new DERSet(digestAlgorithms));
            body.add(contentinfo);
            body.add(new DERTaggedObject(false, 0, dercertificates));
            body.add(new DERSet(new DERSequence(signerinfo)));
            ASN1EncodableVector whole = new ASN1EncodableVector();
            whole.add(new ASN1ObjectIdentifier(ID_PKCS7_SIGNED_DATA));
            whole.add(new DERTaggedObject(0, new DERSequence(body)));
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ASN1OutputStream dout = new ASN1OutputStream(bOut);
            dout.writeObject(new DERSequence(whole));
            dout.close();
            return bOut.toByteArray();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
