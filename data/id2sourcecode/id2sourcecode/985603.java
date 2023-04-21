    public void postShowChange() {
        context.getShow().getChannels().addNameListener(this);
        channels.clear();
        Channels c = context.getShow().getChannels();
        for (int i = 0; i < c.size(); i++) {
            channels.add(c.get(i));
        }
        sort();
        fireTableDataChanged();
    }
