    @Override
    public void onItemClickListener(View v, int position) {
        btnNext.setEnabled(true);
        if (isRecycled = true) {
            if (null != mPlayControllerListener) {
                mPlayControllerListener.onTrackChanged(getCurrentSongEntity());
            }
        }
        if (isFirst) {
            isFirst = false;
            return;
        }
        if (isBackground) {
            return;
        }
        LogUtil.i(Constants.TAG, "onItemClickListener");
        mLoadingBar.setVisibility(View.VISIBLE);
        mPlayList = null;
        mCurrentID = mChannelAdapter.getChannelEntity().get(position).getCID();
        NetWorkManager.getListByChannel(this, mCurrentID);
        MobclickAgent.onEvent(this, Constants.CHANNEL_CLICK_RATE, String.valueOf(mCurrentID));
    }
