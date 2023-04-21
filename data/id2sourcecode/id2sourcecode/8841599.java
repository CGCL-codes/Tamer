    public ChannelFrame(Channel chan) {
        super("[" + chan.getName() + "] " + chan.getTopic(), true, true, true, true);
        _channel = chan;
        setFrameIcon(IconManager.getIcon("Users"));
        _chatPanel = new ChannelPanel(_channel, this);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(_chatPanel, BorderLayout.CENTER);
        addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameClosed(InternalFrameEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ((ChannelPanel) getChannelPanel()).part();
                    }
                });
            }
        });
        _channel.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Topic")) {
                    setTitle("[" + _channel.getName() + "] " + evt.getNewValue());
                }
            }
        });
        validate();
    }
