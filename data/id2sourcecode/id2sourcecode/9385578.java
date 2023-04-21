    private int translate(int vaddr, int size, boolean writing) throws MipsException {
        if (Lib.test(dbgProcessor)) System.out.println("\ttranslate vaddr=0x" + Lib.toHexString(vaddr) + (writing ? ", write" : ", read..."));
        if ((vaddr & (size - 1)) != 0) {
            Lib.debug(dbgProcessor, "\t\talignment error");
            throw new MipsException(exceptionAddressError, vaddr);
        }
        int vpn = pageFromAddress(vaddr);
        int offset = offsetFromAddress(vaddr);
        TranslationEntry entry = null;
        if (!usingTLB) {
            if (translations == null || vpn >= translations.length || translations[vpn] == null || !translations[vpn].valid) {
                privilege.stats.numPageFaults++;
                Lib.debug(dbgProcessor, "\t\tpage fault");
                throw new MipsException(exceptionPageFault, vaddr);
            }
            entry = translations[vpn];
        } else {
            for (int i = 0; i < tlbSize; i++) {
                if (translations[i].valid && translations[i].vpn == vpn) {
                    entry = translations[i];
                    break;
                }
            }
            if (entry == null) {
                privilege.stats.numTLBMisses++;
                Lib.debug(dbgProcessor, "\t\tTLB miss");
                throw new MipsException(exceptionTLBMiss, vaddr);
            }
        }
        if (entry.readOnly && writing) {
            Lib.debug(dbgProcessor, "\t\tread-only exception");
            throw new MipsException(exceptionReadOnly, vaddr);
        }
        int ppn = entry.ppn;
        if (ppn < 0 || ppn >= numPhysPages) {
            Lib.debug(dbgProcessor, "\t\tbad ppn");
            throw new MipsException(exceptionBusError, vaddr);
        }
        entry.used = true;
        if (writing) entry.dirty = true;
        int paddr = (ppn * pageSize) + offset;
        if (Lib.test(dbgProcessor)) System.out.println("\t\tpaddr=0x" + Lib.toHexString(paddr));
        return paddr;
    }
