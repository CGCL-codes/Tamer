    public String[] findActivePVs(DuplicatePV[] pvs) {
        ArrayList channels = new ArrayList();
        ChannelFactory factory = ChannelFactory.defaultFactory();
        for (int i = 0; i < pvs.length; i++) channels.add(factory.getChannel(pvs[i].getName()));
        ChannelChecker checker = new ChannelChecker(channels);
        checker.checkThem();
        List<String> goodPVs = checker.getGoodPVs();
        return goodPVs.toArray(new String[goodPVs.size()]);
    }
