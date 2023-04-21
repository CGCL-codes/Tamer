    protected Object isInRange(long address_long) {
        try {
            this_mon.enter();
            checkRebuild();
            if (mergedRanges.length == 0) {
                return (null);
            }
            int bottom = 0;
            int top = mergedRanges.length - 1;
            int current = -1;
            while (top >= 0 && bottom < mergedRanges.length && bottom <= top) {
                current = (bottom + top) / 2;
                IpRange e = mergedRanges[current];
                long this_start = e.getStartIpLong();
                long this_end = e.getMergedEndLong();
                if (address_long == this_start) {
                    break;
                } else if (address_long > this_start) {
                    if (address_long <= this_end) {
                        break;
                    }
                    bottom = current + 1;
                } else if (address_long == this_end) {
                    break;
                } else {
                    if (address_long >= this_start) {
                        break;
                    }
                    top = current - 1;
                }
            }
            if (top >= 0 && bottom < mergedRanges.length && bottom <= top) {
                IpRange e = mergedRanges[current];
                if (address_long <= e.getEndIpLong()) {
                    return (e);
                }
                IpRange[] merged = e.getMergedEntries();
                if (merged == null) {
                    Debug.out("IPAddressRangeManager: inconsistent merged details - no entries");
                    return (null);
                }
                for (int i = 0; i < merged.length; i++) {
                    IpRange me = merged[i];
                    if (me.getStartIpLong() <= address_long && me.getEndIpLong() >= address_long) {
                        return (me);
                    }
                }
                Debug.out("IPAddressRangeManager: inconsistent merged details - entry not found");
            }
            return (null);
        } finally {
            this_mon.exit();
        }
    }
