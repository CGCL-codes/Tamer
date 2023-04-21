    public String getHash(String message, Algorithms algorithm) {
        try {
            byte[] buffer = message.getBytes();
            MessageDigest md = MessageDigest.getInstance(algorithmNames.get(algorithm));
            md.update(buffer);
            byte[] digest = md.digest();
            String hash = formatHash(digest);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
