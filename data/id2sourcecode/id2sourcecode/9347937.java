    public void setDiffuseTextureNode(TextureGraphNode c) {
        diffuseTexNode = setTexNode(diffuseTexNode, c);
        if (!TextureEditor.GL_ENABLED) return;
        if (diffuseTexNode != null) channelChanged(diffuseTexNode.getChannel()); else glcanvas.updateDiffuseMap(null);
    }
