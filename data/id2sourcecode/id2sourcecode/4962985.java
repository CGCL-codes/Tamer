    static byte[] hashName(Name name, int hashAlg, int iterations, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest;
        switch(hashAlg) {
            case Digest.SHA1:
                digest = MessageDigest.getInstance("sha-1");
                break;
            default:
                throw new NoSuchAlgorithmException("Unknown NSEC3 algorithm" + "identifier: " + hashAlg);
        }
        byte[] hash = null;
        for (int i = 0; i <= iterations; i++) {
            digest.reset();
            if (i == 0) digest.update(name.toWireCanonical()); else digest.update(hash);
            if (salt != null) digest.update(salt);
            hash = digest.digest();
        }
        return hash;
    }
