    private char shiftAndAppend(char[] buffer, char c) {
        char shifted = buffer[0];
        for (int i = 0; i + 1 < buffer.length; i++) {
            buffer[i] = buffer[i + 1];
        }
        buffer[buffer.length - 1] = c;
        return shifted;
    }
