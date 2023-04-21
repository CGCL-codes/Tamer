    static void mem_dump() {
        int cpu;
        int naddr, addr;
        char nhw, hw;
        if (errorlog == null) return;
        for (cpu = 0; cpu < totalcpu; cpu++) {
            fprintf(errorlog, "cpu %d read memory \n", cpu);
            addr = 0;
            naddr = 0;
            nhw = 0xff;
            while ((addr >> mhshift[cpu][0]) <= mhmask[cpu][0]) {
                hw = cur_mr_element[cpu].read(addr >> mhshift[cpu][0]);
                if (hw >= MH_HARDMAX) {
                    hw = readhardware.read(((hw - MH_HARDMAX) << MH_SBITS) + ((addr >> mhshift[cpu][1]) & mhmask[cpu][1]));
                    if (hw >= MH_HARDMAX) hw = readhardware.read(((hw - MH_HARDMAX) << MH_SBITS) + (addr & mhmask[cpu][2]));
                }
                if (nhw != hw) {
                    if (addr != 0) fprintf(errorlog, "  %06x(%06x) - %06x = %02x\n", new Object[] { naddr, memoryreadoffset[nhw], addr - 1, Integer.valueOf(nhw) });
                    nhw = hw;
                    naddr = addr;
                }
                addr++;
            }
            fprintf(errorlog, "  %06x(%06x) - %06x = %02x\n", naddr, memoryreadoffset[nhw], addr - 1, Integer.valueOf(nhw));
            fprintf(errorlog, "cpu %d write memory \n", cpu);
            naddr = 0;
            addr = 0;
            nhw = 0xff;
            while ((addr >> mhshift[cpu][0]) <= mhmask[cpu][0]) {
                hw = cur_mw_element[cpu].read(addr >> mhshift[cpu][0]);
                if (hw >= MH_HARDMAX) {
                    hw = writehardware.read(((hw - MH_HARDMAX) << MH_SBITS) + ((addr >> mhshift[cpu][1]) & mhmask[cpu][1]));
                    if (hw >= MH_HARDMAX) hw = writehardware.read(((hw - MH_HARDMAX) << MH_SBITS) + (addr & mhmask[cpu][2]));
                }
                if (nhw != hw) {
                    if (addr != 0) fprintf(errorlog, "  %06x(%06x) - %06x = %02x\n", naddr, memorywriteoffset[nhw], addr - 1, Integer.valueOf(nhw));
                    nhw = hw;
                    naddr = addr;
                }
                addr++;
            }
            fprintf(errorlog, "  %06x(%06x) - %06x = %02x\n", naddr, memorywriteoffset[nhw], addr - 1, Integer.valueOf(nhw));
        }
    }
