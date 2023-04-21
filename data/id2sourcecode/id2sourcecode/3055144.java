    public void test_putFields() throws Exception {
        SerializableTestHelper sth;
        oos.writeObject(new SerializableTestHelper("Gabba", "Jabba"));
        oos.flush();
        ois = new ObjectInputStream(new ByteArrayInputStream(bao.toByteArray()));
        sth = (SerializableTestHelper) (ois.readObject());
        assertEquals("readFields / writeFields failed--first field not set", "Gabba", sth.getText1());
        assertNull("readFields / writeFields failed--second field should not have been set", sth.getText2());
    }
