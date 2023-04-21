    static void addOpenLink(JPopupMenu menu, Message linkMessage, String url) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }
        final Desktop desktop = Desktop.getDesktop();
        if (url != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            JMenuItem openInBrowserItem = new JMenuItem(linkMessage.get());
            openInBrowserItem.addActionListener(new OpenUrlAction(url));
            menu.add(openInBrowserItem);
        }
    }
