    public void reload() {
        if (layer != null) {
            int maxIndex = layer.getNumberOfChannels();
            int actualIndex = paramChannel.getSelectedIndex();
            paramChannel.removeAllItems();
            for (int i = 0; i < maxIndex; i++) {
                paramChannel.addItem(layer.getChannel(i));
            }
            if (actualIndex < maxIndex) paramChannel.setSelectedIndex(actualIndex);
        }
    }
