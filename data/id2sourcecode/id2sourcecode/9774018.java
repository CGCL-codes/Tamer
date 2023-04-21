    private void runCharTest(Output write) throws IOException {
        write.writeChar((char) 0);
        write.writeChar((char) 63);
        write.writeChar((char) 64);
        write.writeChar((char) 127);
        write.writeChar((char) 128);
        write.writeChar((char) 8192);
        write.writeChar((char) 16384);
        write.writeChar((char) 32767);
        write.writeChar((char) 65535);
        Input read = new Input(write.toBytes());
        assertEquals(0, read.readChar());
        assertEquals(63, read.readChar());
        assertEquals(64, read.readChar());
        assertEquals(127, read.readChar());
        assertEquals(128, read.readChar());
        assertEquals(8192, read.readChar());
        assertEquals(16384, read.readChar());
        assertEquals(32767, read.readChar());
        assertEquals(65535, read.readChar());
    }
