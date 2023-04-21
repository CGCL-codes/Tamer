    public void runStringTest(Output write) throws IOException {
        String value1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\rabcdefghijklmnopqrstuvwxyz\n1234567890\t\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*";
        String value2 = "abcdefáéíóúሴ";
        write.writeString("");
        write.writeString("1");
        write.writeString("22");
        write.writeString("uno");
        write.writeString("dos");
        write.writeString("tres");
        write.writeString(null);
        write.writeString(value1);
        write.writeString(value2);
        for (int i = 0; i < 127; i++) write.writeString(String.valueOf((char) i));
        for (int i = 0; i < 127; i++) write.writeString(String.valueOf((char) i) + "abc");
        Input read = new Input(write.toBytes());
        assertEquals("", read.readString());
        assertEquals("1", read.readString());
        assertEquals("22", read.readString());
        assertEquals("uno", read.readString());
        assertEquals("dos", read.readString());
        assertEquals("tres", read.readString());
        assertEquals(null, read.readString());
        assertEquals(value1, read.readString());
        assertEquals(value2, read.readString());
        for (int i = 0; i < 127; i++) assertEquals(String.valueOf((char) i), read.readString());
        for (int i = 0; i < 127; i++) assertEquals(String.valueOf((char) i) + "abc", read.readString());
    }
