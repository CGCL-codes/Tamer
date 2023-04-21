    public final int sbrk(int incr) {
        if (incr < 0) return -ENOMEM;
        if (incr == 0) return heapEnd;
        incr = (incr + 3) & ~3;
        int oldEnd = heapEnd;
        int newEnd = oldEnd + incr;
        if (newEnd >= stackBottom) return -ENOMEM;
        if (writePages.length > 1) {
            int pageMask = (1 << pageShift) - 1;
            int pageWords = (1 << pageShift) >>> 2;
            int start = (oldEnd + pageMask) >>> pageShift;
            int end = (newEnd + pageMask) >>> pageShift;
            try {
                for (int i = start; i < end; i++) readPages[i] = writePages[i] = new int[pageWords];
            } catch (OutOfMemoryError e) {
                if (STDERR_DIAG) System.err.println("WARNING: Caught OOM Exception in sbrk: " + e);
                return -ENOMEM;
            }
        }
        heapEnd = newEnd;
        return oldEnd;
    }
