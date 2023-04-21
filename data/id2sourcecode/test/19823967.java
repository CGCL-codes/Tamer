        SignerInfo toSignerInfo(DERObjectIdentifier contentType, CMSProcessable content, SecureRandom random, Provider sigProvider, boolean addDefaultAttributes, boolean isCounterSignature) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException, CertificateEncodingException, CMSException {
            AlgorithmIdentifier digAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(this.getDigestAlgOID()), new DERNull());
            AlgorithmIdentifier encAlgId = getEncAlgorithmIdentifier(this.getEncryptionAlgOID());
            String digestName = CMSSignedHelper.INSTANCE.getDigestAlgName(digestOID);
            String signatureName = digestName + "with" + CMSSignedHelper.INSTANCE.getEncryptionAlgName(encOID);
            Signature sig = CMSSignedHelper.INSTANCE.getSignatureInstance(signatureName, sigProvider);
            MessageDigest dig = CMSSignedHelper.INSTANCE.getDigestInstance(digestName, sigProvider);
            byte[] hash = null;
            if (content != null) {
                content.write(new DigOutputStream(dig));
                hash = dig.digest();
                _digests.put(digestOID, hash.clone());
            }
            AttributeTable signed;
            if (addDefaultAttributes) {
                Map parameters = getBaseParameters(contentType, digAlgId, hash);
                signed = (sAttr != null) ? sAttr.getAttributes(Collections.unmodifiableMap(parameters)) : null;
            } else {
                signed = baseSignedTable;
            }
            if (isCounterSignature) {
                Hashtable ats = signed.toHashtable();
                ats.remove(CMSAttributes.contentType);
                signed = new AttributeTable(ats);
            }
            ASN1Set signedAttr = getAttributeSet(signed);
            byte[] tmp;
            if (signedAttr != null) {
                tmp = signedAttr.getEncoded(ASN1Encodable.DER);
            } else {
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                content.write(bOut);
                tmp = bOut.toByteArray();
            }
            sig.initSign(key, random);
            sig.update(tmp);
            ASN1OctetString encDigest = new DEROctetString(sig.sign());
            Map parameters = getBaseParameters(contentType, digAlgId, hash);
            parameters.put(CMSAttributeTableGenerator.SIGNATURE, encDigest.getOctets().clone());
            AttributeTable unsigned = (unsAttr != null) ? unsAttr.getAttributes(Collections.unmodifiableMap(parameters)) : null;
            ASN1Set unsignedAttr = getAttributeSet(unsigned);
            X509Certificate cert = this.getCertificate();
            SignerIdentifier identifier;
            if (cert != null) {
                TBSCertificateStructure tbs = TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(cert.getTBSCertificate()));
                IssuerAndSerialNumber encSid = new IssuerAndSerialNumber(tbs.getIssuer(), tbs.getSerialNumber().getValue());
                identifier = new SignerIdentifier(encSid);
            } else {
                identifier = new SignerIdentifier(new DEROctetString(keyIdentifier));
            }
            return new SignerInfo(identifier, digAlgId, signedAttr, encAlgId, encDigest, unsignedAttr);
        }
