    public byte[] getBytesToSign(DERObjectIdentifier contentType, CMSProcessable content, String sigProvider) throws IOException, SignatureException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, CertificateEncodingException, CMSException {
        MessageDigest dig = MessageDigest.getInstance(this.getDigestAlgOID(), sigProvider);
        content.write(new DigOutputStream(dig));
        byte[] hash = dig.digest();
        AttributeTable attr = this.getSignedAttributes();
        if (attr != null) {
            ASN1EncodableVector v = new ASN1EncodableVector();
            if (attr.get(CMSAttributes.contentType) == null) {
                v.add(new Attribute(CMSAttributes.contentType, new DERSet(contentType)));
            } else {
                v.add(attr.get(CMSAttributes.contentType));
            }
            if (attr.get(CMSAttributes.signingTime) == null) {
                v.add(new Attribute(CMSAttributes.signingTime, new DERSet(new Time(new Date()))));
            } else {
                v.add(attr.get(CMSAttributes.signingTime));
            }
            v.add(new Attribute(CMSAttributes.messageDigest, new DERSet(new DEROctetString(hash))));
            v.add(buildSigningCertificateV2Attribute(sigProvider));
            Hashtable ats = attr.toHashtable();
            ats.remove(CMSAttributes.contentType);
            ats.remove(CMSAttributes.signingTime);
            ats.remove(CMSAttributes.messageDigest);
            ats.remove(PKCSObjectIdentifiers.id_aa_signingCertificateV2);
            Iterator it = ats.values().iterator();
            while (it.hasNext()) {
                v.add(Attribute.getInstance(it.next()));
            }
            signedAttr = new DERSet(v);
        } else {
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new Attribute(CMSAttributes.contentType, new DERSet(contentType)));
            v.add(new Attribute(CMSAttributes.signingTime, new DERSet(new DERUTCTime(new Date()))));
            v.add(new Attribute(CMSAttributes.messageDigest, new DERSet(new DEROctetString(hash))));
            v.add(buildSigningCertificateV2Attribute(sigProvider));
            signedAttr = new DERSet(v);
        }
        attr = this.getUnsignedAttributes();
        if (attr != null) {
            Hashtable ats = attr.toHashtable();
            Iterator it = ats.values().iterator();
            ASN1EncodableVector v = new ASN1EncodableVector();
            while (it.hasNext()) {
                v.add(Attribute.getInstance(it.next()));
            }
            unsignedAttr = new DERSet(v);
        }
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DEROutputStream dOut = new DEROutputStream(bOut);
        dOut.writeObject(signedAttr);
        return bOut.toByteArray();
    }
