    public BBCMemory() {
        super("BBC Memory", 0x10000, LAST_ROM + 1);
        for (int i = BASE_RAM; i < BASE_OS_ROM; i++) getMem(i, 0x4000);
        for (int i = 0; i < 3; i++) readMap[i] = writeMap[i] = baseAddr[BASE_RAM + i];
    }
