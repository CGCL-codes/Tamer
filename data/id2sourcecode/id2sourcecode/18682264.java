    void getChannelList() {
        mThread = new ClientTask(URLProvider.OP_GET_CHANNEL_LIST, mListener) {

            @Override
            protected void onFinished(HttpResponse response) throws Exception {
                final String body = getResponseBodyAsString(response.getEntity());
                LogUtil.i(Constants.TAG, "ChannelList response: " + body);
                final ChannelList channelList = JsonParser.parserChannels(body);
                clientDidGetChannelList(mClientListener, channelList);
            }

            @Override
            void setRequestURL() {
                setURL(URLProvider.getURL(mOp));
            }

            @Override
            protected boolean shouldAddSessionId() {
                return false;
            }
        };
        mThread.execute();
    }
