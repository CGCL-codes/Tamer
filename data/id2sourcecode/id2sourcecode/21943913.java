    private String md5(String raw) throws RegistrationException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            byte[] byteHash = md.digest(raw.getBytes());
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < byteHash.length; i++) {
                result.append(Integer.toHexString(0xFF & byteHash[i]));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while MD5 encoding: ", e);
            throw new RegistrationException("Error while MD5 encoding code ");
        }
    }
