    public Element serialize() throws SerializationException {
        Element keyInfo = new Element(WSConstants.DSIG_PREFIX + ":KeyInfo", WSConstants.DSIG_NAMESPACE);
        Element encryptedKey = new Element(WSConstants.ENC_PREFIX + ":EncryptedKey", WSConstants.ENC_NAMESPACE);
        Element encryptionMethod = new Element(WSConstants.ENC_PREFIX + ":EncryptionMethod", WSConstants.ENC_NAMESPACE);
        Attribute encMethAlg = new Attribute("Algorithm", "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
        encryptionMethod.addAttribute(encMethAlg);
        Element digestMethod = new Element(WSConstants.DSIG_PREFIX + ":DigestMethod", WSConstants.DSIG_NAMESPACE);
        Attribute digMethAlg = new Attribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
        digestMethod.addAttribute(digMethAlg);
        encryptionMethod.appendChild(digestMethod);
        encryptedKey.appendChild(encryptionMethod);
        Element subKeyInfo = new Element(WSConstants.DSIG_PREFIX + ":KeyInfo", WSConstants.DSIG_NAMESPACE);
        Element securityTokenReference = new Element(WSConstants.WSSE_PREFIX + ":SecurityTokenReference", WSConstants.WSSE_NAMESPACE_OASIS_10);
        Element keyIdentifier = new Element(WSConstants.WSSE_PREFIX + ":KeyIdentifier", WSConstants.WSSE_NAMESPACE_OASIS_10);
        Attribute valueType = new Attribute("ValueType", "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1");
        Attribute encodingType = new Attribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
        keyIdentifier.addAttribute(valueType);
        keyIdentifier.addAttribute(encodingType);
        String fingerPrint = "";
        try {
            fingerPrint = CryptoUtils.digest(cert.getEncoded(), "SHA");
        } catch (org.xmldap.exceptions.CryptoException e) {
            throw new SerializationException(e);
        } catch (CertificateEncodingException e) {
            throw new SerializationException(e);
        }
        keyIdentifier.appendChild(fingerPrint);
        securityTokenReference.appendChild(keyIdentifier);
        subKeyInfo.appendChild(securityTokenReference);
        encryptedKey.appendChild(subKeyInfo);
        Element cipherData = new Element(WSConstants.ENC_PREFIX + ":CipherData", WSConstants.ENC_NAMESPACE);
        Element cipherValue = new Element(WSConstants.ENC_PREFIX + ":CipherValue", WSConstants.ENC_NAMESPACE);
        try {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) cert.getPublicKey();
            String cipherText = CryptoUtils.rsaoaepEncrypt(secretKey, rsaPublicKey);
            cipherValue.appendChild(cipherText);
        } catch (org.xmldap.exceptions.CryptoException e) {
            throw new SerializationException(e);
        }
        cipherData.appendChild(cipherValue);
        encryptedKey.appendChild(cipherData);
        keyInfo.appendChild(encryptedKey);
        return keyInfo;
    }
