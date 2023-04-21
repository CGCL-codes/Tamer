    private String encode(String username, String password) {
        try {
            String mergedCredentials = password + "{" + username + "}";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(mergedCredentials.getBytes());
            BigInteger number = new BigInteger(1, digest);
            String md5 = number.toString(16);
            while (md5.length() < 32) md5 = "0" + md5;
            return Base64.encodeToString((username + ":" + md5).getBytes(), Base64.DEFAULT).trim();
        } catch (Exception e) {
            Log.e("ServerConfigurator", "couldn't encode with MD5", e);
            return null;
        }
    }
