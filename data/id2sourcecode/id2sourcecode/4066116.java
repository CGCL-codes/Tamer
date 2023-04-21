    public void testReadString() throws ParseException {
        DataWriter writer = new DataWriter();
        writer.writeString("Hassu");
        writer.writeString("Kala");
        DataReader reader = new DataReader(writer.getData());
        assertEquals("Hassu", reader.readString());
        assertEquals("Kala", reader.readString());
    }
