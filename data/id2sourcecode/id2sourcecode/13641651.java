    private void decode() {
        for (write = read = off; read + 3 < count; ) {
            while (ready < 4) {
                int ch = translate(buf[read++]);
                if (ch >= 0) {
                    four[ready++] = (byte) ch;
                }
            }
            if (four[2] == 65) {
                buf[write++] = first(four);
                break;
            } else if (four[3] == 65) {
                buf[write++] = first(four);
                buf[write++] = second(four);
                break;
            } else {
                buf[write++] = first(four);
                buf[write++] = second(four);
                buf[write++] = third(four);
            }
            ready = 0;
        }
        count = write;
    }
