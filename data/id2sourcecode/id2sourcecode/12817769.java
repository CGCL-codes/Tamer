    public static void main(String[] args) throws Exception {
        String password = "hm";
        String encoded = CriptoUtils.pwEncrypt(password);
        System.out.println(encoded);
        System.out.println(CriptoUtils.pwDecrypt(encoded).equals(password));
        char[] enc = encoded.toCharArray();
        enc[2] = (char) (enc[2] + 1);
        encoded = new String(enc);
        System.out.println(encoded);
        System.out.println(password.equals(CriptoUtils.pwDecrypt(encoded)));
        System.out.println(CriptoUtils.digest("admin"));
    }
