    public String MD5FromFile(String filename) {
        MessageDigest digest;
        DigestInputStream is = null;
        try {
            try {
                digest = MessageDigest.getInstance("MD5");
                try {
                    is = new DigestInputStream(new FileInputStream(filename), digest);
                    while (is.available() > 0) {
                        is.read();
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new BigInteger(1, digest.digest()).toString(16);
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Some sort of error trying to md5sum a file");
        return null;
    }
