    public static void main(String[] args) {
        String md5val[] = { "AAB", "BBC", "DD" };
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("Cannot find digest algorithm");
            System.exit(1);
        }
        for (String arg : md5val) {
            byte[] defaultBytes = arg.getBytes();
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("c");
                }
                hexString.append(hex);
            }
            String md5char = hexString.toString();
            System.out.println("MD5 (" + arg + ") = " + md5char);
        }
    }
