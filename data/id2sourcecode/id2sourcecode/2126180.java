    private void testSerializationSHA_DATA_1(MessageDigest sha) {
        try {
            sha.reset();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(out);
            output.writeUTF("tests.api.java.security.MessageDigestTest$OptionalDataNotRead");
            output.writeInt(0);
            output.writeUTF("java.io.Serializable");
            output.writeUTF("class$0");
            output.writeInt(8);
            output.writeUTF("Ljava/lang/Class;");
            output.writeUTF("field1");
            output.writeInt(2);
            output.writeUTF("I");
            output.writeUTF("field2");
            output.writeInt(2);
            output.writeUTF("I");
            output.writeUTF("<clinit>");
            output.writeInt(8);
            output.writeUTF("()V");
            output.writeUTF("<init>");
            output.writeInt(1);
            output.writeUTF("()V");
            output.flush();
            byte[] data = out.toByteArray();
            byte[] hash = sha.digest(data);
            assertTrue("SHA_DATA_1 NOT ok", Arrays.equals(hash, SHA_DATA_1));
        } catch (IOException e) {
            fail("SHA_DATA_1 NOT ok");
        }
    }
