    @Override
    public void initialize(final Connection source) {
        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    new ConfigurationFrame(source.getConnectionSettings());
                } catch (OperationCancelledException e) {
                }
            }
        });
        settingsMenuItems.put(source, settings);
        for (int i = 0; i < menuBar.getMenuComponentCount(); i++) {
            Object x = menuBar.getMenuComponent(i);
            if (x instanceof Separator) {
                menuBar.add(settings, i);
                break;
            }
        }
        titleChanged(source);
        String channel = source.getChannel();
        if (channel != null) {
            joinedChannel(source, channel);
            for (BNetUser user : source.getUsers()) channelUser(source, user);
        }
    }
