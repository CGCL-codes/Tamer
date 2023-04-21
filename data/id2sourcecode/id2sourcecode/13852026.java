    @Test
    public void testdeCipher() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("password:");
        String password = br.readLine();
        String ciphertext = "e85a2d4e15d0b90c273406e3f7c0188b8be89acc0172f170ce1cb0f86716c37a42cbd92d4d1e3c5fc956a92bb5084cf715f431460158b20da56d6af2d4b8ccdd";
        byte[] cipherbytes = hex2bytes(ciphertext);
        MessageDigest SHA_256 = MessageDigest.getInstance("SHA-256");
        byte[] key = SHA_256.digest(password.getBytes());
        MessageDigest MD5 = MessageDigest.getInstance("MD5");
        byte[] iv = MD5.digest(password.getBytes());
        System.out.println(Arrays.toString(cipherbytes));
        byte[] text = deCipher("AES/CBC/PKCS5Padding", key, "AES", iv, cipherbytes);
        System.out.println(new String(text));
    }
