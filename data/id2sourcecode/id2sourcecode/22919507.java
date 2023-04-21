    public static String createIdentifier() {
        String miliSecs = String.valueOf(Calendar.getInstance().getTimeInMillis());
        MessageDigest cripter = null;
        try {
            cripter = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.getLocalizedMessage();
            return null;
        }
        byte[] a = cripter.digest(miliSecs.getBytes());
        byte[] b = new Hex().encode(a);
        return new String(b).substring(0, 15);
    }
