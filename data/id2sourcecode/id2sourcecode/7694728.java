    public static String sha1(String message) {
        try {
            byte[] buffer = message.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(buffer);
            byte[] digest = md.digest();
            char[] hash = new char[40];
            for (int i = 0, n = 0; i < digest.length; i++) {
                byte aux = digest[i];
                int b = aux & 0xff;
                String hex = Integer.toHexString(b);
                if (hex.length() == 1) {
                    hash[n++] = '0';
                    hash[n++] = hex.charAt(0);
                } else {
                    hash[n++] = hex.charAt(0);
                    hash[n++] = hex.charAt(1);
                }
            }
            return new String(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
