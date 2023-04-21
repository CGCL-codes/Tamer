    public byte[] readScratchpad() throws OneWireIOException, OneWireException {
        byte[] result_block;
        if (adapter.select(address)) {
            byte[] send_block = new byte[10];
            send_block[0] = (byte) READ_SCRATCHPAD_COMMAND;
            for (int i = 1; i < 10; i++) send_block[i] = (byte) 0xFF;
            adapter.dataBlock(send_block, 0, send_block.length);
            result_block = new byte[9];
            for (int i = 0; i < 9; i++) {
                result_block[i] = send_block[i + 1];
            }
            if (CRC8.compute(send_block, 1, 9) == 0) return (result_block); else throw new OneWireIOException("OneWireContainer28-Error reading CRC8 from device.");
        }
        throw new OneWireIOException("OneWireContainer28-Device not found on 1-Wire Network");
    }
