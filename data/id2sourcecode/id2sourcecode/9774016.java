    private void runBooleanTest(Output write) throws IOException {
        for (int i = 0; i < 100; i++) {
            write.writeBoolean(true);
            write.writeBoolean(false);
        }
        Input read = new Input(write.toBytes());
        for (int i = 0; i < 100; i++) {
            assertEquals(true, read.readBoolean());
            assertEquals(false, read.readBoolean());
        }
    }
