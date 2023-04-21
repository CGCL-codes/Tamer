    public static byte[] hash(byte[] toBeHashed, byte hashAlgorithm) throws NoSuchAlgorithmException {
        String hashAlgorithmName = null;
        switch(hashAlgorithm) {
            case NONE_HASH_ALGORITHM:
                break;
            case SHA1_HASH_ALGORITHM:
                hashAlgorithmName = "SHA1";
                break;
            case SHA1_256_HASH_ALGORITHM:
                hashAlgorithmName = "SHA-256";
                break;
            case SHA1_512_HASH_ALGORITHM:
                hashAlgorithmName = "SHA-512";
                break;
            case MD4_HASH_ALGORITHM:
                hashAlgorithmName = "MD4";
                break;
            case MD5_HASH_ALGORITHM:
                hashAlgorithmName = "MD5";
                break;
        }
        MessageDigest digest = MessageDigest.getInstance(hashAlgorithmName);
        return digest.digest(toBeHashed);
    }
