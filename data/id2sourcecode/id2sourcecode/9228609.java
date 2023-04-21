    static String sha(String source) {
        if (source == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] bytes = md.digest(source.getBytes());
            String s = new String(bytes);
            return s;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }
