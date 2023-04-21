    private void refreshChannel(int defaultId) {
        ChannelList channelList = (ChannelList) getIntent().getSerializableExtra(Constants.EXTRA_KEY_CHANNELLIST_ENTITY);
        if (null == channelList) {
            channelList = BusfmApplication.getInstance().getChannelList();
        }
        if (null == channelList) {
            return;
        }
        mChannelList = channelList;
        LogUtil.i(Constants.TAG, mChannelList.toString());
        viewAnimator.setDisplayedChild(0);
        mPriaveChannelEntity = mChannelList.removePrivateList();
        BusfmApplication.getInstance().setPrivateEntitiy(mPriaveChannelEntity);
        mChannelAdapter = new ChannelAdapter(mChannelList.getchannelList());
        channelGallery.setAdapter(mChannelAdapter);
        channelGallery.setDefaultTab(defaultId);
    }
