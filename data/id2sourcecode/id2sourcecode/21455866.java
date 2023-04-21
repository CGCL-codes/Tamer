    private String MD5Hash(String Input) {
        String pass = Input;
        StringBuffer hexString = new StringBuffer();
        byte[] defaultBytes = pass.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            pass = hexString + "";
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("Coulnd't encrypt password... ");
        }
        return hexString.toString();
    }
