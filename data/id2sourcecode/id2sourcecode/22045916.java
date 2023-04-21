    static void test_mmap() {
        int psize = VM_Memory.getPagesize();
        int size = 1024 * 1024;
        int ro = VM_Memory.PROT_READ;
        VM_Address base = VM_Address.fromIntZeroExtend(0x38000000);
        VM_Address addr = VM_Memory.mmap(base, size);
        VM.sysWrite("page size = ");
        VM.sysWrite(psize);
        VM.sysWrite("\n");
        VM.sysWrite("requested ");
        VM.sysWrite(size);
        VM.sysWrite(" bytes at ");
        VM.sysWrite(base);
        VM.sysWrite("\n");
        VM.sysWrite("mmap call returned ");
        VM.sysWrite(addr);
        VM.sysWrite("\n");
        if (addr.NE(VM_Address.fromIntSignExtend(-1))) {
            VM_Magic.setMemoryInt(addr, 17);
            if (VM_Magic.getMemoryInt(addr) == 17) {
                VM.sysWrite("write and read in memory region succeeded\n");
            } else {
                VM.sysWrite("read in memory region did not return value written\n");
            }
            if (!VM_Memory.mprotect(addr, size, ro)) {
                VM.sysWrite("mprotect failed\n");
            } else {
                VM.sysWrite("mprotect succeeded!\n");
            }
            if (VM_Magic.getMemoryInt(addr) == 17) {
                VM.sysWrite("read in memory region succeeded\n");
            } else {
                VM.sysWrite("read in memory region did not return value written\n");
            }
            if (VM_Memory.munmap(addr, size) == 0) VM.sysWrite("munmap succeeded!\n"); else VM.sysWrite("munmap failed\n");
        }
        addr = VM_Memory.mmap(size);
        VM.sysWrite("requested ");
        VM.sysWrite(size);
        VM.sysWrite(" bytes at any address\n");
        VM.sysWrite("mmap call returned ");
        VM.sysWrite(addr);
        VM.sysWrite("\n");
        if (addr.NE(VM_Address.fromIntSignExtend(-1))) {
            VM_Magic.setMemoryInt(addr, 17);
            if (VM_Magic.getMemoryInt(addr) == 17) {
                VM.sysWrite("write and read in memory region succeeded\n");
            } else {
                VM.sysWrite("read in memory region did not return value written\n");
            }
            if (!VM_Memory.mprotect(addr, size, ro)) {
                VM.sysWrite("mprotect failed\n");
            } else {
                VM.sysWrite("mprotect succeeded!\n");
            }
            if (VM_Magic.getMemoryInt(addr) == 17) {
                VM.sysWrite("read in memory region succeeded\n");
            } else {
                VM.sysWrite("read in memory region did not return value written\n");
            }
            if (VM_Memory.munmap(addr, size) == 0) VM.sysWrite("munmap succeeded!\n"); else VM.sysWrite("munmap failed\n");
        }
        VM.sysWrite("mmap tests done\n");
    }
