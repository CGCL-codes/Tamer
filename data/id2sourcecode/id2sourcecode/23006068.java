    private void initFieldsFromIntent() {
        if (TextUtils.isEmpty(getIntent().getAction()) && getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            channelType = b.getString(News.Channel.CHANNEL_TYPE);
            feedID = b.getString(News.Channel._ID);
            feedName = b.getString(News.Channel.CHANNEL_NAME);
            feedLink = b.getString(News.Channel.CHANNEL_LINK);
            feedUpdateMsgs = b.getString(News.Channel.UPDATE_MSGS);
            Log.v(_TAG, "bundle: " + channelType + " " + " " + feedID + " " + feedName + " " + feedLink);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NewsreaderService.NEW_NEWS_NOTIFICATION);
        } else if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            feedID = mNews.findChannel(Channel.CONTENT_URI, getIntent().getDataString());
            feedLink = getIntent().getDataString();
            int channelTypeInt = getChannelType(getIntent().getType());
            channelType = String.valueOf(channelTypeInt);
            Log.v(_TAG, "view: " + channelType + " " + " " + feedID + " " + feedName + " " + feedLink);
            if (feedID == null) {
                HashMap map = new HashMap();
                map.put(News.Channel.CHANNEL_LINK, feedLink);
                AbstractFeedFetcherThread thread = null;
                if (channelTypeInt == News.CHANNEL_TYPE_ATM) {
                    thread = new AtomSaxFetcherThread(map, mNews, this);
                } else if (channelTypeInt == News.CHANNEL_TYPE_RSS) {
                    thread = new RSSSaxFetcherThread(map, mNews, this);
                } else {
                }
                if (thread != null) {
                    InputStream is = thread.fetch();
                    feedName = thread.parseTitle(is);
                } else {
                    feedName = getString(R.string.feedfromurl);
                }
                ContentValues cv = new ContentValues();
                cv.put(Channel.CHANNEL_LINK, feedLink);
                cv.put(Channel.CHANNEL_TYPE, channelType);
                cv.put(Channel.CHANNEL_NAME, feedName);
                feedUpdateMsgs = String.valueOf(0);
                cv.put(News.Channel.UPDATE_MSGS, feedUpdateMsgs);
                cv.put(News.Channel.NOTIFY_NEW, String.valueOf(0));
                Uri newUri = mNews.insert(Channel.CONTENT_URI, cv);
                feedID = newUri.getLastPathSegment();
                Log.v(_TAG, "new id: " + channelType + " " + " " + feedID + " " + feedName + " " + feedLink);
            } else {
                Cursor cursor = getContentResolver().query(Uri.withAppendedPath(Channel.CONTENT_URI, feedID), new String[] { Channel._ID, Channel.CHANNEL_TYPE, Channel.UPDATE_MSGS }, null, null, null);
                cursor.moveToFirst();
                channelType = cursor.getString(1);
                feedUpdateMsgs = cursor.getString(2);
                cursor.close();
                Log.v(_TAG, "old id: " + channelType + " " + " " + feedID + " " + feedName + " " + feedLink);
            }
        } else {
            Log.v(_TAG, "unsupported intent:" + getIntent());
            feedID = "-1";
        }
    }
