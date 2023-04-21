    public List<InputChannelItemInterface> getInputChannelItemList() {
        List<InputChannelItemInterface> inputChannelItemList = new ArrayList<InputChannelItemInterface>(strips.size());
        for (Iterator<Strip> it = strips.iterator(); it.hasNext(); ) {
            final Strip s = it.next();
            final InputChannelItemInterface c = s.getChannel(0);
            if (c != null) inputChannelItemList.add(c);
        }
        return inputChannelItemList;
    }
