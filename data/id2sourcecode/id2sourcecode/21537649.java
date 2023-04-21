    public void unlock(jq_Thread t) {
        jq_Thread m_t = this.monitor_owner;
        if (m_t != t) {
            SystemInterface.debugwriteln("We (" + t + ") tried to unlock lock " + this + " owned by " + m_t);
            throw new IllegalMonitorStateException();
        }
        if (--this.entry_count > 0) {
            if (TRACE) SystemInterface.debugwriteln("Decrementing lock " + this + " entry count " + this.entry_count);
            return;
        }
        if (TRACE) SystemInterface.debugwriteln("We (" + t + ") are unlocking lock " + this + ", current waiters=" + this.atomic_count);
        this.monitor_owner = null;
        HeapAddress ac_loc = (HeapAddress) HeapAddress.addressOf(this).offset(_atomic_count.getOffset());
        ac_loc.atomicSub(1);
        if (Unsafe.isGE()) {
            if (TRACE) SystemInterface.debugwriteln((this.atomic_count + 1) + " threads are waiting on released lock " + this + ", releasing semaphore.");
            this.releaseSemaphore();
        } else {
            if (TRACE) SystemInterface.debugwriteln("No threads are waiting on released lock " + this + ".");
        }
    }
