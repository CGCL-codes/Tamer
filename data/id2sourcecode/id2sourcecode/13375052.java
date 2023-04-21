    private void makeCompleteChannelList() {
        final ClassLoader cl = this.getClass().getClassLoader();
        InputStream listStream = cl.getResourceAsStream(CHANNELNAMEFILE);
        BufferedReader b = new BufferedReader(new InputStreamReader(listStream));
        completeChannelList = new ArrayList<StripChannel>();
        try {
            while (b.ready()) {
                String line = b.readLine();
                completeChannelList.add(StripChannel.createChannel(line));
            }
        } catch (IOException e) {
            System.err.println("StripHandler: I/O exception loading channels");
        }
        channelGroupMap = StripChannelGroup.createChannelGroupMap(completeChannelList);
        Iterator<StripChannelGroup> it = channelGroupMap.values().iterator();
        while (it.hasNext()) {
            final StripChannelGroup scg = it.next();
            final StripChannel sc = new StripChannel(StripChannel.PREFIX, scg.getPrefix(), scg.getPrefix() + StripChannelGroup.GROUPDESCRIPTION);
            completeChannelList.add(sc);
        }
        Collections.sort(completeChannelList, new Comparator<StripChannel>() {

            public int compare(StripChannel o1, StripChannel o2) {
                return o1.getChannelID().compareTo(o2.getChannelID());
            }
        });
    }
