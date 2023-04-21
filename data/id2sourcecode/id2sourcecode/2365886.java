    public static void leave(int address) {
        int lockCount = Memory.mem_readd(address + 4);
        lockCount--;
        Memory.mem_writed(address + 4, lockCount);
        int recursionCount = Memory.mem_readb(address + 8);
        recursionCount--;
        if (recursionCount > 0) {
            Memory.mem_writed(address + 8, recursionCount);
        } else {
            WaitObject object = WaitObject.getWait(Memory.mem_readd(address));
            if (object.waiting.size() > 0) {
                WaitGroup group = (WaitGroup) object.waiting.remove(0);
                Memory.mem_writed(address + 12, group.thread.getHandle());
                Scheduler.addThread(group.thread, false);
            } else {
                Memory.mem_writed(address + 12, 0);
                Memory.mem_writed(address + 8, 0);
            }
        }
    }
