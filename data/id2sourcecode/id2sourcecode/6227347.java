    public void yield() {
        if (this != Unsafe.getThreadBlock()) {
            SystemInterface.debugwriteln("Yield called on " + this + " from thread " + Unsafe.getThreadBlock());
            Assert.UNREACHABLE();
        }
        this.disableThreadSwitch();
        StackAddress esp = StackAddress.getStackPointer();
        registers.setEsp((StackAddress) esp.offset(-CodeAddress.size() - HeapAddress.size()));
        registers.setEbp(StackAddress.getBasePointer());
        registers.setControlWord(0x027f);
        registers.setStatusWord(0x4000);
        registers.setTagWord(0xffff);
        this.getNativeThread().yieldCurrentThread();
    }
