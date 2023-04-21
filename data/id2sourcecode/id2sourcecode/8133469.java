    private String generateDigest(String password) throws ServiceException {
        byte[] digest = null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            digest = sha.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e.toString());
        }
        return hexEncode(digest);
    }
