    private void updateParameterChannel() {
        ALayer l = (ALayer) paramLayer.getSelectedItem();
        if (l != null) {
            int maxIndex = l.getNumberOfChannels();
            int index = paramChannel.getSelectedIndex();
            paramChannel.removeAllItems();
            for (int i = 0; i < maxIndex; i++) {
                paramChannel.addItem(l.getChannel(i));
            }
            if (index < maxIndex) paramChannel.setSelectedIndex(index);
        }
    }
