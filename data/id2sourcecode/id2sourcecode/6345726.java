    public void callInst(Address from, Address to) {
        int head = 0;
        int end = ncounters;
        int index = ncounters / 2;
        int linaddr = to.getLinearAddress();
        while (head < end) {
            int l = linearAddrs[index];
            if (linaddr < l) {
                end = index;
                index = index / 2;
            } else if (linaddr > l) {
                head = index + 1;
                index = (index + end) / 2;
            } else {
                break;
            }
        }
        if (head < end) {
            callCounters[index]++;
        } else if (ncounters < NCOUNTERS) {
            if (ncounters > head) {
                System.arraycopy(callCounters, head, callCounters, head + 1, ncounters - head);
                System.arraycopy(funcAddrs, head, funcAddrs, head + 1, ncounters - head);
                System.arraycopy(linearAddrs, head, linearAddrs, head + 1, ncounters - head);
            }
            callCounters[head] = 1;
            funcAddrs[head] = to;
            linearAddrs[head] = linaddr;
            ncounters++;
        }
        refresh++;
        if ((refresh & 0xfff) == 0) {
            callTableModel.fireTableDataChanged();
        }
    }
