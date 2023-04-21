        public static byte[] contentPrefixToDistinguishingHash(ContentName name) {
            byte[] fullDigest;
            byte[] encoded;
            try {
                encoded = name.encode();
            } catch (ContentEncodingException e) {
                throw new RuntimeException(e);
            }
            fullDigest = CCNDigestHelper.digest(encoded);
            if (fullDigest.length > DISTINGUISHING_HASH_LENGTH) {
                byte[] returnedDigest = new byte[DISTINGUISHING_HASH_LENGTH];
                System.arraycopy(fullDigest, 0, returnedDigest, 0, DISTINGUISHING_HASH_LENGTH);
                return returnedDigest;
            } else if (fullDigest.length < DISTINGUISHING_HASH_LENGTH) {
                byte[] returnedDigest = new byte[DISTINGUISHING_HASH_LENGTH];
                System.arraycopy(fullDigest, 0, returnedDigest, 0, fullDigest.length);
                return returnedDigest;
            }
            return fullDigest;
        }
