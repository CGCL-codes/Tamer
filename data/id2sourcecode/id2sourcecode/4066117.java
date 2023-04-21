    public void testCheckString() throws ParseException {
        DataWriter writer = new DataWriter();
        writer.writeString("Hassu");
        writer.writeString("Kala");
        DataReader reader = new DataReader(writer.getData());
        reader.checkString("Hassu");
        reader.checkString("Kala");
    }
