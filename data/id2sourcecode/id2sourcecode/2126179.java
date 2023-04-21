    private void testSerializationSHA_DATA_2(MessageDigest sha) {
        try {
            sha.reset();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(out);
            output.writeUTF("tests.api.java.security.MessageDigestTest$InitializerFieldsTest3");
            output.writeInt(0);
            output.writeUTF("java.io.Serializable");
            output.writeUTF("sub_toBeNotSerialized");
            output.writeInt(9);
            output.writeUTF("Ljava/lang/String;");
            output.writeUTF("sub_toBeNotSerialized2");
            output.writeInt(9);
            output.writeUTF("Ljava/lang/String;");
            output.writeUTF("sub_toBeSerialized");
            output.writeInt(1);
            output.writeUTF("Ljava/lang/String;");
            output.writeUTF("sub_toBeSerialized3");
            output.writeInt(1);
            output.writeUTF("Ljava/lang/String;");
            output.writeUTF("sub_toBeSerialized4");
            output.writeInt(1);
            output.writeUTF("Ljava/lang/String;");
            output.writeUTF("sub_toBeSerialized5");
            output.writeInt(1);
            output.writeUTF("Ljava/lang/String;");
            output.writeUTF("<clinit>");
            output.writeInt(8);
            output.writeUTF("()V");
            output.writeUTF("<init>");
            output.writeInt(0);
            output.writeUTF("()V");
            output.writeUTF("equals");
            output.writeInt(1);
            output.writeUTF("(Ljava.lang.Object;)Z");
            output.flush();
            byte[] data = out.toByteArray();
            byte[] hash = sha.digest(data);
            assertTrue("SHA_DATA_2 NOT ok", Arrays.equals(hash, SHA_DATA_2));
        } catch (IOException e) {
            fail("SHA_DATA_2 NOT ok");
        }
    }
