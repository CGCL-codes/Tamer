    public boolean writeb_checked(long address, int val) {
        int addr = (int) (address & 4095);
        if (Memory.host_readb(hostmem + addr) == (val & 0xFF)) return false;
        if (write_map.readb(addr) == 0) {
            if (active_blocks == 0) {
                active_count--;
                if (active_count == 0) Release();
            }
        } else {
            if (invalidation_map == null) {
                invalidation_map = new Ptr(4096);
            }
            invalidation_map.p[addr]++;
            if (InvalidateRange(addr, addr)) {
                CPU.cpu.exception.which = Core_dynrec.SMC_CURRENT_BLOCK;
                return true;
            }
        }
        Memory.host_writeb(hostmem + addr, (short) val);
        return false;
    }
