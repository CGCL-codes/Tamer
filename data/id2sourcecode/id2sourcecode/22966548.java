    private void deadPoolCheck(SmfPvMonitor.IAction ifcAct, SmfPvMonitor monSrc) {
        if (!this.bolActive) {
            System.err.println("Active monitor  in dead pool for device: " + monSrc.getDevice() + " handle: " + monSrc.getChannelHandle());
            System.err.println("  shutting it down");
            monSrc.clear();
        }
    }
