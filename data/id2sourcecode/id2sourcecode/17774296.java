    public Cache getInstance(Repository repo, String id) throws CacheInitialisationException {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(id.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new CacheInitialisationException("cannot generate ID hash");
        }
        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else hashString.append(hex.substring(hex.length() - 2));
        }
        String filename = System.getProperty("user.home") + "/.dm4j/" + repo.getClass().getName() + "/" + hashString.toString() + "/";
        File f = new File(filename);
        try {
            f.mkdirs();
        } catch (SecurityException e) {
            throw new CacheInitialisationException("do not have required rights");
        }
        return new CacheImpl(filename, repo.getSourcesFileExtension());
    }
