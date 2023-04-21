    public void appendConsoleLine(int type, String line) {
        if (lines == null) {
            lines = new String[BUFFER_SIZE];
            lineTypes = new int[BUFFER_SIZE];
        }
        lines[writeIndex] = line;
        lineTypes[writeIndex] = type;
        if (++writeIndex >= BUFFER_SIZE) {
            writeIndex = 0;
        }
        if (writeIndex == readIndex) {
            if (++readIndex >= BUFFER_SIZE) {
                readIndex = 0;
            }
        }
    }
