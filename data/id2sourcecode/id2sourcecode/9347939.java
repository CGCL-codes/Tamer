    public void setSpecWeightTextureNode(TextureGraphNode c) {
        specWeightTexNode = setTexNode(specWeightTexNode, c);
        if (!TextureEditor.GL_ENABLED) return;
        if (specWeightTexNode != null) channelChanged(specWeightTexNode.getChannel()); else glcanvas.updateSpecWeightMap(null);
    }
