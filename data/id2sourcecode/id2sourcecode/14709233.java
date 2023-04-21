    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            final int listIndex = ((JList) e.getSource()).locationToIndex(e.getPoint());
            final String user = ((JList) e.getSource()).getModel().getElementAt(listIndex).toString();
            ActiveChannel.active(user);
            Controller.getInstance().getClient().getChannelList().setChannelData(new Vector<String>(Controller.getInstance().getCWatcher().chanList()));
        }
    }
