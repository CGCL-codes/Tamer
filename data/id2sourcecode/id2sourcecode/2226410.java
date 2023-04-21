    public void addChannelViewContainer(ChannelViewContainer container) {
        if (tabbedPane == null) {
            Container contentPane = getContentPane();
            tabbedPane = new JTabbedPane();
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(container.getChannelPane()), container.getUsersPanel());
            splitPane.setResizeWeight(1.0);
            tabbedPane.add(container.getChannelName(), splitPane);
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            bottomPanel.add(container.getMessageInputField(), BorderLayout.CENTER);
            bottomPanel.add(container.getSendButton(), BorderLayout.EAST);
            bottomPanel.add(container.getEmotionComboBox(), BorderLayout.WEST);
            contentPane.setLayout(new BorderLayout());
            contentPane.add(splitPane, BorderLayout.CENTER);
            contentPane.add(container.getTopPanel(), BorderLayout.NORTH);
            contentPane.add(bottomPanel, BorderLayout.SOUTH);
            channelViews.put(container.getChannelName(), container);
        } else {
            Set keys = channelViews.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key.equals(container.getChannelName())) {
                    channelViews.remove(key);
                    channelViews.put(container.getChannelName(), container);
                    return;
                }
            }
        }
        channelViews.put(container.getChannelName(), container);
    }
