    private void write(byte[] buf, int readSize) throws IOException {
        for (int i = 0; i < readSize; ++i) {
            int b = buf[i] & 0xff;
            if (b == '\\') {
                output.write(b);
                output.write(b);
            } else if ((b < 32 && b != 9 && b != 10 && b != 13) || b >= 127) {
                output.write('\\');
                output.write('x');
                output.write(HEX[b / 16]);
                output.write(HEX[b % 16]);
            } else {
                output.write(b);
            }
        }
    }
