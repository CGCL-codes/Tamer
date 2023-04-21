    public String getDigest(String query) {
        byte[] queryAsBytes;
        try {
            queryAsBytes = query.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Creating a byte array from the query string caused a {}: {}", e.getClass().getName(), e.getMessage());
            return "";
        }
        mdFct.reset();
        mdFct.update(queryAsBytes);
        byte[] digestAsBytes = mdFct.digest();
        String digestAsString = "";
        for (int i = 0; i < digestAsBytes.length; ++i) {
            digestAsString += String.valueOf(digestAsBytes[i]);
        }
        return digestAsString;
    }
