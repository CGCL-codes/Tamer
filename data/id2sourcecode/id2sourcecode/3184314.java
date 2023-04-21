    protected float getValidYOffset() {
        return -(1 << (((AClip) getChannelModel().getParent().getParent()).getSampleWidth() - 1));
    }
