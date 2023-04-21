    public void newSubscription(String url) {
        RssChannelDialog dialog = RssChannelDialog.showDialog(parent, url);
        if (dialog.getDialogResult() == RssChannelDialog.YES_OPTION) {
            Object path[] = parent.getSelectionModel().getSelectionPath();
            model.add((RssGroupNode) path[path.length - 1], dialog.getChannel());
        }
    }
