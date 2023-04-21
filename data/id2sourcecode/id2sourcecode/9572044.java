    private static final int findIndex(TISymbol[] varAddrMap, long addr) {
        int idx = 0;
        int end = varAddrMap.length;
        int pivot = (idx + end) / 2;
        while (idx < end) {
            long paddr = varAddrMap[pivot].getAddress();
            if (addr < paddr) end = pivot - 1; else if (addr > paddr) idx = pivot + 1; else break;
            pivot = (idx + end) / 2;
        }
        if (pivot >= varAddrMap.length) pivot = varAddrMap.length - 1;
        if (pivot > 0) while (varAddrMap[pivot].getAddress() > addr) pivot--;
        return pivot;
    }
