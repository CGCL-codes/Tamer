    public static String hashPassword(String username, String password, String hashMethod) {
        String encodedPassword;
        if (hashMethod.equals("base64")) {
            try {
                encodedPassword = Base64Encoder.encode(username + password);
            } catch (Exception e) {
                throw new OntopiaRuntimeException("Problem occurred when attempting to hash password", e);
            }
        } else if (hashMethod.equals("md5")) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] digest = messageDigest.digest((username + password).getBytes("ISO-8859-1"));
                encodedPassword = Base64Encoder.encode(new String(digest, "ISO-8859-1"));
            } catch (Exception e) {
                throw new OntopiaRuntimeException("Problems occurrend when attempting to hash password", e);
            }
        } else if (hashMethod.equals("plaintext")) {
            encodedPassword = password;
        } else {
            throw new OntopiaRuntimeException("Invalid password encoding: " + hashMethod);
        }
        return encodedPassword;
    }
