    public String getNonce(String seed) throws InternalServerErrorException {
        if (nonceDigestSecret == null) {
            nonceDigestSecret = generateOpaque();
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException("failed to get instance of MD5 digest, used in " + RFC2617AuthQopDigest.class.getName());
        }
        return AsciiHexStringEncoder.encode(messageDigest.digest(EncodingUtil.getAsciiBytes(seed + ":" + nonceDigestSecret)));
    }
