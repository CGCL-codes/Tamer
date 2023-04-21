    public static String getResourceReferenceWithSHA256HashAsHexEncodedString(byte[] bytes, String extension) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(getExtraBytes(bytes.length, extension));
            byte[] hashSHA256 = messageDigest.digest(bytes);
            String resourceReference = ResourceFileSupport.ResourceFilePrefix + hexEncodedStringForByteArray(hashSHA256) + getExtraString(bytes.length, extension);
            return resourceReference;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
