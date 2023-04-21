    public void onClick(MarkerClickEvent event) {
        Marker m = event.getSender();
        String id = m.getTitle();
        if (mapPanel.isInDeletingMode()) {
            try {
                mapPanel.getInfoPanel().clear();
                mapPanel.getNodeManager().removeNode(id, mapPanel.getChannelManager().getChannels());
            } catch (Exception ex) {
                mapPanel.showMessage(ex.getMessage());
            }
            return;
        }
        handler.setPreviousValue(id);
        nodeEditBox.setText(id);
        nodeEditBox.addValueChangeHandler(handler);
        mapPanel.getInfoPanel().clear();
        mapPanel.getInfoPanel().add(nodeEditPanel);
    }
