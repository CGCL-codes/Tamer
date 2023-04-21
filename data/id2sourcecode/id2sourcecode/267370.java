    public static void main(String[] args) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        sha.update((byte) 1);
        sha.update((byte) 2);
        sha.update((byte) 3);
        byte[] hash = sha.digest();
        System.out.println("Length of digest: " + hash.length);
    }
