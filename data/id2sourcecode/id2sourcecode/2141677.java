    private byte[] hash(byte[] body, String hashAlgorithm) throws SignatureException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(hashAlgorithm);
        } catch (Exception e) {
            throw new SignatureException(e);
        }
        int length = body.length;
        if (attributes.containsKey(LENGTH)) {
            length = Integer.parseInt(attributes.get(LENGTH));
        }
        byte[] bodyHash = null;
        digest.update(body, 0, length);
        bodyHash = digest.digest();
        return bodyHash;
    }
