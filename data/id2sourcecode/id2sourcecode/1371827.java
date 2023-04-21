    protected boolean lockImpl() throws Exception {
        boolean isValid;
        if (fl != null && fl.isValid()) {
            return true;
        }
        trace("lockImpl(): fc = raf.getChannel()");
        fc = raf.getChannel();
        trace("lockImpl(): fl = fc.tryLock()");
        fl = null;
        try {
            fl = fc.tryLock(0, MIN_LOCK_REGION, false);
            trace("lockImpl(): fl = " + fl);
        } catch (Exception e) {
            trace(e.toString());
        }
        trace("lockImpl(): f.deleteOnExit()");
        f.deleteOnExit();
        isValid = fl != null && fl.isValid();
        trace("lockImpl():isValid(): " + isValid);
        return isValid;
    }
