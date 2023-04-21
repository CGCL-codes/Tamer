    private Channel lookupAndCreateChannel(String label, String url) {
        log.info("Looking up channel: " + label);
        Channel achan = locateChannel(url);
        if (achan != null) {
            verifyChannel(label, achan.getId());
        } else {
            log.info(label + " not yet located.");
        }
        achan = getChannel(url, achan);
        verifyChannel(label + " that was created:", achan.getId());
        return achan;
    }
