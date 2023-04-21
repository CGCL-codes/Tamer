    public static void cpu_writemem16(int address, int data) {
        int hw = cur_mwhard.read(address >> (ABITS2_16 + ABITS_MIN_16));
        if (NOT(hw) != 0) {
            RAM[address] = (char) data;
            return;
        }
        if (hw >= MH_HARDMAX) {
            hw = writehardware.read(((hw - MH_HARDMAX) << MH_SBITS) + ((address >> ABITS_MIN_16) & MHMASK(ABITS2_16)));
            if (hw == 0) {
                RAM[address] = (char) data;
                return;
            }
        }
        memorywritehandler[hw].handler(address - memorywriteoffset[hw], data);
    }
