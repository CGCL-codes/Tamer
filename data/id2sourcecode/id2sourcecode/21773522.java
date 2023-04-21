    private String SHA(String data) {
        if (data == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return toHexString(md.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return data;
        }
    }
