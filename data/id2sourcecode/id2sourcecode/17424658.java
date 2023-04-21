    public boolean writed_checked(long address, int val) {
        int addr = (int) (address & 4095);
        if (Memory.host_readd(hostmem + addr) == (val & 0xFFFFFFFFl)) return false;
        if (write_map.readd(addr) == 0) {
            if (active_blocks == 0) {
                active_count--;
                if (active_count == 0) Release();
            }
        } else {
            if (invalidation_map == null) {
                invalidation_map = new Ptr(4096);
            }
            invalidation_map.writed(addr, invalidation_map.readd(addr) + 0x1010101);
            if (InvalidateRange(addr, addr + 3)) {
                CPU.cpu.exception.which = Core_dynrec.SMC_CURRENT_BLOCK;
                return true;
            }
        }
        Memory.host_writed(hostmem + addr, val);
        return false;
    }
