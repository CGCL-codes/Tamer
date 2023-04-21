    public static void scanCurrentThreadStack(int skip) {
        if (SimpleAllocator.TRACE_GC) Debug.writeln("Scanning current thread stack");
        StackAddress fp = StackAddress.getBasePointer();
        StackAddress sp = StackAddress.getStackPointer();
        CodeAddress ip = (CodeAddress) fp.offset(HeapAddress.size()).peek();
        while (!fp.isNull()) {
            if (SimpleAllocator.TRACE_GC) {
                Debug.write("Scanning stack frame fp=", fp);
                Debug.write(" sp=", sp);
                Debug.writeln(" ip=", ip);
            }
            if (--skip < 0) {
                while (fp.difference(sp) > 0) {
                    if (SimpleAllocator.TRACE_GC) {
                        Debug.write("sp: ", sp);
                        Debug.writeln("  ", sp.peek());
                    }
                    addConservativeAddress(sp);
                    sp = (StackAddress) sp.offset(HeapAddress.size());
                }
            } else {
                if (SimpleAllocator.TRACE_GC) Debug.writeln("Skipping this frame.");
            }
            ip = (CodeAddress) fp.offset(HeapAddress.size()).peek();
            sp = fp;
            fp = (StackAddress) fp.peek();
        }
    }
