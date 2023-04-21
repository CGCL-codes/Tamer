    public void setNormalTextureNode(TextureGraphNode c) {
        normalTexNode = setTexNode(normalTexNode, c);
        if (!TextureEditor.GL_ENABLED) return;
        if (normalTexNode != null) channelChanged(normalTexNode.getChannel()); else glcanvas.updateNormalMap(null);
    }
