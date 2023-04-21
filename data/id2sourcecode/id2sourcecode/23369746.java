    private static ASN1Set createAuthenticatedAttributes(String digestAlgorithm, byte[] contentBytes) throws NoSuchAlgorithmException {
        MessageDigest dig = MessageDigest.getInstance(digestAlgorithm);
        byte[] digestedContentBytes = dig.digest(contentBytes);
        ASN1OctetString digestedContent = new DEROctetString(digestedContentBytes);
        Attribute contentTypeAttribute = new Attribute(RFC_3369_CONTENT_TYPE_OID, createSingletonSet(ICAO_SOD_OID));
        Attribute messageDigestAttribute = new Attribute(RFC_3369_MESSAGE_DIGEST_OID, createSingletonSet(digestedContent));
        ASN1Encodable[] result = { contentTypeAttribute.toASN1Object(), messageDigestAttribute.toASN1Object() };
        return new DERSet(result);
    }
