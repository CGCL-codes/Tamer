    public void channelChanged(Channel source) {
        if (!TextureEditor.GL_ENABLED) return;
        if ((diffuseTexNode != null) && (source == diffuseTexNode.getChannel())) glcanvas.updateDiffuseMap(diffuseTexNode.getChannel());
        if ((normalTexNode != null) && (source == normalTexNode.getChannel())) glcanvas.updateNormalMap(normalTexNode.getChannel());
        if ((specWeightTexNode != null) && (source == specWeightTexNode.getChannel())) glcanvas.updateSpecWeightMap(specWeightTexNode.getChannel());
        if ((heightmapTexNode != null) && (source == heightmapTexNode.getChannel())) glcanvas.updateHeightMap(heightmapTexNode.getChannel());
    }
