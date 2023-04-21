    private static void writeToBus(Entry entry) {
        if (actAsDummy) return;
        try {
            JavaSpace systemBus = (JavaSpace) BusConnectionManager.getSharedInstance().getBus(SystemBusArea.INCOMING);
            systemBus.write(entry, null, Lease.FOREVER);
        } catch (Exception e) {
            log.error("LindaReadWithSpaceThread: Could not write to Systen BUS " + e.toString(), e);
        }
    }
