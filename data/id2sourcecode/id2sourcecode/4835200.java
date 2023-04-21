    private void programPage() {
        if (writing) logw("Can not set program page while already writing... from $" + Utils.hex16(cpu.getPC()));
        writeStatus(PROGRAM_PAGE_MILLIS);
        ensureLoaded(blockWriteAddress);
        for (int i = 0; i < readMemory.length; i++) {
            readMemory[i] &= buffer[i];
        }
        writeBack(blockWriteAddress, readMemory);
    }
