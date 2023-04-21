    public byte[] getEncodedPKCS7(byte secondDigest[], Calendar signingTime, TSAClient tsaClient) throws SinaduraCoreException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        try {
            if (this.externalDigest != null) {
                this.digest = this.externalDigest;
                if (this.RSAdata != null) this.RSAdata = this.externalRSAdata;
            } else if (this.externalRSAdata != null && this.RSAdata != null) {
                this.RSAdata = this.externalRSAdata;
                this.sig.update(this.RSAdata);
                this.digest = this.sig.sign();
            } else {
                if (this.RSAdata != null) {
                    this.RSAdata = this.messageDigest.digest();
                    this.sig.update(this.RSAdata);
                }
                this.digest = this.sig.sign();
            }
            DERConstructedSet digestAlgorithms = new DERConstructedSet();
            for (Iterator it = this.digestalgos.iterator(); it.hasNext(); ) {
                ASN1EncodableVector algos = new ASN1EncodableVector();
                algos.add(new DERObjectIdentifier((String) it.next()));
                algos.add(new DERNull());
                digestAlgorithms.addObject(new DERSequence(algos));
            }
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier(ID_PKCS7_DATA));
            if (this.RSAdata != null) v.add(new DERTaggedObject(0, new DEROctetString(this.RSAdata)));
            DERSequence contentinfo = new DERSequence(v);
            v = new ASN1EncodableVector();
            for (Iterator i = this.certs.iterator(); i.hasNext(); ) {
                ASN1InputStream tempstream = new ASN1InputStream(new ByteArrayInputStream(((X509Certificate) i.next()).getEncoded()));
                v.add(tempstream.readObject());
            }
            DERSet dercertificates = new DERSet(v);
            ASN1EncodableVector signerinfo = new ASN1EncodableVector();
            signerinfo.add(new DERInteger(this.signerversion));
            v = new ASN1EncodableVector();
            v.add(getIssuer(this.signCert.getTBSCertificate()));
            v.add(new DERInteger(this.signCert.getSerialNumber()));
            signerinfo.add(new DERSequence(v));
            v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier(this.digestAlgorithm));
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
                if (!this.crls.isEmpty()) {
                    v = new ASN1EncodableVector();
                    v.add(new DERObjectIdentifier(ID_ADBE_REVOCATION));
                    ASN1EncodableVector v2 = new ASN1EncodableVector();
                    for (Iterator i = this.crls.iterator(); i.hasNext(); ) {
                        ASN1InputStream t = new ASN1InputStream(new ByteArrayInputStream((((X509CRL) i.next()).getEncoded())));
                        v2.add(t.readObject());
                    }
                    v.add(new DERSet(new DERSequence(new DERTaggedObject(true, 0, new DERSequence(v2)))));
                    attribute.add(new DERSequence(v));
                }
                signerinfo.add(new DERTaggedObject(false, 0, new DERSet(attribute)));
            }
            v = new ASN1EncodableVector();
            v.add(new DERObjectIdentifier(this.digestEncryptionAlgorithm));
            v.add(new DERNull());
            signerinfo.add(new DERSequence(v));
            signerinfo.add(new DEROctetString(this.digest));
            if (tsaClient != null) {
                byte[] tsImprint = MessageDigest.getInstance("SHA-1").digest(this.digest);
                byte[] tsToken = tsaClient.getTimeStampToken(this, tsImprint);
                if (tsToken != null) {
                    ASN1EncodableVector unauthAttributes = buildUnauthenticatedAttributes(tsToken);
                    if (unauthAttributes != null) {
                        signerinfo.add(new DERTaggedObject(false, 1, new DERSet(unauthAttributes)));
                    }
                }
            }
            ASN1EncodableVector body = new ASN1EncodableVector();
            body.add(new DERInteger(this.version));
            body.add(digestAlgorithms);
            body.add(contentinfo);
            body.add(new DERTaggedObject(false, 0, dercertificates));
            if (!this.crls.isEmpty()) {
                v = new ASN1EncodableVector();
                for (Iterator i = this.crls.iterator(); i.hasNext(); ) {
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
            ASN1OutputStream dout = new ASN1OutputStream(bOut);
            dout.writeObject(new DERSequence(whole));
            dout.close();
        } catch (CertificateEncodingException e) {
            throw new SinaduraCoreException(e.getMessage(), e);
        } catch (SignatureException e) {
            throw new SinaduraCoreException(e.getMessage(), e);
        } catch (CRLException e) {
            throw new SinaduraCoreException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new SinaduraCoreException(e.getMessage(), e);
        } catch (IOException e) {
            throw new SinaduraCoreException(e.getMessage(), e);
        } catch (ProviderException e) {
            throw new SinaduraCoreException(e.getMessage(), e);
        }
        return bOut.toByteArray();
    }
