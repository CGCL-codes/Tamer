    public void render(BufferedImage target) {
        if (canRender(target)) {
            resizeFieldIfNeeded(target);
            myProceduralTexture.calculateField(myField, myAreaParameters);
            ChannelUtils.renderChannelsToImage(target, myField.getChannel(myRedChannelName), myField.getChannel(myGreenChannelName), myField.getChannel(myBlueChannelName), myField.getChannel(myAlphaChannelName));
        }
    }
