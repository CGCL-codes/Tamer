        SignerInfo toSignerInfo(DERObjectIdentifier contentType) throws IOException, SignatureException, CertificateEncodingException {
            AlgorithmIdentifier digAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(this.getDigestAlgOID()), new DERNull());
            AlgorithmIdentifier encAlgId = getEncAlgorithmIdentifier(this.getEncryptionAlgOID());
            byte[] hash = _digest.digest();
            _digests.put(_digestOID, hash.clone());
            Map parameters = getBaseParameters(contentType, digAlgId, hash);
            AttributeTable signed = (_sAttr != null) ? _sAttr.getAttributes(Collections.unmodifiableMap(parameters)) : null;
            ASN1Set signedAttr = getAttributeSet(signed);
            byte[] tmp;
            if (signedAttr != null) {
                tmp = signedAttr.getEncoded(ASN1Encodable.DER);
            } else {
                throw new RuntimeException("signatures without signed attributes not implemented.");
            }
            _signature.update(tmp);
            ASN1OctetString encDigest = new DEROctetString(_signature.sign());
            parameters = getBaseParameters(contentType, digAlgId, hash);
            parameters.put(CMSAttributeTableGenerator.SIGNATURE, encDigest.getOctets().clone());
            AttributeTable unsigned = (_unsAttr != null) ? _unsAttr.getAttributes(Collections.unmodifiableMap(parameters)) : null;
            ASN1Set unsignedAttr = getAttributeSet(unsigned);
            X509Certificate cert = this.getCertificate();
            SignerIdentifier signerIdentifier;
            if (cert != null) {
                TBSCertificateStructure tbs = TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(cert.getTBSCertificate()));
                IssuerAndSerialNumber encSid = new IssuerAndSerialNumber(tbs.getIssuer(), tbs.getSerialNumber().getValue());
                signerIdentifier = new SignerIdentifier(encSid);
            } else {
                signerIdentifier = new SignerIdentifier(new DEROctetString(_subjectKeyID));
            }
            return new SignerInfo(signerIdentifier, digAlgId, signedAttr, encAlgId, encDigest, unsignedAttr);
        }
