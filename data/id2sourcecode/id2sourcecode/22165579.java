    JComponent getTitleComponent() {
        JPanel titleBar = new JPanel();
        titleBar.setOpaque(false);
        titleBar.setLayout(new BorderLayout());
        JPopupMenu popupMenu = new ScrollablePopupMenu();
        final String title;
        if (description != null) {
            title = "Edit description";
        } else {
            title = "Add description";
        }
        JMenuItem addDescriptionMenuItem = new JMenuItem(title);
        addDescriptionMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String response = (String) JOptionPane.showInputDialog(null, "Enter a description", title, JOptionPane.QUESTION_MESSAGE, null, null, description);
                if (response == null) {
                    return;
                } else if (response.length() == 0) {
                    setDescription(null);
                } else {
                    setDescription(response);
                }
            }
        });
        popupMenu.add(addDescriptionMenuItem);
        if (description != null) {
            JMenuItem removeDescriptionMenuItem = new JMenuItem("Remove description");
            removeDescriptionMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    setDescription(null);
                }
            });
            popupMenu.add(removeDescriptionMenuItem);
        }
        popupMenu.addSeparator();
        final JCheckBoxMenuItem showChannelsInTitleMenuItem = new JCheckBoxMenuItem("Show channels in title", showChannelsInTitle);
        showChannelsInTitleMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                setShowChannelsInTitle(showChannelsInTitleMenuItem.isSelected());
            }
        });
        popupMenu.add(showChannelsInTitleMenuItem);
        if (channels.size() > 0) {
            popupMenu.addSeparator();
            Iterator<String> i = channels.iterator();
            while (i.hasNext()) {
                final String channelName = i.next();
                JMenuItem unsubscribeChannelMenuItem = new JMenuItem("Unsubscribe from " + channelName);
                unsubscribeChannelMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        removeChannel(channelName);
                    }
                });
                popupMenu.add(unsubscribeChannelMenuItem);
            }
        }
        titleBar.setComponentPopupMenu(popupMenu);
        titleBar.addMouseListener(new MouseInputAdapter() {
        });
        if (description != null) {
            titleBar.add(getDescriptionComponent(), BorderLayout.WEST);
        }
        JComponent titleComponent = getChannelComponent();
        if (titleComponent != null) {
            titleBar.add(titleComponent, BorderLayout.CENTER);
        }
        return titleBar;
    }
