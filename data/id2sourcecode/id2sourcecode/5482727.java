    private static byte[] getshaID(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] bytes = md.digest(source.getBytes());
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
