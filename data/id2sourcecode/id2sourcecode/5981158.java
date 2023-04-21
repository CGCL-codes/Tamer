    public void testPasswordEncoding() throws Throwable {
        String atHashAlgorithm = "SHA-1";
        String newPassword = "joekel";
        byte[] hash = MessageDigest.getInstance(atHashAlgorithm).digest(newPassword.getBytes());
        Assert.assertEquals("zFaW6CdmxKJMxNPVannhwAywmWo=", Base64.encodeBytes(hash));
    }
