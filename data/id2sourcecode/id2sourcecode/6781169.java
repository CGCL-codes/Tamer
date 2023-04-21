    public ConsoleLine[] getLines() {
        if (isEmpty()) return new ConsoleLine[0];
        ConsoleLine[] docLines = new ConsoleLine[readIndex > writeIndex ? BUFFER_SIZE : writeIndex];
        int index = readIndex;
        for (int i = 0; i < docLines.length; i++) {
            docLines[i] = new ConsoleLine(lines[index], lineTypes[index]);
            if (++index >= BUFFER_SIZE) {
                index = 0;
            }
        }
        return docLines;
    }
