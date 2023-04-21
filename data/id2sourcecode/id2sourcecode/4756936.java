    protected void mapRAM() {
        for (int i = 0; i < 8; i++) readMap[i] = writeMap[i] = baseRAM[i];
    }
