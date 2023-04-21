    @Override
    protected void write() {
        super.write();
        write32(language);
        write32(buttonSwap);
        write32(graphicsThread);
        write32(accessThread);
        write32(fontThread);
        write32(soundThread);
        write32(result);
        writeUnknown(16);
    }
