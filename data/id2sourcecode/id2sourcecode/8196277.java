    public static String makeHash(String senha) {
        MessageDigest md = null;
        byte[] senhaHash = null;
        StringBuilder sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
        }
        senhaHash = md.digest(senha.getBytes());
        for (int i = 0; i < senhaHash.length; i++) {
            int j = (int) (senhaHash[i] & 0x000000ff);
            if (j > 0xf) {
                sb.append(Integer.toHexString(j));
            } else {
                sb.append('0');
                sb.append(Integer.toHexString(j));
            }
        }
        return sb.toString();
    }
