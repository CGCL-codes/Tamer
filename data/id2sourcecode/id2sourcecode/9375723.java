    protected final void writePhysical() {
        if (!writeQueue.isEmtpy()) {
            List<DataPackage> dataPackages = writeQueue.readAvailable();
            for (DataPackage dataPackage : dataPackages) {
                try {
                    logFine("send datagram package (" + dataPackage + ")");
                    dataPackage.writeTo(channel);
                    incNumberOfHandledOutgoingDatagram();
                } catch (IOException ioe) {
                    LOG.warning("couldn't write datagram to " + dataPackage.getAddress() + " .Reason: " + ioe.toString());
                }
            }
        }
    }
