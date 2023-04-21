    void showSelectedChannelPopupMenu(TextureGraphNode node, int x, int y) {
        if (node.getChannel() instanceof Pattern) addToPresetsChannelMenuItem.setEnabled(true); else addToPresetsChannelMenuItem.setEnabled(false);
        replacepasteChannelMenuItem.setEnabled(toCopyTextureGraphNode != null);
        selectedChannelPopupMenu.show(this, x, y);
    }
