    public List<Channel> getChannelList() {
        List<Channel> lC = new ArrayList<Channel>();
        for (Integer i : htChannelTransponder.values()) {
            Channel c = htChannel.get(i);
            lC.add(c);
        }
        ChannelComparator jvdrComparator = new ChannelComparator();
        Comparator<Channel> comparator = jvdrComparator.getComparator(ChannelComparator.CompTyp.Number);
        Collections.sort(lC, comparator);
        return lC;
    }
