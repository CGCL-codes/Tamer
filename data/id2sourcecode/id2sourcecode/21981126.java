    @Test
    public void testWriteKeyToFileAndCompare() throws Exception {
        File file = new File("test.new");
        if (file.exists()) file.delete();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        String identity = "Alice";
        char[] pass = { 'p', 'a', 's', 's' };
        PGPPublicKeyRing ring1 = KeyUtil.generateKeyPair(identity, pass);
        String output = KeyUtil.encodeBase64(ring1.getEncoded());
        out.write(output);
        out.close();
        BufferedReader input = new BufferedReader(new FileReader("test.new"));
        StringBuffer buff = new StringBuffer();
        String line;
        while ((line = input.readLine()) != null) {
            buff.append(line);
        }
        String result = buff.toString();
        byte[] bytes = KeyUtil.decodeBase64(result);
        PGPPublicKeyRing ring2 = new PGPPublicKeyRing(bytes);
        String output2 = KeyUtil.encodeBase64(ring2.getEncoded());
        assertTrue("Keys did not match", output.equals(output2));
    }
