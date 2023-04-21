    public void testStrings() throws IOException {
        runStringTest(new Output(4096));
        runStringTest(new Output(897));
        runStringTest(new Output(new ByteArrayOutputStream()));
        Output write = new Output(21);
        String value2 = "abcdefáéíóúሴ";
        write.writeString(value2);
        Input read = new Input(write.toBytes());
        assertEquals(value2, read.readString());
    }
