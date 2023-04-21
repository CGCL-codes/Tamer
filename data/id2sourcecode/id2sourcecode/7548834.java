    public static String getPasswordHash(String password, String salt) throws IOException, NoSuchAlgorithmException {
        if (password == null) {
            throw new NullPointerException("Cannot hash null password.");
        }
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        String text = (salt == null) ? password : password + salt;
        ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(ba_out, Charset.forName("UTF-8"));
        writer.write(text);
        writer.close();
        byte[] bytes = ba_out.toByteArray();
        byte[] hash = new byte[40];
        md.update(bytes, 0, bytes.length);
        hash = md.digest();
        return toHexString(hash);
    }
