    public void testSHA256() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] b = md.digest("zhengrenqi".getBytes());
        System.out.println(b.length);
        System.out.println(Arrays.toString(b));
        System.out.println(asHex(b));
    }
