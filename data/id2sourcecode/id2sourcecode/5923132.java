    private Color getColorFromByteArray(byte[] bytes) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("SHA1");
            algorithm.reset();
            algorithm.update(bytes);
            byte sha1[] = algorithm.digest();
            return (new Color(sha1[0] & 0xFF, sha1[1] & 0xFF, sha1[2] & 0xFF));
        } catch (NoSuchAlgorithmException e) {
            m_logger.error(e.getMessage(), e);
        }
        return (Color.BLACK);
    }
