        public boolean validate(String username, String password) {
            if (username != null && password != null && username.equals(auth_username)) {
                switch(auth_encoding) {
                    case PLAIN_TEXT:
                        return password.equals(auth_password);
                    case MD5:
                        try {
                            MessageDigest digest = MessageDigest.getInstance("MD5");
                            String hashedPassword = getHexString(digest.digest(password.getBytes()));
                            return hashedPassword.equals(auth_password.toUpperCase());
                        } catch (NoSuchAlgorithmException e) {
                            throw new UnsupportedOperationException("Unexpected lack of MD5 support.", e);
                        }
                }
            }
            return false;
        }
