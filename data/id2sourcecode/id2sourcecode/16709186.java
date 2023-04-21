    private JPopupMenu getChannelPopup() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;
        final List<String> channels = getSelectedChannels();
        if (channels.isEmpty()) {
            return null;
        }
        String plural = (channels.size() == 1) ? "" : "s";
        List<Extension> extensions = dataPanelManager.getExtensions(channels);
        if (extensions.size() > 0) {
            for (final Extension extension : extensions) {
                menuItem = new JMenuItem("View channel" + plural + " with " + extension.getName());
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        viewChannels(channels, extension);
                    }
                });
                popup.add(menuItem);
            }
            popup.addSeparator();
        }
        if (dataPanelManager.isAnyChannelSubscribed(channels)) {
            menuItem = new JMenuItem("Unsubscribe from channel" + plural);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    dataPanelManager.unsubscribeChannels(channels);
                }
            });
            popup.add(menuItem);
            popup.addSeparator();
        }
        String mime = getMime(channels);
        if (mime != null) {
            if (mime.equals("application/octet-stream")) {
                menuItem = new JMenuItem("Export data channel" + plural + "...", DataViewer.getIcon("icons/export.gif"));
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        ActionFactory.getInstance().getDataExportAction().exportData(channels);
                    }
                });
                popup.add(menuItem);
            } else if (mime.equals("image/jpeg")) {
                menuItem = new JMenuItem("Export video channel" + plural + "...", DataViewer.getIcon("icons/export.gif"));
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        JFrame frame = RDV.getInstance(RDV.class).getMainFrame();
                        new ExportVideoDialog(frame, rbnb, channels);
                    }
                });
                popup.add(menuItem);
            } else {
                popup.remove(popup.getComponentCount() - 1);
            }
        }
        return popup;
    }
