    public boolean writew_checked(long address, int val) {
        int addr = (int) (address & 4095);
        if (Memory.host_readw(hostmem + addr) == (val & 0xFFFF)) return false;
        if (write_map.readw(addr) == 0) {
            if (active_blocks == 0) {
                active_count--;
                if (active_count == 0) Release();
            }
        } else {
            if (invalidation_map == null) {
                invalidation_map = new Ptr(4096);
            }
            invalidation_map.writew(addr, invalidation_map.readw(addr) + 0x101);
            if (InvalidateRange(addr, addr + 1)) {
                CPU.cpu.exception.which = Core_dynrec.SMC_CURRENT_BLOCK;
                return true;
            }
        }
        Memory.host_writew(hostmem + addr, val);
        return false;
    }
