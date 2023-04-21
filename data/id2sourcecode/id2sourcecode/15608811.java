    protected Runtime(int pageSize, int totalPages, boolean exec) {
        if (pageSize <= 0) throw new IllegalArgumentException("pageSize <= 0");
        if (totalPages <= 0) throw new IllegalArgumentException("totalPages <= 0");
        if ((pageSize & (pageSize - 1)) != 0) throw new IllegalArgumentException("pageSize not a power of two");
        int _pageShift = 0;
        while (pageSize >>> _pageShift != 1) _pageShift++;
        pageShift = _pageShift;
        int heapStart = heapStart();
        int totalMemory = totalPages * pageSize;
        int stackSize = max(totalMemory / 512, ARG_MAX + 65536);
        int stackPages = 0;
        if (totalPages > 1) {
            stackSize = max(stackSize, pageSize);
            stackSize = (stackSize + pageSize - 1) & ~(pageSize - 1);
            stackPages = stackSize >>> pageShift;
            heapStart = (heapStart + pageSize - 1) & ~(pageSize - 1);
            if (stackPages + STACK_GUARD_PAGES + (heapStart >>> pageShift) >= totalPages) throw new IllegalArgumentException("total pages too small");
        } else {
            if (pageSize < heapStart + stackSize) throw new IllegalArgumentException("total memory too small");
            heapStart = (heapStart + 4095) & ~4096;
        }
        stackBottom = totalMemory - stackSize;
        heapEnd = heapStart;
        readPages = new int[totalPages][];
        writePages = new int[totalPages][];
        if (totalPages == 1) {
            readPages[0] = writePages[0] = new int[pageSize >> 2];
        } else {
            for (int i = (stackBottom >>> pageShift); i < writePages.length; i++) {
                readPages[i] = writePages[i] = new int[pageSize >> 2];
            }
        }
        if (!exec) {
            fds = new FD[OPEN_MAX];
            closeOnExec = new boolean[OPEN_MAX];
            InputStream stdin = win32Hacks ? new Win32ConsoleIS(System.in) : System.in;
            addFD(new TerminalFD(stdin));
            addFD(new TerminalFD(System.out));
            addFD(new TerminalFD(System.err));
        }
    }
