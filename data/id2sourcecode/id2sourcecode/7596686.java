    @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "IOException seems to be never thrown, therefore there is no such test.", method = "useProtocolVersion", args = { int.class })
    public void test_useProtocolVersionI() throws Exception {
        oos.useProtocolVersion(ObjectOutputStream.PROTOCOL_VERSION_1);
        ExternalTest t1 = new ExternalTest();
        t1.setValue("hello1");
        oos.writeObject(t1);
        oos.close();
        ois = new ObjectInputStream(new ByteArrayInputStream(bao.toByteArray()));
        ExternalTest t2 = (ExternalTest) ois.readObject();
        ois.close();
        assertTrue("Cannot read/write PROTOCAL_VERSION_1 Externalizable objects: " + t2.getValue(), t1.getValue().equals(t2.getValue()));
    }
