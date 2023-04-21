    public void setChannels(ChatChannelsData channelsData) {
        ArrayList<Channel> newChannels = new ArrayList<Channel>();
        for (int i = 0; i < channelsData.getChannelsCount(); i++) {
            ChatChannelData channelData = channelsData.getChannelAt(i);
            newChannels.add(new Channel(channelData.getName(), channelData.isActive()));
        }
        Collections.sort(newChannels, new Comparator<Channel>() {

            public int compare(Channel c1, Channel c2) {
                int priority1, priority2;
                AllyData ally = Client.getInstance().getAllyDialog().getAlly();
                String allyName = ally.getId() != 0 ? ally.getName() : "";
                if (c1.getName().startsWith("%")) {
                    priority1 = 5;
                } else if (c1.getName().startsWith("@")) {
                    priority1 = 4;
                } else if (c1.getName().startsWith("$")) {
                    if (c1.getName().substring(1).equals(allyName)) priority1 = 3; else priority1 = 2;
                } else {
                    priority1 = 1;
                }
                if (c2.getName().startsWith("%")) {
                    priority2 = 5;
                } else if (c2.getName().startsWith("@")) {
                    priority2 = 4;
                } else if (c2.getName().startsWith("$")) {
                    if (c2.getName().substring(1).equals(allyName)) priority2 = 3; else priority2 = 2;
                } else {
                    priority2 = 1;
                }
                if (priority1 < priority2) return 1; else if (priority1 > priority2) return -1; else return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
        this.channels = newChannels;
        updateTabLabels();
    }
