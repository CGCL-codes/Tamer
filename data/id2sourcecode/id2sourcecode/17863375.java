    private static String getPasswordHash(String pass) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
            BigInteger sha;
            sha = new BigInteger(1, md.digest(pass.getBytes("UTF-8")));
            return sha.toString(16);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
