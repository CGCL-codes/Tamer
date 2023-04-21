    private void zeroJ2NThreadCounts(int threadPointer) throws Exception {
        if (threadPointer == 0) return;
        try {
            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.VM_Thread", "J2NYieldCount");
            mem.write(threadPointer + field.getOffset(), 0);
        } catch (BmapNotFoundException e) {
            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");
        }
        try {
            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.VM_Thread", "J2NLockFailureCount");
            mem.write(threadPointer + field.getOffset(), 0);
        } catch (BmapNotFoundException e) {
            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");
        }
        try {
            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.VM_Thread", "J2NTotalYieldDuration");
            mem.write(threadPointer + field.getOffset(), 0);
        } catch (BmapNotFoundException e) {
            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");
        }
        try {
            VM_Field field = bmap.findVMField("com.ibm.JikesRVM.VM_Thread", "J2NTotalLockDuration");
            mem.write(threadPointer + field.getOffset(), 0);
        } catch (BmapNotFoundException e) {
            throw new Exception("cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?");
        }
    }
