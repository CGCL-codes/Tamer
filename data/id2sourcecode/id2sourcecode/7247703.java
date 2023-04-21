    public final synchronized void lockWriteExit() {
        java.lang.Thread W = this.writer;
        java.lang.Thread T = java.lang.Thread.currentThread();
        if (T == W) {
            this.enterWrite -= 1;
            if (0 == this.enterWrite) {
                if (this.compareAndSet(false, true)) {
                    this.writer = null;
                    this.notify();
                } else throw new alto.sys.Error.State();
            } else return;
        } else if (null == W) throw new alto.sys.Error.State(); else throw new alto.sys.Error.State();
    }
