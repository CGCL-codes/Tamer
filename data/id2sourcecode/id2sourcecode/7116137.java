    private void compareBuffers(byte[] buffer1, byte[] buffer2, int length) throws Exception {
        for (int i = 0; i < length; i++) {
            if (buffer1[i] != buffer2[i]) {
                throw new Exception("readBytes did not read back what writeBytes wrote");
            }
        }
    }
