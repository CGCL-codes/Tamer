    public ChannelGridPanel() {
        setLayout(new GridLayout(3, 4));
        setBackground(Color.black);
        for (int i = 0; i < 12; i++) {
            GenericChannel genericChannel = new GenericChannel(i);
            channels[i] = genericChannel;
        }
        for (int i = 0; i < channels.length; i++) {
            add(channels[i].getChannelDisplay());
        }
    }
