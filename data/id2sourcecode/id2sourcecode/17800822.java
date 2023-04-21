    public byte[] getEncodedPKCS7(byte secondDigest[], Calendar signingTime) {
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
            DERConstructedSet digestAlgorithms = new DERConstructedSet();
            for (Iterator it = digestalgos.iterator(); it.hasNext(); ) {
                ASN1EncodableVector algos = new ASN1EncodableVector();
                algos.add(new DERObjectIdentifier((String) it.next()));
                algos.add(new DERNull());
                digestAlgorithms.addObject(new DERSequence(algos));
            }
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier(ID_PKCS7_DATA));
            if (RSAdata != null) v.add(new DERTaggedObject(0, new DEROctetString(RSAdata)));
            DERSequence contentinfo = new DERSequence(v);
            v = new ASN1EncodableVector();
            for (Iterator i = certs.iterator(); i.hasNext(); ) {
                ASN1InputStream tempstream = new ASN1InputStream(new ByteArrayInputStream(((X509Certificate) i.next()).getEncoded()));
                v.add(tempstream.readObject());
            }
            DERSet dercertificates = new DERSet(v);
            ASN1EncodableVector signerinfo = new ASN1EncodableVector();
            signerinfo.add(new DERInteger(signerversion));
            v = new ASN1EncodableVector();
            v.add(getIssuer(signCert.getTBSCertificate()));
            v.add(new DERInteger(signCert.getSerialNumber()));
            signerinfo.add(new DERSequence(v));
            v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier(digestAlgorithm));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            if (secondDigest != null && signingTime != null) {
                ASN1EncodableVector attribute = new ASN1EncodableVector();
                v = new ASN1EncodableVector();
                v.add(new DERObjectIdentifier(ID_CONTENT_TYPE));
                v.add(new DERSet(new DERObjectIdentifier(ID_PKCS7_DATA)));
                attribute.add(new DERSequence(v));
                v = new ASN1EncodableVector();
                v.add(new DERObjectIdentifier(ID_SIGNING_TIME));
                v.add(new DERSet(new DERUTCTime(signingTime.getTime())));
                attribute.add(new DERSequence(v));
                v = new ASN1EncodableVector();
                v.add(new DERObjectIdentifier(ID_MESSAGE_DIGEST));
                v.add(new DERSet(new DEROctetString(secondDigest)));
                attribute.add(new DERSequence(v));
                signerinfo.add(new DERTaggedObject(false, 0, new DERSet(attribute)));
            }
            v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier(digestEncryptionAlgorithm));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            signerinfo.add(new DEROctetString(digest));
            ASN1EncodableVector body = new ASN1EncodableVector();
            body.add(new DERInteger(version));
            body.add(digestAlgorithms);
            body.add(contentinfo);
            body.add(new DERTaggedObject(false, 0, dercertificates));
            if (crls.size() > 0) {
                v = new ASN1EncodableVector();
                for (Iterator i = crls.iterator(); i.hasNext(); ) {
                    ASN1InputStream t = new ASN1InputStream(new ByteArrayInputStream((((X509CRL) i.next()).getEncoded())));
                    v.add(t.readObject());
                }
                DERSet dercrls = new DERSet(v);
                body.add(new DERTaggedObject(false, 1, dercrls));
            }
            body.add(new DERSet(new DERSequence(signerinfo)));
            ASN1EncodableVector whole = new ASN1EncodableVector();
            whole.add(new DERObjectIdentifier(ID_PKCS7_SIGNED_DATA));
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
