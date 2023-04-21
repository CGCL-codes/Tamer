    private int getCurrentCID() {
        int mCurrentID = -1;
        if (null != channelGallery) {
            mCurrentID = (mChannelAdapter.getChannelEntity().get(channelGallery.getFocus()).getCID());
        }
        return mCurrentID;
    }
