    public String crypto(String input) {
        if (input != null) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                StringBuffer buffer = new StringBuffer();
                md5.reset();
                byte[] senha = md5.digest(input.getBytes());
                for (int i = 0; i < senha.length; i++) {
                    buffer.append(senha[i++]);
                }
                return buffer.toString().replace("-", "");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
