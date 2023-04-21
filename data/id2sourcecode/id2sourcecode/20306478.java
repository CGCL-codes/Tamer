    public String getHash(InputStream inputStream, Algorithms algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithmNames.get(algorithm));
            DigestInputStream dis = new DigestInputStream(inputStream, md);
            try {
                while (dis.available() > 0) dis.read();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return formatHash(dis.getMessageDigest().digest());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }
