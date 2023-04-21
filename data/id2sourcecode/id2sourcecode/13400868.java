    private synchronized void updateChannelMap(SwitchContainer sc) {
        String address = sc.getAddress();
        if (channelMap.get(address) != null) {
            return;
        }
        channelMap.put(address, new Integer(sc.getChannelCount()));
    }
