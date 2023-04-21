    public static String md5PasswordCrypt(String password) throws AutoDeployException {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(password.getBytes());
            StringBuffer hashString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashString.append('0');
                    hashString.append(hex.charAt(hex.length() - 1));
                } else {
                    hashString.append(hex.substring(hex.length() - 2));
                }
            }
            return hashString.toString();
        } catch (Exception e) {
            log.error("Can't crypt the password due to an unexpected error : " + e.getMessage());
            throw new AutoDeployException("Cant' crypt the password due to an unexpected error : " + e.getMessage());
        }
    }
