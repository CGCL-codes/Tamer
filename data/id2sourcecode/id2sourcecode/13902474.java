    public void consume() throws TokenException {
        for (int i = 0; i < (depth - 1); i++) {
            buffer[i] = buffer[i + 1];
        }
        buffer[depth - 1] = input.read();
    }
