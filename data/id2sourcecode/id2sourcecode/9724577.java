    public static String getHash(String password) {
        if (password != null) {
            try {
                MessageDigest md4 = MessageDigest.getInstance("MD4", new CryptixCrypto());
                byte[] pwdBytes = password.getBytes("UTF-8");
                md4.update(pwdBytes);
                String hash = new String(Base64.encodeBase64(md4.digest()));
                return hash;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
