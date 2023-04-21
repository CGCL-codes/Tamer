    private void printServicesInfo(final TripMonitorPortal service) {
        System.out.println("Trip Monitors:  " + service.getTripMonitorNames());
        final Vector monitors = service.getTripMonitorNames();
        for (Object monitor : monitors) {
            System.out.println("Monitor: " + monitor + ", Enabled:  " + service.isEnabled(monitor.toString()));
            System.out.println("Monitor: " + monitor + ", Enabled:  " + "Trip Records:  " + service.getTripRecords(monitor.toString()));
            System.out.println("Monitor: " + monitor + ", Enabled:  " + "Channel Info:  " + service.getChannelInfo(monitor.toString()));
        }
    }
