    public byte[] getEContent() {
        SignerInfo signerInfo = getSignerInfo(signedData);
        ASN1Set signedAttributesSet = signerInfo.getAuthenticatedAttributes();
        ContentInfo contentInfo = signedData.getEncapContentInfo();
        byte[] contentBytes = ((DEROctetString) contentInfo.getContent()).getOctets();
        if (signedAttributesSet.size() == 0) {
            return contentBytes;
        } else {
            byte[] attributesBytes = signedAttributesSet.getDEREncoded();
            String digAlg = signerInfo.getDigestAlgorithm().getObjectId().getId();
            try {
                Enumeration<?> attributes = signedAttributesSet.getObjects();
                byte[] storedDigestedContent = null;
                while (attributes.hasMoreElements()) {
                    Attribute attribute = new Attribute((DERSequence) attributes.nextElement());
                    DERObjectIdentifier attrType = attribute.getAttrType();
                    if (attrType.equals(RFC_3369_MESSAGE_DIGEST_OID)) {
                        ASN1Set attrValuesSet = attribute.getAttrValues();
                        if (attrValuesSet.size() != 1) {
                            System.err.println("WARNING: expected only one attribute value in signedAttribute message digest in eContent!");
                        }
                        storedDigestedContent = ((DEROctetString) attrValuesSet.getObjectAt(0)).getOctets();
                    }
                }
                if (storedDigestedContent == null) {
                    System.err.println("WARNING: error extracting signedAttribute message digest in eContent!");
                }
                MessageDigest dig = MessageDigest.getInstance(digAlg);
                byte[] computedDigestedContent = dig.digest(contentBytes);
                if (!Arrays.equals(storedDigestedContent, computedDigestedContent)) {
                    System.err.println("WARNING: error checking signedAttribute message digest in eContent!");
                }
            } catch (NoSuchAlgorithmException nsae) {
                System.err.println("WARNING: error checking signedAttribute in eContent! No such algorithm " + digAlg);
            }
            return attributesBytes;
        }
    }
