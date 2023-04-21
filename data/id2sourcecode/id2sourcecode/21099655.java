    @Test
    public void testMd5() throws Exception {
        System.out.println("md5");
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        byte[] expResult = digest.digest(message.getBytes());
        instance.setPayload(message.getBytes());
        byte[] result = instance.md5();
        assertTrue(Arrays.equals(result, expResult));
    }
