    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(Channel.NAME)) {
            String oldName = (String) pce.getOldValue();
            String newName = (String) pce.getNewValue();
            for (int i = 0; i < getComponentCount(); i++) {
                ChannelPanel chanPanel = (ChannelPanel) getComponent(i);
                ChannelOutComboBoxModel model = (ChannelOutComboBoxModel) chanPanel.getChannelOutModel();
                model.reconcile(oldName, newName);
            }
        }
    }
