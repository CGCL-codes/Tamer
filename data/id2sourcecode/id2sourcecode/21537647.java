    public static void installInflatedLock(Object k, Monitor m) {
        Assert._assert(m.monitor_owner == Unsafe.getThreadBlock());
        Assert._assert(m.entry_count >= 1);
        for (; ; ) {
            HeapAddress status_address = (HeapAddress) HeapAddress.addressOf(k).offset(ObjectLayout.STATUS_WORD_OFFSET);
            int oldlockword = status_address.peek4();
            if (oldlockword < 0) {
                Assert._assert(m.entry_count == 1);
                m.free();
                Monitor m2 = getMonitor(oldlockword);
                if (TRACE) SystemInterface.debugwriteln("Inflated by another thread! lockword=" + Strings.hex8(oldlockword) + " lock=" + m2);
                Assert._assert(m != m2);
                m2.lock(Unsafe.getThreadBlock());
                return;
            }
            int status_flags = oldlockword & ObjectLayout.STATUS_FLAGS_MASK;
            HeapAddress m_addr = HeapAddress.addressOf(m);
            if ((m_addr.to32BitValue() & ObjectLayout.STATUS_FLAGS_MASK) != 0 || (m_addr.to32BitValue() & ObjectLayout.LOCK_EXPANDED) != 0) {
                Assert.UNREACHABLE("Monitor object has address " + m_addr.stringRep());
            }
            int newlockword = m_addr.to32BitValue() | ObjectLayout.LOCK_EXPANDED | status_flags;
            status_address.atomicCas4(status_flags, newlockword);
            if (Unsafe.isEQ()) {
                if (TRACE) SystemInterface.debugwriteln("Thread " + m.monitor_owner.getThreadId() + " obtained inflated lock! new lockword=" + Strings.hex8(newlockword));
                return;
            } else {
                if (TRACE) SystemInterface.debugwriteln("Thread " + m.monitor_owner.getThreadId() + " failed to obtain inflated lock, lockword was " + Strings.hex8(oldlockword));
            }
            Thread.yield();
        }
    }
