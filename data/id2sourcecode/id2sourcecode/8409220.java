    @Override
    protected ConnectResult doInBackground(Void... params) {
        try {
            ApiManager api = mApp.getApiManager();
            return api.connect(AndroidUtil.getDeviceId(mContext), AndroidUtil.getChannelId(mContext) + "");
        } catch (Exception e) {
            return null;
        }
    }
