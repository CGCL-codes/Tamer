    public void channelEnd(ChanList list) {
        ChannelInfo[] info = getChanList().getChannels();
        sort(info);
        int count = info.length;
        if (count > 1024) count = 1024;
        String[] lines = new String[count];
        for (int i = 0; i < count; i++) lines[i] = format(info[i]);
        clear(getSource());
        _list.addLines(lines);
        _list.setFirst(0);
        _scroll.setMaximum(_list.getLineCount() - 1);
        _scroll.setValue(_list.getLast());
        _hscroll.setMaximum(_list.getLogicalWidth() / 10);
    }
