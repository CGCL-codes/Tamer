    public void setValue(double val) {
        if (ch_ != null) {
            try {
                ch_.putVal(val);
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
