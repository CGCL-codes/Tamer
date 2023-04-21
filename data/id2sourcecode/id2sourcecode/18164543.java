    protected void refreshForMonitorSelection() {
        final ServiceHandler serviceHandler = _selectedServiceHandler;
        final String monitorID = _selectedTripMonitorID;
        final JButton publishButton = (JButton) MAIN_WINDOW_REFERENCE.getView("PublishButton");
        if (serviceHandler != null && monitorID != null) {
            CHANNEL_LIST_MODEL.setChannelRefs(serviceHandler.getChannelRefs(monitorID));
            TRIPS_TABLE_MODEL.setTripRecords(serviceHandler.getTripRecords(monitorID));
            publishButton.setEnabled(true);
        } else if (monitorID == null) {
            CHANNEL_LIST_MODEL.setChannelRefs(new ArrayList<ChannelRef>());
            TRIPS_TABLE_MODEL.setTripRecords(new ArrayList<TripRecord>());
            publishButton.setEnabled(false);
        }
    }
