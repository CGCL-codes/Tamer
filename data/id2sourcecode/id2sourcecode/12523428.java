    @Override
    public String grandToken(String userName, String password) {
        try {
            String token = encode(MessageDigest.getInstance("MD5").digest(("userName" + "password" + Math.random()).getBytes()));
            tokens.put(token, (new Date()).getTime());
            tokensToUser.put(token, userName);
            logger.info("token Granted");
            System.out.println("token grandted");
            return token;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
