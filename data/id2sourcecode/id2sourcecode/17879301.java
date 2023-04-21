    public static void main(String[] args) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            while (str != null) {
                System.out.println("> Enter the password to digest (exit to quit) ");
                str = in.readLine();
                if ("exit".equals(str)) return; else {
                    System.out.println(Base64Helper.encode(md5.digest(str.getBytes())));
                }
            }
        } catch (Exception e) {
        }
    }
