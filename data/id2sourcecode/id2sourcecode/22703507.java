    private static void getChannels() {
        log("Retrieving channel data");
        chanList.setListData(client.channels());
        log("Done retrieving channel data");
    }
