    public static byte[] digestLeaf(String digestAlgorithm, XMLEncodable[] toBeSigneds, byte[][] additionalToBeSigneds) throws ContentEncodingException, NoSuchAlgorithmException {
        if (null == toBeSigneds) {
            Log.info("Value to be signed must not be null.");
            throw new ContentEncodingException("Unexpected null content in digestLeaf!");
        }
        byte[][] encodedData = new byte[toBeSigneds.length + ((null != additionalToBeSigneds) ? additionalToBeSigneds.length : 0)][];
        for (int i = 0; i < toBeSigneds.length; ++i) {
            encodedData[i] = toBeSigneds[i].encode();
        }
        if (null != additionalToBeSigneds) {
            for (int i = 0, j = toBeSigneds.length; j < encodedData.length; ++i, ++j) {
                encodedData[j] = additionalToBeSigneds[i];
            }
        }
        return DigestHelper.digest(((null == digestAlgorithm) || (digestAlgorithm.length() == 0)) ? CCNDigestHelper.DEFAULT_DIGEST_ALGORITHM : digestAlgorithm, encodedData);
    }
