    private static String calculateRefID(String id) {
        try {
            BigInteger digest = new BigInteger(MessageDigest.getInstance("MD5").digest(id.getBytes("UTF-8")));
            return digest.toString(Character.MAX_RADIX);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Failed to get MD5 algorithm", e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Failed to decode user id", e);
            e.printStackTrace();
        }
        return null;
    }
