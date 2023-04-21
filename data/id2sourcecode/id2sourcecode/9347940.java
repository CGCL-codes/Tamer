    public void setHeightmapTextureNode(TextureGraphNode c) {
        heightmapTexNode = setTexNode(heightmapTexNode, c);
        if (!TextureEditor.GL_ENABLED) return;
        if (heightmapTexNode != null) channelChanged(heightmapTexNode.getChannel()); else glcanvas.updateHeightMap(null);
    }
