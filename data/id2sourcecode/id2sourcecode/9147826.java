    public void channelAdded(ChannelInfo item, ChanList list) {
        int count = getChanList().getChannelCount();
        int total = getChanList().getIgnoredChannelCount() + count;
        if (total % 100 == 0) {
            clear(getSource());
            print("Retrieving available rooms... (" + count + "/" + total + ")");
            _list.setFirst(0);
        }
    }
