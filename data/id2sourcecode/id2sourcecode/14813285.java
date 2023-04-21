    public void restoreFromMemory() {
        if (ch_ != null) {
            try {
                ch_.putVal(memValue);
            } catch (ConnectionException e) {
                stopScanWithReport(e, getChannelName(), "JCA ConnectionException");
                return;
            } catch (PutException e) {
                stopScanWithReport(e, getChannelName(), "JCA PutException");
                return;
            }
        }
        measure();
    }
