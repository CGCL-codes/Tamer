    protected void amphetaDeskChannelAdded(AmphetaDeskEvent evt) {
        if (mode == MODE_INTERACTIVE) {
            RssChannelDialog dialog = RssChannelDialog.showDialog(this, evt.getUrl());
            if (dialog.getDialogResult() == RssChannelDialog.YES_OPTION) {
                NodePath path = new NodePath(selectionModel.getSelectionPath());
                if (path.getLastElement() instanceof CategoryNode) {
                    channelModel.add((CategoryNode) path.getLastElement(), dialog.getChannel());
                } else {
                    channelModel.add(path.getLastCategory(), dialog.getChannel());
                }
            }
        } else {
            channelModel.remove(channelModel.getRootNode(), 0);
            RssChannel channel = new RssChannel();
            channel.setUrl(evt.getUrl());
            channel.setName(rb.getString("Linked_by_AmphetaDesk_Adapter"));
            Object path[] = selectionModel.getSelectionPath();
            channelModel.add((RssGroupNode) path[path.length - 1], channel);
        }
    }
