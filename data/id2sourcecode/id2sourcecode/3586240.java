    public void setKeyIdentifierThumb(X509Certificate cert) throws WSSecurityException {
        Document doc = this.element.getOwnerDocument();
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            throw new WSSecurityException(0, "noSHA1availabe");
        }
        sha.reset();
        try {
            sha.update(cert.getEncoded());
        } catch (CertificateEncodingException e1) {
            throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE, "encodeError");
        }
        byte[] data = sha.digest();
        org.w3c.dom.Text text = doc.createTextNode(Base64.encode(data));
        createKeyIdentifier(doc, THUMB_URI, text);
    }
