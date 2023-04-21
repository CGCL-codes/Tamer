    private static boolean checkMessageDigest(Node a_node, XMLSignature a_signature) throws XMLParseException {
        MessageDigest sha1;
        byte[] digest;
        try {
            if (a_signature.getDigestMethod() == null) {
                return true;
            } else {
                sha1 = MessageDigest.getInstance("SHA-1");
            }
        } catch (NoSuchAlgorithmException a_e) {
            return false;
        }
        byte[] buff = toCanonical(a_node, a_signature.getSignatureElement());
        digest = sha1.digest(buff);
        if (!MessageDigest.isEqual(Base64.decode(a_signature.getDigestValue()), digest)) {
            return false;
        }
        return true;
    }
